package team.shiva.shivalib.tablist.tab;

import team.shiva.shivalib.tablist.abstraction.EntityPlayerWrapper;
import team.shiva.shivalib.tablist.util.StringUtil;
import org.bukkit.scoreboard.Team;

public class BlankTabRow implements TabRow, StandardRow
{
    private EntityPlayerWrapper rowPlayer;
    private Team rowTeam;

    @Override
    public String getRowString() {
        return new StringUtil().randomColorString();
    }

    @Override
    public int getPing(){
        return 0;
    }

    @Override
    public void setRowPlayer(final EntityPlayerWrapper rowPlayer) {
        this.rowPlayer = rowPlayer;
    }

    @Override
    public boolean equals(Object object) {
        if(object.getClass().equals(this.getClass())){
            return true;
        }else{
            return false;
        }
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
