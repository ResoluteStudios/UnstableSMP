package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class ItemRestrictionListener implements Listener {

    private final UnstableSMP plugin;

    public ItemRestrictionListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSmithingResult(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof SmithingInventory))
            return;
        if (event.getSlotType() != org.bukkit.event.inventory.InventoryType.SlotType.RESULT)
            return;

        if (plugin.getConfig().getBoolean("features.netheriteban", false)) {
            ItemStack current = event.getCurrentItem();
            if (current != null && isNetherite(current.getType())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage("§cNetherite creation is disabled!");
            }
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (plugin.getConfig().getBoolean("features.maceban", false)) {
            if (event.getInventory().getResult() != null
                    && event.getInventory().getResult().getType() == Material.MACE) {
                event.getInventory().setResult(null);
            }
        }
    }

    private boolean isNetherite(Material type) {
        return type == Material.NETHERITE_HELMET ||
                type == Material.NETHERITE_CHESTPLATE ||
                type == Material.NETHERITE_LEGGINGS ||
                type == Material.NETHERITE_BOOTS ||
                type == Material.NETHERITE_SWORD ||
                type == Material.NETHERITE_AXE ||
                type == Material.NETHERITE_PICKAXE ||
                type == Material.NETHERITE_SHOVEL ||
                type == Material.NETHERITE_HOE;
    }
}
