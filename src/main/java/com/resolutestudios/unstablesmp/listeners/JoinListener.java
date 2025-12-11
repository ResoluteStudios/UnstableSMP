package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

        // Hide join message
        if (event.joinMessage() != null) {
            plugin.addPendingJoinMessage(player.getUniqueId(), event.joinMessage());
            event.joinMessage(null);
        }

        // Hide player from others
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }

        Location voidLoc = new Location(player.getWorld(), 0, 1000000, 0);
        player.teleportAsync(voidLoc).thenAccept(success -> {
            if (success) {
                player.setGameMode(GameMode.SPECTATOR);

                String url = plugin.getConfig().getString("resource-pack.url");
                String hash = plugin.getConfig().getString("resource-pack.sha1");
                String promptObj = plugin.getConfig().getString("resource-pack.prompt");

                String smallCapsPrompt = com.resolutestudios.unstablesmp.utils.TextUtils.toSmallCaps(promptObj);

                // Set required to false (optional), but we keep them in void until decision
                player.setResourcePack(url, hash, false, Component.text(smallCapsPrompt));
            }
        });
    }
}
