package us.zonix.anticheat.manager;

import java.util.*;
import us.zonix.anticheat.log.*;
import java.util.concurrent.*;

public class LogManager
{
    private final Queue<Log> logQueue;
    
    public LogManager() {
        this.logQueue = new ConcurrentLinkedQueue<Log>();
    }
    
    public void addToLogQueue(final Log log) {
        this.logQueue.add(log);
    }
    
    public void removeFromLogQueue(final Log log) {
        this.logQueue.remove(log);
    }
    
    public Queue<Log> getLogQueue() {
        return this.logQueue;
    }
}
