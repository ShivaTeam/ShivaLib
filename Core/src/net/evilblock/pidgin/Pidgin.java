package net.evilblock.pidgin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.evilblock.pidgin.packet.Packet;
import net.evilblock.pidgin.packet.handler.IncomingPacketHandler;
import net.evilblock.pidgin.packet.handler.PacketExceptionHandler;
import net.evilblock.pidgin.packet.listener.PacketListener;
import net.evilblock.pidgin.packet.listener.PacketListenerData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class Pidgin {
    private static JsonParser PARSER = new JsonParser();
    private final String channel;
    private JedisPool jedisPool;
    private JedisPubSub jedisPubSub;
    private List<PacketListenerData> packetListeners = new ArrayList<PacketListenerData>();
    private Map<Integer, Class> idToType = new HashMap<Integer, Class>();
    private Map<Class, Integer> typeToId = new HashMap<Class, Integer>();

    public Pidgin(String channel, String host, int port, String password) {
        this.channel = channel;
        this.packetListeners = new ArrayList<PacketListenerData>();
        this.jedisPool = new JedisPool(host, port);
        if (password != null && !password.equals("")) {
            try (Jedis jedis = this.jedisPool.getResource();){
                jedis.auth(password);
            }
        }
        this.setupPubSub();
    }

    public void sendPacket(Packet packet) {
        this.sendPacket(packet, null);
    }

    public void sendPacket(Packet packet, PacketExceptionHandler exceptionHandler) {
        block15: {
            try {
                JsonObject object = packet.serialize();
                if (object == null) {
                    throw new IllegalStateException("Packet cannot generate null serialized data");
                }
                try (Jedis jedis = this.jedisPool.getResource();){
                    jedis.publish(this.channel, packet.id() + ";" + object.toString());
                }
            }
            catch (Exception e) {
                if (exceptionHandler == null) break block15;
                exceptionHandler.onException(e);
            }
        }
    }

    public Packet buildPacket(int id) {
        if (!this.idToType.containsKey(id)) {
            throw new IllegalStateException("A packet with that ID does not exist");
        }
        try {
            return (Packet)this.idToType.get(id).newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create new instance of packet type");
        }
    }

    public void registerPacket(Class clazz) {
        try {
            int id = (Integer)clazz.getDeclaredMethod("id", new Class[0]).invoke(clazz.newInstance(), null);
            if (this.idToType.containsKey(id) || this.typeToId.containsKey(clazz)) {
                throw new IllegalStateException("A packet with that ID has already been registered");
            }
            this.idToType.put(id, clazz);
            this.typeToId.put(clazz, id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListener(PacketListener packetListener) {
        for (Method method : packetListener.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(IncomingPacketHandler.class) == null) continue;
            Class<?> packetClass = null;
            if (method.getParameters().length > 0 && Packet.class.isAssignableFrom(method.getParameters()[0].getType())) {
                packetClass = method.getParameters()[0].getType();
            }
            if (packetClass == null) continue;
            this.packetListeners.add(new PacketListenerData(packetListener, method, packetClass));
        }
    }

    private void setupPubSub() {
        this.jedisPubSub = new JedisPubSub(){

            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase(Pidgin.this.channel)) {
                    try {
                        String[] args = message.split(";");
                        Integer id = Integer.valueOf(args[0]);
                        Packet packet = Pidgin.this.buildPacket(id);
                        if (packet != null) {
                            packet.deserialize(PARSER.parse(args[1]).getAsJsonObject());
                            for (PacketListenerData data : Pidgin.this.packetListeners) {
                                if (!data.matches(packet)) continue;
                                data.getMethod().invoke(data.getInstance(), packet);
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println("[Pidgin] Failed to handle message");
                        e.printStackTrace();
                    }
                }
            }
        };
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = this.jedisPool.getResource();){
                jedis.subscribe(this.jedisPubSub, this.channel);
            }
        });
    }
}

