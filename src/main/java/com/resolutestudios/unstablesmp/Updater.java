package com.resolutestudios.unstablesmp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        // [█████████████████░░░] - [XX%]
        String filledChar = "█";
        String emptyChar = "░";
        int totalBars = 20;
        int filledBars = (int) (progress * totalBars);
        
        StringBuilder barBuilder = new StringBuilder();
        barBuilder.append("§8[§a");
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars)
                barBuilder.append(filledChar);
            else
                barBuilder.append("§8").append(emptyChar); // Gray for empty
        }
        barBuilder.append("§8] - ");

        int percent = (int)(progress * 100);
        String percentStr = percent + "%";
        String gradientPercent = TextUtils.gradient(percentStr, 
                new java.awt.Color(255, 85, 85), // Red 
                new java.awt.Color(85, 255, 85), // Green
                progress); // Using progress as interpolation factor? 
                           // Request says: "§(gradiant-red to green)[percent complete]"
                           // So we want the TEXT "XX%" to be a gradient from Red to Green based on progress?
                           // Or the text ITSELF has a gradient across characters?
                           // "percent complete" implies the string "XX%".
                           
        // Let's implement a color interpolation based on % completion.
        // 0% = Red, 50% = Yellow, 100% = Green.
        String colorCode = getInterpolatedColor(progress);
        
        // Actually user said: "§(gradiant-red to green)[percent complete]"
        // Usually means the text color shifts from red to green depending on the value, OR the text has a multicolor gradient.
        // Given "percent complete", I'll interpolate the single color for the whole text based on the value.
        // It's cleaner for short text like "45%".
        
        String finalMsg = barBuilder.toString() + colorCode + percentStr + " " + "§7" + status;
        
        final Component msg = Component.text(TextUtils.toSmallCaps(finalMsg));

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendActionBar(msg);
            }
        }
    }
    
    private String getInterpolatedColor(float progress) {
        int r, g, b;
        if (progress < 0.5) {
             // Red to Yellow
             r = 255;
             g = (int) (255 * (progress * 2));
             b = 85;
        } else {
             // Yellow to Green
             r = (int) (255 * (1 - (progress - 0.5) * 2));
             g = 255;
             b = 85; 
        }
        // Format as Bungee Hex: §x§R§R§G§G§B§B
        // Using concise hex format for Spigot: §x§r§r§g§g§b§b
        return String.format("§x§%x§%x§%x§%x§%x§%x", 
            (r >> 4) & 0xF, r & 0xF, 
            (g >> 4) & 0xF, g & 0xF,
            (b >> 4) & 0xF, b & 0xF);
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

            File outputFile = new File(updateFolder, "UnstableSMP-v" + fileName + ".jar");
            if (!outputFile.getName().endsWith(".jar"))
                outputFile = new File(updateFolder, "UnstableSMP.jar");

            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                long totalBytes = connContentLength(url); // Optional
                long readBytes = 0;
                
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    readBytes += bytesRead;
                    // Optional: update progress bar during download? 
                    // Might be too spamy for ActionBar, main loop handles stages 0.1 -> 0.4 -> 1.0.
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Could not download update.", e);
        }
    }
    
    // Helper to avoid altering signature too much
    private long connContentLength(URL url) {
        try {
            return url.openConnection().getContentLengthLong();
        } catch(Exception e) { return -1; }
    }
}
