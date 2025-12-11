package com.resolutestudios.unstablesmp.systems;

import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AdaptiveRenderDistance extends BukkitRunnable {

    private final UnstableSMP plugin;

    public AdaptiveRenderDistance(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getConfig().getBoolean("render-distance.enabled", false)) return;

        int minRD = plugin.getConfig().getInt("render-distance.min", 8);
        int maxRD = plugin.getConfig().getInt("render-distance.max", 18);
        int maxPlayers = plugin.getConfig().getInt("render-distance.player-max-threshold", 100);
        
        int currentPlayers = Bukkit.getOnlinePlayers().size();
        
        // Get TPS (Last 1m via server method usually returns array [1m, 5m, 15m])
        // If Paper API, Bukkit.getTPS() is accurate.
        double currentTps = 20.0;
        try {
             double[] tps = Bukkit.getTPS();
             if (tps != null && tps.length > 0) currentTps = tps[0];
        } catch (Exception e) {
            // Fallback if method missing in specific spigot version (rare for 1.21)
        }
        
        // RD = max(RD_min, min(RD_max, RD_max * ((P_max - PD) / P_max) * (TPS / 20)))
        // Note: The formula provided by user:
        // RD_max * ((P_max - PD) / P_max) * (TPS / 20)
        
        // Calculate factor
        double playerFactor = (double) (maxPlayers - currentPlayers) / maxPlayers;
        if (playerFactor < 0) playerFactor = 0; // Don't go negative if count exceeds max
        
        double tpsFactor = currentTps / 20.0;
        
        double calculated = maxRD * playerFactor * tpsFactor;
        
        // Clamp
        int finalRD = (int) Math.max(minRD, Math.min(maxRD, calculated));
        
        // Apply
        for (World world : Bukkit.getWorlds()) {
            if (world.getViewDistance() != finalRD) {
                world.setViewDistance(finalRD);
                // Also set Simulation distance if needed/possible, but request specifically mentioned Render Distance
                // world.setSimulationDistance(finalRD); 
            }
        }
    }
}
