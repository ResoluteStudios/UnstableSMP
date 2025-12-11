package com.resolutestudios.unstablesmp.commands;

import com.resolutestudios.unstablesmp.UnstableSMP;
import com.resolutestudios.unstablesmp.utils.SkinUtils;
import com.resolutestudios.unstablesmp.utils.TextUtils;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DisguiseCommand implements CommandExecutor {

    private final UnstableSMP plugin;

    public DisguiseCommand(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (!sender.hasPermission("unstablesmp.admin")) {
            sender.sendMessage(TextUtils.toSmallCaps("§cYou do not have permission."));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(TextUtils.toSmallCaps("§cUsage: /disguise <skinName> [targetPlayer]"));
            return true;
        }

        String skinName = args[0];
        Player targetPlayer;

        if (args.length >= 2) {
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage(TextUtils.toSmallCaps("§cPlayer not found."));
                return true;
            }
        } else if (sender instanceof Player) {
            targetPlayer = (Player) sender;
        } else {
            sender.sendMessage("§cConsole must specify a target.");
            return true;
        }

        sender.sendMessage(TextUtils.toSmallCaps("§aFetching skin for " + skinName + "..."));

        // Run async
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                SkinUtils.SkinData skin = SkinUtils.getSkinFromMojang(skinName);
                
                // Back to main thread
                plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (skin == null) {
                            sender.sendMessage(TextUtils.toSmallCaps("§cFailed to fetch skin (Invalid name?)."));
                            return;
                        }

                        // Apply Skin using Paper API
                        PlayerProfile profile = targetPlayer.getPlayerProfile();
                        profile.removeProperty("textures"); // Clean old
                        profile.setProperty(new ProfileProperty("textures", skin.value, skin.signature));
                        targetPlayer.setPlayerProfile(profile);

                        // Refresh Player (Hide/Show)
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.hidePlayer(plugin, targetPlayer);
                            p.showPlayer(plugin, targetPlayer);
                        }
                        
                        // Change only the display name (used in chat)
                        // The nameplate (above head) will show the actual username
                        Component nameComponent = Component.text(skinName);
                        
                        try {
                            targetPlayer.displayName(nameComponent);
                        } catch (Throwable ignored) {
                        }
                        
                        // Feedback
                        sender.sendMessage(TextUtils.toSmallCaps("§aDisguised " + targetPlayer.getName() + " as " + skinName));
                    }
                });
            }
        });

        return true;
    }
}
