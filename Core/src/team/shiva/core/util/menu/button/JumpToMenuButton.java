package team.shiva.core.util.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import team.shiva.core.util.menu.Button;
import team.shiva.core.util.menu.Menu;

public class JumpToMenuButton extends Button {

	private Menu menu;
	private ItemStack itemStack;

	public JumpToMenuButton(Menu menu, ItemStack itemStack) {
		this.menu = menu;
		this.itemStack = itemStack;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return itemStack;
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		menu.openMenu(player);
	}

}
