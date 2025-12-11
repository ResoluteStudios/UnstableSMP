package com.resolutestudios.unstablesmp.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CombatListener implements Listener {

    private final UnstableSMP plugin;
    private final Set<UUID> maceAttackers = new HashSet<>();

    public CombatListener(UnstableSMP plugin) {
        this.plugin = plugin;

        // Register ProtocolLib Packet Listener
        if (plugin.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            com.comphenix.protocol.ProtocolLibrary.getProtocolManager().addPacketListener(
                    new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
                        @Override
                        public void onPacketReceiving(PacketEvent event) {
                            if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                                PacketContainer packet = event.getPacket();
                                // Check if action is ATTACK
                                // StructureModifier for EnumWrappers might differ by version,
                                // but usually index 1 is interaction type if index 0 is entity ID.
                                // However, simple approach: check if player holds mace.

                                Player player = event.getPlayer();
                                ItemStack item = player.getInventory().getItemInMainHand();

                                // We only care if they are holding a MACE when sending the packet
                                if (item.getType() == Material.MACE) {
                                    // Mark usage for the next tick (when damage event fires)
                                    synchronized (maceAttackers) {
                                        maceAttackers.add(player.getUniqueId());
                                    }

                                    // Schedule cleanup
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            synchronized (maceAttackers) {
                                                maceAttackers.remove(player.getUniqueId());
                                            }
                                        }
                                    }.runTaskLater(plugin, 2); // 2 ticks expire
                                }
                            }
                        }
                    });
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfig().getBoolean("features.macenerf", false))
            return;
        if (!(event.getDamager() instanceof Player))
            return;

        Player attacker = (Player) event.getDamager();
        UUID uuid = attacker.getUniqueId();

        boolean isMaceAttack = false;
        synchronized (maceAttackers) {
            isMaceAttack = maceAttackers.contains(uuid);
        }

        // Fallback: If packet listener failed or not installed, check normally
        if (!isMaceAttack && attacker.getInventory().getItemInMainHand().getType() == Material.MACE) {
            isMaceAttack = true;
        }

        if (isMaceAttack) {
            // Apply damage nerf
            event.setDamage(event.getDamage() * 0.666);

            // Disable Wind Burst
            // We check if the item has Wind Burst enchantment
            ItemStack item = attacker.getInventory().getItemInMainHand();
            // Note: Paper API 1.21 might map Wind Burst to a specific Enchantment key.
            // If unknown, we iterate or assume standard name.
            // "minecraft:wind_burst" -> Enchantment.getByKey(...)
            // To be safe, we will remove ALL enchantments if it's a mace? No, that's too
            // aggressive.
            // We'll try to find Wind Burst by name or key if possible.

            // Since specific API might be missing in this context, we will try standard
            // iteration
            // if we can identify it.
            // However, without exact key, we can try removing checking specific
            // enchantment.

            // Simplest "Disable": Cancel event and deal raw damage? No.
            // Logic: Remove enchantment -> Event Process -> Add back.

            // Note: Wind Burst is "wind_burst"
            org.bukkit.NamespacedKey key = org.bukkit.NamespacedKey.minecraft("wind_burst");
            Enchantment windBurst = Enchantment.getByKey(key);

            if (windBurst != null && item.containsEnchantment(windBurst)) {
                int level = item.getEnchantmentLevel(windBurst);
                item.removeEnchantment(windBurst);

                // Restore next tick
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        item.addUnsafeEnchantment(windBurst, level);
                    }
                }.runTask(plugin);
            }
        }
    }
}
