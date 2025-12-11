package com.resolutestudios.unstablesmp;

import com.resolutestudios.unstablesmp.listeners.DeathListener;
import com.resolutestudios.unstablesmp.listeners.JoinListener;
import com.resolutestudios.unstablesmp.listeners.QuitListener;
import com.resolutestudios.unstablesmp.listeners.ResourcePackListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UnstableSMP extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.databaseManager = new DatabaseManager(this);

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ResourcePackListener(this), this);
        getServer().getPluginManager()
                .registerEvents(new com.resolutestudios.unstablesmp.listeners.ItemRestrictionListener(this), this);
        getServer().getPluginManager()
                .registerEvents(new com.resolutestudios.unstablesmp.listeners.CombatListener(this), this);

        getCommand("unstable").setExecutor(new com.resolutestudios.unstablesmp.commands.UnstableCommand(this));
        getCommand("disguise").setExecutor(new com.resolutestudios.unstablesmp.commands.DisguiseCommand(this));

        // Start Adaptive Render Distance
        new com.resolutestudios.unstablesmp.systems.AdaptiveRenderDistance(this).runTaskTimer(this, 100L, 600L); // Start after 5s, run every 30s

        getLogger().info("UnstableSMP enabled!");

        // Auto-updater placeholder logic would go here
        if (getConfig().getBoolean("auto-update", true)) {
            try {
                new Updater(this).checkForUpdates();
            } catch (Exception e) {
                getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        }
    }

    public String getPrefix() {
        return getConfig().getString("prefix", ""); // Default handled in config
    }

    @Override
    public void onDisable() {
        // Save all online players' locations
        for (Player p : getServer().getOnlinePlayers()) {
            // Only save if NOT pending (not in waiting room)
            if (!isPending(p.getUniqueId())) {
                databaseManager.savePlayerLocation(p.getUniqueId(), p.getLocation());
            }
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("UnstableSMP disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    private final java.util.Map<java.util.UUID, net.kyori.adventure.text.Component> pendingJoinMessages = new java.util.concurrent.ConcurrentHashMap<>();

    public boolean isPending(java.util.UUID uuid) {
        return pendingJoinMessages.containsKey(uuid);
    }

    public void addPendingJoinMessage(java.util.UUID uuid, net.kyori.adventure.text.Component message) {
        pendingJoinMessages.put(uuid, message);
    }

    public net.kyori.adventure.text.Component getAndRemovePendingJoinMessage(java.util.UUID uuid) {
        return pendingJoinMessages.remove(uuid);
    }

    public void restorePlayer(Player player) {
        org.bukkit.Location originalLoc = getDatabaseManager().getPlayerLocation(player.getUniqueId());

        java.util.function.Consumer<Boolean> onComplete = (val) -> {
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);

            // Show player to others
            for (Player p : getServer().getOnlinePlayers()) {
                p.showPlayer(this, player);
            }

            // Broadcast join message
            net.kyori.adventure.text.Component msg = getAndRemovePendingJoinMessage(player.getUniqueId());
            if (msg != null) {
                getServer().sendMessage(msg);
            }
        };

        if (originalLoc != null) {
            player.teleportAsync(originalLoc).thenAccept(onComplete);
        } else {
            player.teleportAsync(player.getWorld().getSpawnLocation()).thenAccept(onComplete);
        }
    }
}
