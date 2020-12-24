package net.evilblock.pidgin.packet;

import com.google.gson.JsonObject;

public interface Packet {
    public int id();

    public JsonObject serialize();

    public void deserialize(JsonObject var1);
}

