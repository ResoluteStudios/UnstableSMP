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

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED ||
                event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED ||
                event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED ||
                event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {

            // "If the player autodeclines the resorcepack, it just puts them direcly at
            // their logout location"
            // So for ANY terminal status (Accepted, Declined, Failed), we restore them.
            // Note: ACCEPTED should technically wait for LOADED, but if client doesn't send
            // LOADED,
            // we might want a timeout or manual skip. Standard flow is Accepted -> Loaded.
            // Use skiprp if stuck.

            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
                // Wait for successfully loaded.
                return;
            }

            plugin.restorePlayer(player);
        }
    }
}
