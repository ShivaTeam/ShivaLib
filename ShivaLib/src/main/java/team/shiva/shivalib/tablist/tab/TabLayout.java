package team.shiva.shivalib.tablist.tab;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TabLayout {
    protected static int WIDTH = 3;
    protected static int HEIGHT = 20;
    private final String[] tabNames =new String[WIDTH * HEIGHT];
    private final int[] tabPings = new int[WIDTH * HEIGHT];
    public TabLayout(){
        for(int i = 0; i < WIDTH * HEIGHT; i++){
            tabNames[i] = null;
            tabPings[i] = 0;
        }
    }
    public void set(int x, int y, String name, int ping) {
        if (!this.validate(x, y, true)) {
            return;
        }
        int pos = y + x * HEIGHT;
        this.tabNames[pos] = ChatColor.translateAlternateColorCodes('&', name);
        this.tabPings[pos] = ping;
    }

    public void set(int x, int y, String name) {
        this.set(x, y, name, 0);
    }
    public boolean validate(int x, int y, boolean silent) {
        if (x >= WIDTH) {
            if (!silent) {
                throw new IllegalArgumentException("x >= WIDTH (" + WIDTH + ")");
            }
            return false;
        }
        if (y >= HEIGHT) {
            if (!silent) {
                throw new IllegalArgumentException("y >= HEIGHT (" + HEIGHT + ")");
            }
            return false;
        }
        return true;
    }
    public List<StandardRow> getStandardRows(){
        List<StandardRow> standardRows = new ArrayList<>();
        int pos = 0;
        for(int i = 0;i < WIDTH;i++){
            for(int j = 0; j < HEIGHT;j++,pos++){
                if(tabNames[pos] == null){
                    standardRows.add(new BlankTabRow());
                }else{
                    standardRows.add(new StaticTabRow(tabNames[pos], tabPings[pos]));
                }
            }
        }
        return standardRows;
    }
}
