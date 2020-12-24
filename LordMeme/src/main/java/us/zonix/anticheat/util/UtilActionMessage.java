package us.zonix.anticheat.util;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.inventory.ItemStack;

public class UtilActionMessage
{
    private List<AMText> Text;
    
    public UtilActionMessage() {
        this.Text = new ArrayList<AMText>();
    }
    
    public AMText addText(final String Message) {
        final AMText Text = new AMText(Message);
        this.Text.add(Text);
        return Text;
    }
    
    public String getFormattedMessage() {
        String Chat = "[\"\",";
        for (final AMText Text : this.Text) {
            Chat = Chat + Text.getFormattedMessage() + ",";
        }
        Chat = Chat.substring(0, Chat.length() - 1);
        Chat += "]";
        return Chat;
    }
    
    public void sendToPlayer(final Player Player) {
        final IChatBaseComponent base = IChatBaseComponent.ChatSerializer.a(this.getFormattedMessage());
        final PacketPlayOutChat packet = new PacketPlayOutChat(base, (byte)1);
        ((CraftPlayer)Player).getHandle().playerConnection.sendPacket((Packet)packet);
    }
    
    public enum ClickableType
    {
        RunCommand("run_command"), 
        SuggestCommand("suggest_command"), 
        OpenURL("open_url");
        
        public String Action;
        
        private ClickableType(final String Action) {
            this.Action = Action;
        }
    }
    
    public class AMText
    {
        private String Message;
        private Map<String, Map.Entry<String, String>> Modifiers;
        
        public AMText(final String Text) {
            this.Message = "";
            this.Modifiers = new HashMap<String, Map.Entry<String, String>>();
            this.Message = Text;
        }
        
        public String getMessage() {
            return this.Message;
        }
        
        public String getFormattedMessage() {
            String Chat = "{\"text\":\"" + this.Message + "\"";
            for (final String Event : this.Modifiers.keySet()) {
                final Map.Entry<String, String> Modifier = this.Modifiers.get(Event);
                Chat = Chat + ",\"" + Event + "\":{\"action\":\"" + Modifier.getKey() + "\",\"value\":" + Modifier.getValue() + "}";
            }
            Chat += "}";
            return Chat;
        }
        
        public AMText addHoverText(final String... Text) {
            final String Event = "hoverEvent";
            final String Key = "show_text";
            String Value = "";
            if (Text.length == 1) {
                Value = "{\"text\":\"" + Text[0] + "\"}";
            }
            else {
                Value = "{\"text\":\"\",\"extra\":[";
                for (final String Message : Text) {
                    Value = Value + "{\"text\":\"" + Message + "\"},";
                }
                Value = Value.substring(0, Value.length() - 1);
                Value += "]}";
            }
            final Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, Value);
            this.Modifiers.put(Event, Values);
            return this;
        }
        
        public AMText addHoverItem(final ItemStack Item) {
            final String Event = "hoverEvent";
            final String Key = "show_item";
            final String Value = CraftItemStack.asNMSCopy(Item).getTag().toString();
            final Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, Value);
            this.Modifiers.put(Event, Values);
            return this;
        }
        
        public AMText setClickEvent(final ClickableType Type, final String Value) {
            final String Event = "clickEvent";
            final String Key = Type.Action;
            final Map.Entry<String, String> Values = new AbstractMap.SimpleEntry<String, String>(Key, "\"" + Value + "\"");
            this.Modifiers.put(Event, Values);
            return this;
        }
    }
}
