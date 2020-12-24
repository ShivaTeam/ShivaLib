package team.shiva.core.util.menu.button;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import team.shiva.core.util.CC;
import team.shiva.core.util.ItemBuilder;
import team.shiva.core.util.menu.Button;
import team.shiva.core.util.menu.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {

	private Menu back;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.REDSTONE)
				.name(CC.RED + CC.BOLD + "Back")
				.lore(Arrays.asList(
						CC.RED + "Click here to return to",
						CC.RED + "the previous menu.")
				)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Button.playNeutral(player);
		back.openMenu(player);
	}

}
