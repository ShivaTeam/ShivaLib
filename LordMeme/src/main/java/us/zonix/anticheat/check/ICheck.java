package us.zonix.anticheat.check;

import org.bukkit.entity.*;

public interface ICheck<T>
{
    void handleCheck(final Player p0, final T p1);
    
    Class<? extends T> getType();
}
