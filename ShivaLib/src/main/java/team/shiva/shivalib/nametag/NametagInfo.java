package team.shiva.shivalib.nametag;

import lombok.Getter;
import org.bukkit.ChatColor;

public final class NametagInfo {
    ChatColor color;
    boolean showHealth;

    public NametagInfo() {
        this.color = null;
        this.showHealth = false;
    }

    public NametagInfo(ChatColor color) {
        this.color = color;
        this.showHealth = false;
    }

    public NametagInfo(ChatColor color, boolean showHealth) {
        this.color = color;
        this.showHealth = showHealth;
    }

    public boolean equals(Object other) {
        if (other instanceof NametagInfo) {
            NametagInfo otherNametag = (NametagInfo)other;
            return this.color.equals(otherNametag.color) && this.showHealth == otherNametag.showHealth;
        }
        return false;
    }


}

