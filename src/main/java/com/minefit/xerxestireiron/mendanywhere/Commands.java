package com.minefit.xerxestireiron.mendanywhere;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {
    private final MendAnywhere plugin;

    public Commands(MendAnywhere instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (!command.getName().equalsIgnoreCase("mendthis") || !(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mendanywhere.mend")) {
            player.sendMessage("You are not allowed to mend items that way.");
            return true;
        }

        ItemStack objectInHand = player.getInventory().getItemInMainHand();

        if (!validObject(objectInHand)) {
            player.sendMessage("This item cannot be mended.");
            return true;
        }

        repairItem(objectInHand, player);
        return true;
    }

    private boolean validObject(ItemStack item) {
        Material itemType = item.getType();
        return item.containsEnchantment(Enchantment.MENDING) && itemType.getMaxDurability() > 0;
    }

    private void repairItem(ItemStack item, Player player) {
        short durability = item.getDurability();
        int playerExp = Experience.getExp(player);

        if (durability == 0) {
            player.sendMessage("This item does not need mending.");
            return;
        }

        if (playerExp <= 0) {
            player.sendMessage("You don't have any exp to mend with.");
            return;
        }

        int expNeeded = durability / 2;

        if (playerExp >= expNeeded) {
            item.setDurability((short) 0);
            player.sendMessage("This item has been fully mended!");
        } else {
            int mendableDurability = durability - (playerExp * 2);
            item.setDurability((short) (mendableDurability));
            player.sendMessage("This item has been partially mended!");
        }

        Experience.changeExp(player, (int) -expNeeded);
        return;
    }
}
