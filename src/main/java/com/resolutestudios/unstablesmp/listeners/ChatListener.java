package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final UnstableSMP plugin;

    public ChatListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncChatEvent event) {
        // Prevent players in pending state from sending chat messages
        if (plugin.isPending(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
