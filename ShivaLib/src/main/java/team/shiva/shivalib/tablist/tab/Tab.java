package team.shiva.shivalib.tablist.tab;

import team.shiva.shivalib.tablist.TablistHandler;
import team.shiva.shivalib.tablist.abstraction.EntityPlayerWrapper;
import team.shiva.shivalib.tablist.util.StringUtil;
import team.shiva.shivalib.tablist.util.TabIcon;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Tab
{
    private Player player;
    private String tabTitle;
    private String tabFooter;
    private List<TabRow> tabRows;
    private Random random;

    private Map<Integer, EntityPlayerWrapper> slots = new HashMap<>();

    public Tab(final Player player, final String tabTitle, final String tabFooter) {
        this.tabRows = new ArrayList<TabRow>();
        this.random = new Random();
        this.player = player;
        this.tabTitle = tabTitle;
        this.tabFooter = tabFooter;
    }

    private EntityPlayerWrapper[] tabRowList7() {
        final EntityPlayerWrapper[] tabRowList = new EntityPlayerWrapper[this.slots.size()];
        for (int i = 0; i < this.slots.size() / 3; ++i) {
            tabRowList[i * 3] = this.slots.get(i);
            tabRowList[i * 3 + 1] = this.slots.get(20 + i);
            tabRowList[i * 3 + 2] = this.slots.get(40 + i);
        }
        return tabRowList;
    }

    private EntityPlayerWrapper[] tabRowList() {
        return slots.values().toArray(new EntityPlayerWrapper[slots.size()]);
    }

    public void clearTab() {
        TablistHandler.getPacketUtil().clearRows(this.player, this.tabRowList());
        TablistHandler.getPacketUtil().setFooterAndHeader(this.player, "", "");
        this.tabRows.clear();
        this.slots.clear();
    }

    public void sendTab7() {
        TablistHandler.getPacketUtil().clearTab(this.player);
        TablistHandler.getPacketUtil().setFooterAndHeader(this.player, this.tabTitle, this.tabFooter);
        TablistHandler.getPacketUtil().sendRows(this.player, this.tabRowList7());
    }

    public void sendTab() {
        TablistHandler.getPacketUtil().clearTab(this.player);
        TablistHandler.getPacketUtil().setFooterAndHeader(this.player, this.tabTitle, this.tabFooter);
        TablistHandler.getPacketUtil().sendRows(this.player, this.tabRowList());
    }

    public List<TabRow> getTabRows() {
        return this.tabRows;
    }

    private Team getTeam(final Player player, final int index) {
        final String alphabeticalTeamName = new StringUtil().getAlphabeticalString(index) + this.random.nextInt(1000000);
        Team toReturn;
        if (player.getScoreboard().getTeam(alphabeticalTeamName) == null) {
            toReturn = player.getScoreboard().registerNewTeam(alphabeticalTeamName);
        }
        else {
            toReturn = player.getScoreboard().getTeam(alphabeticalTeamName);
        }
        return toReturn;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void addTabRow(final StandardRow standardRow, final TabIcon tabIcon) {
        int index = this.slots.size();

        if (standardRow instanceof StaticTabRow) {
            final String[] rowString = new StringUtil().splitString(standardRow.getRowString());
            final EntityPlayerWrapper rowPlayer = TablistHandler.getPacketUtil().newRow(new StringUtil().fillEndString(rowString[1]), tabIcon, standardRow.getPing());
            standardRow.setRowPlayer(rowPlayer);
            final Team rowTeam = this.getTeam(this.player, index);
            rowTeam.addEntry(rowPlayer.getName());
            if (rowString[0] != null) {
                rowTeam.setPrefix(rowString[0]);
            }
            if (rowString[2] != null) {
                rowTeam.setSuffix(rowString[2]);
            }
            standardRow.setRowTeam(rowTeam);
            this.slots.put(index, rowPlayer);
        }
        else {
            final EntityPlayerWrapper rowPlayer2 = TablistHandler.getPacketUtil().newRow(new StringUtil().fillEndString(standardRow.getRowString()), tabIcon, standardRow.getPing());
            standardRow.setRowPlayer(rowPlayer2);
            final Team rowTeam2 = this.getTeam(this.player, index);
            rowTeam2.addEntry(rowPlayer2.getName());
            standardRow.setRowTeam(rowTeam2);
            this.slots.put(index, rowPlayer2);
        }
        this.tabRows.add((TabRow)standardRow);
    }



    public void addTabRow(final StandardRow standardRow) {
        this.addTabRow(standardRow, TabIcon.GREY);
    }

    public boolean isDifferentFrom(List<StandardRow> standardRows) {
        if(standardRows.size() != tabRows.size()){
            return true;
        }
        TabRow tabRow;
        StandardRow standardRow;
        for(int i=0; i<tabRows.size(); i++){
            tabRow = tabRows.get(i);
            if(!(tabRow instanceof StandardRow)){
                return true;
            }
            if(!standardRows.get(i).equals(tabRow)){
                return true;
            }
        }
        return false;
    }
}