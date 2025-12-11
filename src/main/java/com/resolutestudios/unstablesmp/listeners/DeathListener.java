package com.resolutestudios.unstablesmp.listeners;

import com.resolutestudios.unstablesmp.UnstableSMP;
import com.resolutestudios.unstablesmp.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final UnstableSMP plugin;

    public DeathListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String dhMessage = plugin.getConfig().getString("death-message", "You died!");

        // Check if deathkick is enabled (renamed from deathban)
        if (!plugin.getConfig().getBoolean("features.deathkick", true)) {
            return;
        }

        // Play sound to players in 48 block radius
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.8f, 0.0f);

        // Check for bypass permission
        boolean hasBypass = player.hasPermission("unstablesmp.bypass");
        boolean isPvpDeath = player.getKiller() != null;

        if (hasBypass && !isPvpDeath) {
            return;
        }

        // Convert message to small caps just in case
        String finalMessage = TextUtils.toSmallCaps(dhMessage);
        Component reason = Component.text(finalMessage);

        // Kick the player
        player.kick(reason);

        // Ban the player
        player.ban(finalMessage, (java.util.Date) null, "UnstableSMP");
    }
}
