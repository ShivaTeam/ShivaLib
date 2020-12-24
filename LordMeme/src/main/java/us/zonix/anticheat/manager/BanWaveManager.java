package us.zonix.anticheat.manager;

import us.zonix.anticheat.*;
import java.util.*;

public class BanWaveManager
{
    private final LordMeme plugin;
    private final Deque<Long> cheaters;
    private final Map<Long, String> cheaterNames;
    private boolean banWaveStarted;
    
    public void loadCheaters() {
    }
    
    public void clearCheaters() {
        this.cheaters.clear();
        this.cheaterNames.clear();
    }
    
    public long queueCheater() {
        return this.cheaters.poll();
    }
    
    public String getCheaterName(final long id) {
        return this.cheaterNames.get(id);
    }
    
    public LordMeme getPlugin() {
        return this.plugin;
    }
    
    public Deque<Long> getCheaters() {
        return this.cheaters;
    }
    
    public Map<Long, String> getCheaterNames() {
        return this.cheaterNames;
    }
    
    public boolean isBanWaveStarted() {
        return this.banWaveStarted;
    }
    
    public BanWaveManager(final LordMeme plugin) {
        this.cheaters = new LinkedList<Long>();
        this.cheaterNames = new HashMap<Long, String>();
        this.plugin = plugin;
    }
    
    public void setBanWaveStarted(final boolean banWaveStarted) {
        this.banWaveStarted = banWaveStarted;
    }
}
