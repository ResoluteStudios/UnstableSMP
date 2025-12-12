package com.resolutestudios.unstablesmp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import com.resolutestudios.unstablesmp.UnstableSMP;

public class ResourcePackListener implements Listener {

    private final UnstableSMP plugin;

    public ResourcePackListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            // Start loading bar
            new org.bukkit.scheduler.BukkitRunnable() {
                float progress = 0.0f;
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }
                    // Stop if no longer pending
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

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            // Wait 10 seconds after successful load, then restore player
            new org.bukkit.scheduler.BukkitRunnable() {
                int secondsLeft = 10;
                
                @Override
                public void run() {
                    if (!player.isOnline() || !plugin.isPending(player.getUniqueId())) {
                        this.cancel();
                        return;
                    }
                    
                    if (secondsLeft > 0) {
                        float progress = (10 - secondsLeft) / 10.0f;
                        net.kyori.adventure.text.Component msg = com.resolutestudios.unstablesmp.utils.ProgressBar.create(progress)
                            .append(net.kyori.adventure.text.Component.text(" Finalizing... " + secondsLeft + "s"));
                        player.sendActionBar(msg);
                        secondsLeft--;
                    } else {
                        // Restore player after 10 seconds
                        plugin.restorePlayer(player);
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L); // Run every second (20 ticks)
            
        } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED ||
                   event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            // Immediately restore player if they decline or fail to download
            plugin.restorePlayer(player);
        }
    }
}
