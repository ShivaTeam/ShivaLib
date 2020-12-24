package us.zonix.anticheat.client;

public enum EnumClientType
{
    COSMIC_CLIENT(false, "Cosmic-Client"),
    CHEAT_BREAKER(false, "Cheat-Breaker"),
    VANILLA(false, "Regular-Client"),
    FORGE(false, "Forge-Client"),
    OCMC(false, "OCMC-Client"),
    HACKED_CLIENT_A(true, "Hacked-Client"), 
    HACKED_CLIENT_B(true, "Hacked-Client"), 
    HACKED_CLIENT_C(true, "Hacked-Client"), 
    HACKED_CLIENT_C2(true, "Hacked-Client"), 
    HACKED_CLIENT_C3(true, "Hacked-Client"), 
    HACKED_CLIENT_D(true, "Hacked-Client"), 
    HACKED_CLIENT_E(true, "Hacked-Client"), 
    HACKED_CLIENT_E2(true, "Hacked-Client"), 
    HACKED_CLIENT_F(true, "Hacked-Client"), 
    HACKED_CLIENT_G(true, "Hacked-Client"),
    HACKED_CLIENT_H(true, "Hacked-Client"),
    HACKED_CLIENT_I(true, "Hacked-Client"),
    HACKED_CLIENT_J(true, "Hacked-Client"),
    HACKED_CLIENT_K(true, "Hacked-Client"),
    HACKED_CLIENT_L(true, "Hacked-Client"),
    HACKED_CLIENT_L2(true, "Hacked-Client"),
    HACKED_CLIENT_M(true, "Hacked-Client");
    
    private final boolean hacked;
    private final String name;
    
    public boolean isHacked() {
        return this.hacked;
    }
    
    public String getName() {
        return this.name;
    }
    
    private EnumClientType(final boolean hacked, final String name) {
        this.hacked = hacked;
        this.name = name;
    }
}
