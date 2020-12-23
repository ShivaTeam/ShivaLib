package team.shiva.shivalib.tablist.tab;

import lombok.Getter;
import team.shiva.shivalib.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.Team;

public class StaticTabRow implements TabRow, StandardRow
{
    @Getter private String rowString;
    @Getter private int ping;
    private EntityPlayerWrapper rowPlayer;
    private Team rowTeam;

    public StaticTabRow(final String rowString, int ping) {
        this.rowString = rowString;
        this.ping = ping;
    }

    @Override
    public boolean equals(Object object) {
        if(object.getClass().equals(this.getClass())){
            StaticTabRow staticTabRow = (StaticTabRow)object;
            if(rowString.equals(staticTabRow.getRowString()) && pingToBars(ping) == pingToBars(staticTabRow.getPing())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRowPlayer(final EntityPlayerWrapper rowPlayer) {
        this.rowPlayer = rowPlayer;
    }

    @Override
    public String getRowString() {
        return this.rowString;
    }

    @Override
    public int getPing(){
        return this.ping;
    }

    @Override
    public void setRowTeam(final Team rowTeam) {
        this.rowTeam = rowTeam;
    }

    @Override
    public EntityPlayerWrapper getRowPlayer() {
        return this.rowPlayer;
    }
}
