package net.evilblock.pidgin.packet.handler;

public class PacketExceptionHandler {
    public void onException(Exception e) {
        System.out.println("Failed to send packet");
        e.printStackTrace();
    }
}

