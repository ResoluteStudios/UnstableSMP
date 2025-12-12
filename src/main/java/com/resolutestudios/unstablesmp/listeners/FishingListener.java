package com.resolutestudios.unstablesmp.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.resolutestudios.unstablesmp.DatabaseManager;
import com.resolutestudios.unstablesmp.UnstableSMP;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingListener implements Listener {

    private final UnstableSMP plugin;
    private final Map<UUID, FishingState> activeCasts = new HashMap<>();

    public FishingListener(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    // Track fishing state when player casts
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        switch (event.getState()) {
            case FISHING:
                // Player cast the line
                FishHook hook = event.getHook();
                if (hook != null) {
                    FishingState state = new FishingState(
                        hook.getLocation().clone(),
                        hook.getVelocity().clone(),
                        hook
                    );
                    activeCasts.put(uuid, state);
                    // Save to database
                    plugin.getDatabaseManager().saveFishingState(uuid, hook.getLocation(), hook.getVelocity());
                }
                break;

            case REEL_IN:
            case CAUGHT_ENTITY:
            case CAUGHT_FISH:
            case IN_GROUND:
            case FAILED_ATTEMPT:
            case BITE:
                // Player reeled in or hook is done - clear state
                activeCasts.remove(uuid);
                plugin.getDatabaseManager().removeFishingState(uuid);
                break;
        }
    }

    // Save hook state before teleport
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        saveFishingStateBeforeTeleport(player);
    }

    // Save hook state before portal
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        saveFishingStateBeforeTeleport(player);
    }

    private void saveFishingStateBeforeTeleport(Player player) {
        UUID uuid = player.getUniqueId();
        FishingState state = activeCasts.get(uuid);
        
        if (state != null && state.hook != null && state.hook.isValid()) {
            // Update saved location to current hook position
            state.location = state.hook.getLocation().clone();
            state.velocity = state.hook.getVelocity().clone();
            
            // Save to database
            plugin.getDatabaseManager().saveFishingState(uuid, state.location, state.velocity);
            
            // Schedule hook restoration after teleport
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                restoreHook(player, state.location, state.velocity);
            }, 5L); // 5 ticks delay to ensure teleport is complete
        }
    }

    // Restore hook for player
    public void restoreHook(Player player, Location hookLoc, Vector velocity) {
        if (!player.isOnline()) return;
        
        // Set world from player if not set
        if (hookLoc.getWorld() == null) {
            hookLoc.setWorld(player.getWorld());
        }
        
        // Launch new fishing hook
        FishHook newHook = player.launchProjectile(FishHook.class);
        
        // Teleport to saved location
        newHook.teleport(hookLoc);
        
        // Apply saved velocity
        if (velocity != null) {
            newHook.setVelocity(velocity);
        }
        
        // Update active cast tracking
        FishingState newState = new FishingState(hookLoc, velocity, newHook);
        activeCasts.put(player.getUniqueId(), newState);
        
        // Force client sync using ProtocolLib
        syncHookWithClient(player, newHook);
    }

    // Send packet to force client to see the hook
    private void syncHookWithClient(Player player, FishHook hook) {
        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            
            // Send spawn entity packet for the fishing hook
            PacketContainer spawnPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
            
            // Set entity ID
            spawnPacket.getIntegers().write(0, hook.getEntityId());
            
            // Set entity UUID
            spawnPacket.getUUIDs().write(0, hook.getUniqueId());
            
            // Set position
            spawnPacket.getDoubles().write(0, hook.getLocation().getX());
            spawnPacket.getDoubles().write(1, hook.getLocation().getY());
            spawnPacket.getDoubles().write(2, hook.getLocation().getZ());
            
            // Send to player
            protocolManager.sendServerPacket(player, spawnPacket);
            
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to sync fishing hook with client: " + e.getMessage());
        }
    }

    // Called when player logs out - save state if casting
    public void handlePlayerQuit(Player player) {
        UUID uuid = player.getUniqueId();
        FishingState state = activeCasts.get(uuid);
        
        if (state != null && state.hook != null && state.hook.isValid()) {
            // Save current hook location
            plugin.getDatabaseManager().saveFishingState(uuid, state.hook.getLocation(), state.hook.getVelocity());
        }
        
        // Clear from memory
        activeCasts.remove(uuid);
    }

    // Called when player joins - restore hook if was casting
    public void handlePlayerJoin(Player player) {
        UUID uuid = player.getUniqueId();
        
        // Check database for saved fishing state
        DatabaseManager.FishingStateData fishingState = plugin.getDatabaseManager().getFishingState(uuid);
        
        if (fishingState != null) {
            // Restore hook after a short delay
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    Location hookLoc = fishingState.hookLocation.clone();
                    hookLoc.setWorld(player.getWorld());
                    restoreHook(player, hookLoc, fishingState.velocity);
                }
            }, 20L); // 1 second delay after join
        }
    }

    // Check if player is currently casting
    public boolean isCasting(UUID uuid) {
        return activeCasts.containsKey(uuid);
    }

    // Clear fishing state
    public void clearFishingState(UUID uuid) {
        activeCasts.remove(uuid);
        plugin.getDatabaseManager().removeFishingState(uuid);
    }

    // Inner class to track fishing state in memory
    private static class FishingState {
        Location location;
        Vector velocity;
        FishHook hook;

        FishingState(Location location, Vector velocity, FishHook hook) {
            this.location = location;
            this.velocity = velocity;
            this.hook = hook;
        }
    }
}
