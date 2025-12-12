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
        // Save fishing state if player is casting
        plugin.getFishingListener().handlePlayerQuit(event.getPlayer());
        
        // Save player location to database on quit
        plugin.getDatabaseManager().savePlayerLocation(event.getPlayer().getUniqueId(),
                event.getPlayer().getLocation());
        
        // Customize quit message if disguised
        com.resolutestudios.unstablesmp.DatabaseManager.DisguiseData disguise = 
            plugin.getDatabaseManager().getDisguise(event.getPlayer().getUniqueId());
        
        if (disguise != null && event.quitMessage() != null) {
            String originalMsg = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(event.quitMessage());
            String customMsg = originalMsg.replace(event.getPlayer().getName(), disguise.name);
            event.quitMessage(net.kyori.adventure.text.Component.text(customMsg));
        }
    }
}
