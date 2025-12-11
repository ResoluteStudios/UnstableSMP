package com.resolutestudios.unstablesmp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.resolutestudios.unstablesmp.utils.ProgressBar;
import com.resolutestudios.unstablesmp.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    public Updater(UnstableSMP plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        checkForUpdates(null);
    }

    public void checkForUpdates(CommandSender validationSender) {
        plugin.getServer().getGlobalRegionScheduler().execute(plugin, () -> {
            try {
                // Simulate progress for UI
                updateProgressBar(0.1f, "Checking...");

                URL url = URI.create(REPO_URL).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "UnstableSMP-Plugin");

                if (conn.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    String latestVersion = json.get("tag_name").getAsString().replace("v", "");
                    String currentVersion = plugin.getDescription().getVersion();

                    if (isNewer(currentVersion, latestVersion)) {
                        updateProgressBar(0.4f, "Found version " + latestVersion);

                        String downloadUrl = json.get("assets").getAsJsonArray().get(0).getAsJsonObject()
                                .get("browser_download_url").getAsString();
                        downloadUpdate(downloadUrl, json.get("name").getAsString());

                        updateProgressBar(1.0f, "Update Downloaded! Restart to apply.");
                        if (validationSender != null)
                            validationSender
                                    .sendMessage(TextUtils.toSmallCaps("§aUpdate downloaded! Restart to apply."));
                    } else {
                        updateProgressBar(1.0f, "Plugin is up to date.");
                        if (validationSender != null)
                            validationSender.sendMessage(TextUtils.toSmallCaps("§aPlugin is up to date."));
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Could not check for updates.", e);
                if (validationSender != null)
                    validationSender.sendMessage("§cFailed to check updates.");
            }
        });
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
        return !current.equalsIgnoreCase(latest);
    }

    private void downloadUpdate(String urlStr, String fileName) {
        try {
            URL url = URI.create(urlStr).toURL();
            File updateFolder = new File(plugin.getServer().getUpdateFolderFile().getParentFile(), "update");
            if (!updateFolder.exists())
                updateFolder.mkdirs();

            File outputFile = new File(updateFolder, "UnstableSMP-v" + fileName + ".jar"); // Or just overwrite name
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
            // Logic handled by main loop
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Could not download update.", e);
        }
    }
}
