package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourcePackListener implements Listener {

    private final UnstableSMP plugin;

    public ResourcePackListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            // Start fake loading bar
            new org.bukkit.scheduler.BukkitRunnable() {
                float progress = 0.0f;
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }
                    // Stop if loaded (listener below will handle restore) or failed
                    // Actually checking status nicely inside runnable is hard, we rely on cancels or just timed
                    // But simpler: just run animation until we see them fully joined? 
                    // Or keep 'pending' map check.
                    if (!plugin.isPending(player.getUniqueId())) {
                        this.cancel();
                        return;
                    }
                    
                    progress += 0.05f;
                    if (progress > 1.0f) progress = 0.0f; // Loop
                    
                    net.kyori.adventure.text.Component msg = com.resolutestudios.unstablesmp.utils.ProgressBar.create(progress)
                        .append(net.kyori.adventure.text.Component.text(" Loading Resource Pack..."));
                    
                    player.sendActionBar(msg);
                }
            }.runTaskTimer(plugin, 0L, 5L); // Update every 5 ticks
            
            return;
        }

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED ||
                event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED ||
                event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {

            // "If the player autodeclines the resorcepack, it just puts them direcly at
            // their logout location"
            // So for ANY terminal status (Accepted, Declined, Failed), we restore them.
            // Note: ACCEPTED should technically wait for LOADED, but if client doesn't send
            // LOADED,
            // we might want a timeout or manual skip. Standard flow is Accepted -> Loaded.
            // Use skiprp if stuck.

            plugin.restorePlayer(player);
        }
    }
}
