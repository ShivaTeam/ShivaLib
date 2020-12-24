package us.zonix.anticheat.log;

import java.util.UUID;

public class Log
{
    private final long timestamp;
    private final UUID uuid;
    private final String log;
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public String getLog() {
        return this.log;
    }
    
    public Log(final UUID uuid, final String log) {
        this.timestamp = System.currentTimeMillis();
        this.uuid = uuid;
        this.log = log;
    }
}
