package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final UnstableSMP plugin;

    public JoinListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Update Notification
        if (player.hasPermission("unstablesmp.admin") && plugin.getConfig().getBoolean("notifications.autoupdate", true)) {
            if (com.resolutestudios.unstablesmp.Updater.isUpdateAvailable()) {
                String ver = com.resolutestudios.unstablesmp.Updater.getLatestVersion();
                String prefix = plugin.getPrefix();
                player.sendMessage(com.resolutestudios.unstablesmp.utils.TextUtils.toSmallCaps(prefix + "§aUpdate available: v" + ver));
            }
        }
    }
}
