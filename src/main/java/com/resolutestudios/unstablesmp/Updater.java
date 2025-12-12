package com.resolutestudios.unstablesmp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.resolutestudios.unstablesmp.utils.ProgressBar;
import com.resolutestudios.unstablesmp.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

public class Updater {

    private final UnstableSMP plugin;
    private final String REPO_URL = "https://api.github.com/repos/ResoluteStudios/UnstableSMP/releases/latest";
    
    private static boolean updateAvailable = false;
    private static String latestVersionTag = "";

    public Updater(UnstableSMP plugin) {
        this.plugin = plugin;
    }
    
    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }
    
    public static String getLatestVersion() {
        return latestVersionTag;
    }

    public void startLoop() {
        // Run immediately on start
        checkForUpdates(null, true);
        
        // Schedule every 10 minutes (12000 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                checkForUpdates(null, false);
            }
        }.runTaskTimer(plugin, 12000L, 12000L);
    }

    public void checkForUpdates(CommandSender validationSender) {
        checkForUpdates(validationSender, false);
    }

    private void checkForUpdates(CommandSender validationSender, boolean isStartup) {
        plugin.getServer().getGlobalRegionScheduler().execute(plugin, () -> {
            try {
                if (validationSender != null) {
                    // Simulate progress for UI if manual check
                    updateProgressBar(0.1f, "Checking...");
                }

                URL url = URI.create(REPO_URL).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "UnstableSMP-Plugin");

                if (conn.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    String latestVersion = json.get("tag_name").getAsString().replace("v", "");
                    String currentVersion = plugin.getDescription().getVersion();
                    
                    latestVersionTag = latestVersion;

                    if (isNewer(currentVersion, latestVersion)) {
                        updateAvailable = true;
                        
                        if (validationSender != null) updateProgressBar(0.4f, "Found version " + latestVersion);

                        // Always auto-download if configured
                        if (plugin.getConfig().getBoolean("auto-update", true)) {
                            String downloadUrl = json.get("assets").getAsJsonArray().get(0).getAsJsonObject()
                                    .get("browser_download_url").getAsString();
                            
                            // Show progress to opped players during download
                            updateProgressBar(0.5f, "Downloading update...");
                            
                            downloadUpdate(downloadUrl, json.get("name").getAsString());
                            
                            updateProgressBar(1.0f, "Update Downloaded!");
                            
                            // Only log to console AFTER download completes
                            plugin.log("§aUpdate v" + latestVersion + " has been downloaded! Restart to apply.");
                            
                            // Notify opped players
                            notifyAdmins("§aUpdate v" + latestVersion + " downloaded! Restart to apply.");
                            
                            if (validationSender != null) {
                                validationSender.sendMessage(TextUtils.toSmallCaps("§aUpdate downloaded! Restart to apply."));
                            }
                        } else {
                            // If auto-update is disabled, still notify about availability
                            plugin.log("§aUpdate v" + latestVersion + " is available!");
                            
                            if (plugin.getConfig().getBoolean("notifications.autoupdate", true)) {
                                notifyAdmins("§aA new update (v" + latestVersion + ") is available!");
                            }
                        }
                        
                        if (validationSender != null && !plugin.getConfig().getBoolean("auto-update", true)) {
                            validationSender.sendMessage(TextUtils.toSmallCaps("§aUpdate available: v" + latestVersion));
                        }

                    } else {
                        updateAvailable = false;
                        // Only send feedback if this is a manual check
                        if (validationSender != null) {
                            updateProgressBar(1.0f, "Plugin is up to date.");
                            validationSender.sendMessage(TextUtils.toSmallCaps("§aPlugin is up to date."));
                        }
                        // No console logging for silent checks when up to date
                    }
                }
            } catch (Exception e) {
                // Only log errors if this is a manual check or startup
                if (validationSender != null || isStartup) {
                    plugin.getLogger().log(Level.WARNING, "Could not check for updates.", e);
                }
                if (validationSender != null)
                    validationSender.sendMessage("§cFailed to check updates.");
            }
        });
    }
    
    private void notifyAdmins(String message) {
        String prefix = plugin.getPrefix();
        Component comp = Component.text(TextUtils.toSmallCaps(prefix + message));
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("unstablesmp.admin")) {
                p.sendMessage(comp);
            }
        }
    }

    private void updateProgressBar(float progress, String status) {
        final Component msg = ProgressBar.create(progress).append(Component.text(" " + status));

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendActionBar(msg);
            }
        }
    }

    private boolean isNewer(String current, String latest) {
        // Simple string comparison might fail for complex versions, but sufficient for standard x.y.z
        // Better to use Split(".") and compare integers
        return !current.equalsIgnoreCase(latest);
    }

    private void downloadUpdate(String urlStr, String fileName) {
        try {
            URL url = URI.create(urlStr).toURL();
            File updateFolder = new File(plugin.getServer().getUpdateFolderFile().getParentFile(), "update");
            if (!updateFolder.exists())
                updateFolder.mkdirs();

            File outputFile = new File(updateFolder, "UnstableSMP-v" + fileName + ".jar");
            if (!outputFile.getName().endsWith(".jar"))
                outputFile = new File(updateFolder, "UnstableSMP.jar");

            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Could not download update.", e);
        }
    }
}
