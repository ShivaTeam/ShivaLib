package us.zonix.anticheat.client;

public class ModClientType implements ClientType
{
    private final String name;
    private final String modId;
    private final String modVersion;
    
    @Override
    public boolean isHacked() {
        return true;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getModId() {
        return this.modId;
    }
    
    public String getModVersion() {
        return this.modVersion;
    }
    
    public ModClientType(final String name, final String modId, final String modVersion) {
        this.name = name;
        this.modId = modId;
        this.modVersion = modVersion;
    }
}
