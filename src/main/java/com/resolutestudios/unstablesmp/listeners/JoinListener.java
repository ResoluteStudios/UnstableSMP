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
        
        // Apply saved disguise if exists
        com.resolutestudios.unstablesmp.DatabaseManager.DisguiseData disguise = 
            plugin.getDatabaseManager().getDisguise(player.getUniqueId());
        
        if (disguise != null) {
            // Apply disguise
            com.resolutestudios.unstablesmp.commands.DisguiseCommand disguiseCmd = 
                new com.resolutestudios.unstablesmp.commands.DisguiseCommand(plugin);
            disguiseCmd.applyDisguise(player, disguise.name, disguise.skinValue, disguise.skinSignature);
            
            // Customize join message
            if (event.joinMessage() != null) {
                String originalMsg = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                    .serialize(event.joinMessage());
                String customMsg = originalMsg.replace(player.getName(), disguise.name);
                event.joinMessage(net.kyori.adventure.text.Component.text(customMsg));
            }
        }
        
        // Restore fishing hook if player was casting
        plugin.getFishingListener().handlePlayerJoin(player);
        
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
