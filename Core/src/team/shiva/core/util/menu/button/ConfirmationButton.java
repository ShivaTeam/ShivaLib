package team.shiva.core.util.menu.button;

import lombok.AllArgsConstructor;
import team.shiva.core.util.callback.TypeCallback;
import team.shiva.core.util.menu.Button;
import team.shiva.core.util.menu.Menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public class ConfirmationButton extends Button {

	private boolean confirm;
	private TypeCallback<Boolean> callback;
	private boolean closeAfterResponse;

	@Override
	public ItemStack getButtonItem(Player player) {
		ItemStack itemStack = new ItemStack(Material.WOOL, 1, this.confirm ? ((byte) 5) : ((byte) 14));
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(this.confirm ? ChatColor.GREEN + "Confirm" : ChatColor.RED + "Cancel");
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (this.confirm) {
			player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20f, 0.1f);
		} else {
			player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20f, 0.1F);
		}

		if (this.closeAfterResponse) {
			Menu menu = Menu.currentlyOpenedMenus.get(player.getName());

			if (menu != null) {
				menu.setClosedByMenu(true);
			}

			player.closeInventory();
		}

		this.callback.callback(this.confirm);
	}

}
