package com.resolutestudios.unstablesmp.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.resolutestudios.unstablesmp.UnstableSMP;
import com.resolutestudios.unstablesmp.Updater;
import com.resolutestudios.unstablesmp.utils.TextUtils;

public class UnstableCommand implements CommandExecutor, TabCompleter {

    private final UnstableSMP plugin;

    public UnstableCommand(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!sender.hasPermission("unstablesmp.admin")) {
            send(sender, "§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
            send(sender, "§aChecking for updates...");
            new Updater(plugin).checkForUpdates(sender); // Pass sender for feedback
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("skiprp")) {
            org.bukkit.entity.Player target = org.bukkit.Bukkit.getPlayer(args[1]);
            if (target == null) {
                send(sender, "§cPlayer not found.");
                return true;
            }
            plugin.restorePlayer(target);
            send(sender, "§aSkipped Resource Pack check for " + target.getName());
            target.sendMessage(TextUtils.toSmallCaps("§aResource pack skipped by admin."));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            send(sender, "§aVersion: v" + plugin.getDescription().getVersion());
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("notifications")) {
            if (args.length == 3 && args[1].equalsIgnoreCase("autoupdate")) {
                String val = args[2].toLowerCase();
                boolean boolVal = val.equals("true");
                plugin.getConfig().set("notifications.autoupdate", boolVal);
                plugin.saveConfig();
                send(sender, "§aSet Auto-Update Notifications to " + boolVal);
                return true;
            }
        }

        if (args.length < 2) {
            send(sender, "§cUsage: /unstable <feature> <value>");
            return true;
        }

        String feature = args[0].toLowerCase();
        String valueStr = args[1].toLowerCase();
        boolean value;

        if (valueStr.equals("true")) {
            value = true;
        } else if (valueStr.equals("false")) {
            value = false;
        } else {
            send(sender, "§cValue must be true or false.");
            return true;
        }

        if (isValidFeature(feature)) {
            plugin.getConfig().set("features." + feature, value);
            plugin.saveConfig();
            send(sender, "§aSet " + feature + " to " + value);
        } else {
            send(sender, "§cUnknown feature: " + feature);
        }

        return true;
    }

    private void send(CommandSender sender, String message) {
        String prefix = plugin.getPrefix();
        sender.sendMessage(TextUtils.toSmallCaps(prefix + message));
    }

    private boolean isValidFeature(String feature) {
        return Arrays.asList("deathkick", "netheriteban", "maceban", "macenerf").contains(feature);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            org.bukkit.util.StringUtil.copyPartialMatches(args[0], Arrays.asList("deathkick", "netheriteban", "maceban", "macenerf", "update", "skiprp"), completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("skiprp")) {
                 return null; // Return null to let Bukkit suggest player names
            } else if (!args[0].equalsIgnoreCase("update")) {
                org.bukkit.util.StringUtil.copyPartialMatches(args[1], Arrays.asList("true", "false"), completions);
            }
        }
        return completions;
    }
}
