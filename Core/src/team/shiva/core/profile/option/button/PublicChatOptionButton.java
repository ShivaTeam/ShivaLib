package team.shiva.core.profile.option.button;

import team.shiva.core.profile.Profile;
import team.shiva.core.util.ItemBuilder;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PublicChatOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&a&lPublic Chat";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, you will receive public chat messages.";
	}

	@Override
	public String getEnabledOption() {
		return "Receive public chat messages";
	}

	@Override
	public String getDisabledOption() {
		return "Do not receive public chat messages";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.getProfiles().get(player.getUniqueId()).getOptions().publicChatEnabled();
	}

	@Override
	public void clicked(Player player, int slot, ClickType clickType) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());
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
