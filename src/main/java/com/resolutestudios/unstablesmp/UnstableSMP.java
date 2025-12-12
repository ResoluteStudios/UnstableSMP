package com.resolutestudios.unstablesmp;

import com.resolutestudios.unstablesmp.listeners.DeathListener;
import com.resolutestudios.unstablesmp.listeners.FishingListener;
import com.resolutestudios.unstablesmp.listeners.JoinListener;
import com.resolutestudios.unstablesmp.listeners.QuitListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UnstableSMP extends JavaPlugin {

    private DatabaseManager databaseManager;
    private FishingListener fishingListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        // Setup global exception handler to hide plugin code from stack traces
        com.resolutestudios.unstablesmp.utils.ExceptionHandler.setupGlobalHandler(
            getLogger(), 
            "com.resolutestudios.unstablesmp"
        );

        this.databaseManager = new DatabaseManager(this);
        this.fishingListener = new FishingListener(this);

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(fishingListener, this);
        getServer().getPluginManager()
                .registerEvents(new com.resolutestudios.unstablesmp.listeners.ItemRestrictionListener(this), this);
        getServer().getPluginManager()
                .registerEvents(new com.resolutestudios.unstablesmp.listeners.CombatListener(this), this);

        getCommand("unstable").setExecutor(new com.resolutestudios.unstablesmp.commands.UnstableCommand(this));
        getCommand("disguise").setExecutor(new com.resolutestudios.unstablesmp.commands.DisguiseCommand(this));

        // Start Adaptive Render Distance
        new com.resolutestudios.unstablesmp.systems.AdaptiveRenderDistance(this).runTaskTimer(this, 100L, 600L); // Start after 5s, run every 30s

        log("§aUnstableSMP enabled!");

        // Auto-updater loop
        if (getConfig().getBoolean("auto-update", true)) {
            try {
                new Updater(this).startLoop();
            } catch (Exception e) {
                getLogger().warning("Failed to start updater: " + e.getMessage());
            }
        }
    }
    
    public void log(String message) {
        String p = getPrefix();
        getServer().getConsoleSender().sendMessage(com.resolutestudios.unstablesmp.utils.TextUtils.toSmallCaps(p + message));
    }

    public String getPrefix() {
        return getConfig().getString("prefix", ""); // Default handled in config
    }

    @Override
    public void onDisable() {
        // Save all online players' locations
        for (Player p : getServer().getOnlinePlayers()) {
            databaseManager.savePlayerLocation(p.getUniqueId(), p.getLocation());
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        log("§cUnstableSMP disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public FishingListener getFishingListener() {
        return fishingListener;
    }
}
