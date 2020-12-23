package team.shiva.shivalib.nametag;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public abstract class NametagProvider {
    @Getter private final String name;
    @Getter private final int weight;

    public abstract NametagInfo fetchNametag(Player toRefresh, Player refreshFor);

    @ConstructorProperties(value={"name", "weight"})
    public NametagProvider(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    protected static final class DefaultNametagProvider
    extends NametagProvider {
        public DefaultNametagProvider() {
            super("Default Provider", 0);
        }

        @Override
        public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
            return new NametagInfo();
        }
    }
}

