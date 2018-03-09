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

        objectInHand = repairItem(objectInHand, player);

        return true;
    }

    private boolean validObject(ItemStack item) {
        Material itemType = item.getType();

        if (!item.getItemMeta().hasEnchant(Enchantment.MENDING) || itemType.getMaxDurability() <= 0) {
            return false;
        }

        return true;
    }

    private ItemStack repairItem(ItemStack item, Player player) {
        short durability = item.getDurability();
        int currentExp = Experience.getExp(player);

        if (durability == 0) {
            player.sendMessage("This item does not need mending.");
            return item;
        }

        if (currentExp <= 0) {
            player.sendMessage("You don't have any exp to mend with.");
            return item;
        }

        int expNeeded = durability / 2;

        if (currentExp >= expNeeded) {
            item.setDurability((short) 0);
            player.sendMessage("This item has been fully mended!");
        } else {
            int mendableDurability = durability - (currentExp * 2);
            item.setDurability((short) (durability - mendableDurability));
            player.sendMessage("This item has been partially mended!");
        }

        Experience.changeExp(player, (int) -expNeeded);
        return item;
    }

}
