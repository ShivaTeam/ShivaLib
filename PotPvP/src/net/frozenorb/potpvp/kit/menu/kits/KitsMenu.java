package net.frozenorb.potpvp.kit.menu.kits;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.KitHandler;
import net.frozenorb.potpvp.kittype.KitType;
import net.frozenorb.potpvp.kittype.menu.select.SelectKitTypeMenu;
import net.frozenorb.potpvp.util.InventoryUtils;
import net.frozenorb.potpvp.util.menu.MenuBackButton;
import team.shiva.shivalib.menu.Button;
import team.shiva.shivalib.menu.Menu;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class KitsMenu extends Menu {

    private final KitType kitType;

    public KitsMenu(KitType kitType) {
        super("Viewing " + kitType.getDisplayName() + " kits");

        setPlaceholder(true);
        setAutoUpdate(true);

        this.kitType = kitType;
    }

    @Override
    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        KitHandler kitHandler = PotPvPSI.getInstance().getKitHandler();
        Map<Integer, Button> buttons = new HashMap<>();

        // kit slots are 1-indexed
        for (int kitSlot = 1; kitSlot <= KitHandler.KITS_PER_TYPE; kitSlot++) {
            Optional<Kit> kitOpt = kitHandler.getKit(player, kitType, kitSlot);
            int column = (kitSlot * 2) - 1; // - 1 to compensate for this being 0-indexed

            buttons.put(getSlot(column, 0), new KitIconButton(kitOpt, kitType, kitSlot));
            buttons.put(getSlot(column, 2), new KitEditButton(kitOpt, kitType, kitSlot));

            if (kitOpt.isPresent()) {
                buttons.put(getSlot(column, 3), new KitRenameButton(kitOpt.get()));
                buttons.put(getSlot(column, 4), new KitDeleteButton(kitType, kitSlot));
            } else {
                buttons.put(getSlot(column, 3), Button.placeholder(Material.STAINED_GLASS_PANE, DyeColor.GRAY.getWoolData(), ""));
                buttons.put(getSlot(column, 4), Button.placeholder(Material.STAINED_GLASS_PANE, DyeColor.GRAY.getWoolData(), ""));
            }
        }

        buttons.put(getSlot(0, 4), new MenuBackButton(p -> {
            new SelectKitTypeMenu(kitType -> {
                new KitsMenu(kitType).openMenu(p);
            }, "Select a kit type...").openMenu(p);
        }));

        return buttons;
    }

}