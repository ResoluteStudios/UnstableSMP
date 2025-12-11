package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final UnstableSMP plugin;

    public QuitListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Save player location to database on quit ONLY if not pending
        if (!plugin.isPending(event.getPlayer().getUniqueId())) {
            plugin.getDatabaseManager().savePlayerLocation(event.getPlayer().getUniqueId(),
                    event.getPlayer().getLocation());
        }

        // Cleanup pending message if they quit while waiting
        plugin.getAndRemovePendingJoinMessage(event.getPlayer().getUniqueId());
    }
}
