package us.zonix.anticheat.client;

public class PayloadClientType implements ClientType
{
    private final String name;
    private final String payload;
    private final boolean hacked;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getPayload() {
        return this.payload;
    }
    
    @Override
    public boolean isHacked() {
        return this.hacked;
    }
    
    public PayloadClientType(final String name, final String payload, final boolean hacked) {
        this.name = name;
        this.payload = payload;
        this.hacked = hacked;
    }
}
