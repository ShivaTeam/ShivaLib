package team.shiva.core.profile.option.button;

import team.shiva.core.profile.Profile;
import team.shiva.core.util.ItemBuilder;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PrivateChatSoundsOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&e&lPrivate Chat Sounds";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.NOTE_BLOCK).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.NOTE_BLOCK).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, a sound will be played when you receive a private chat message.";
	}

	@Override
	public String getEnabledOption() {
		return "Play private chat message sounds";
	}

	@Override
	public String getDisabledOption() {
		return "Do not play private chat message sounds";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.getProfiles().get(player.getUniqueId()).getOptions().playingMessageSounds();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());
	}

	@Override
	public List<String> getDescription(Player arg0) {
		return null;
	}

	@Override
	public Material getMaterial(Player arg0) {
		return null;
	}

	@Override
	public String getName(Player arg0) {
		return null;
	}

}
