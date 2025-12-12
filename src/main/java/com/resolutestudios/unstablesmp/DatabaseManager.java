package com.resolutestudios.unstablesmp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DatabaseManager {

    private final UnstableSMP plugin;
    private Connection connection;

    public DatabaseManager(UnstableSMP plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        File dataFolder = new File(plugin.getDataFolder(), "userdata.db");
        if (!dataFolder.getParentFile().exists()) {
            dataFolder.getParentFile().mkdirs();
        }

        // Check if database file already exists
        boolean databaseExists = dataFolder.exists();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder.getAbsolutePath());
            
            // Create player_data table
            try (PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_data (" +
                            "uuid TEXT PRIMARY KEY, " +
                            "world TEXT, " +
                            "x DOUBLE, " +
                            "y DOUBLE, " +
                            "z DOUBLE, " +
                            "yaw FLOAT, " +
                            "pitch FLOAT)")) {
                stmt.execute();
            }
            
            // Create disguises table
            try (PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS disguises (" +
                            "uuid TEXT PRIMARY KEY, " +
                            "disguise_name TEXT, " +
                            "skin_value TEXT, " +
                            "skin_signature TEXT)")) {
                stmt.execute();
            }
            
            if (databaseExists) {
                plugin.getLogger().log(Level.INFO, "Loaded existing database file at {0}", dataFolder.getAbsolutePath());
            } else {
                plugin.getLogger().log(Level.INFO, "Created new database file at {0}", dataFolder.getAbsolutePath());
            }
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not initialize database", e);
        }
    }

    public void savePlayerLocation(UUID uuid, Location loc) {
        String query = "INSERT OR REPLACE INTO player_data (uuid, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, loc.getWorld().getName());
            stmt.setDouble(3, loc.getX());
            stmt.setDouble(4, loc.getY());
            stmt.setDouble(5, loc.getZ());
            stmt.setFloat(6, loc.getYaw());
            stmt.setFloat(7, loc.getPitch());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player location", e);
        }
    }

    public Location getPlayerLocation(UUID uuid) {
        String query = "SELECT * FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    World world = Bukkit.getWorld(rs.getString("world"));
                    if (world == null)
                        return null;
                    return new Location(world,
                            rs.getDouble("x"),
                            rs.getDouble("y"),
                            rs.getDouble("z"),
                            rs.getFloat("yaw"),
                            rs.getFloat("pitch"));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not get player location", e);
        }
        return null;
    }

    // Disguise Management
    public void saveDisguise(UUID uuid, String disguiseName, String skinValue, String skinSignature) {
        String query = "INSERT OR REPLACE INTO disguises (uuid, disguise_name, skin_value, skin_signature) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, disguiseName);
            stmt.setString(3, skinValue);
            stmt.setString(4, skinSignature);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save disguise", e);
        }
    }

    public DisguiseData getDisguise(UUID uuid) {
        String query = "SELECT * FROM disguises WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DisguiseData(
                        rs.getString("disguise_name"),
                        rs.getString("skin_value"),
                        rs.getString("skin_signature")
                    );
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not get disguise", e);
        }
        return null;
    }

    public boolean hasDisguise(UUID uuid) {
        return getDisguise(uuid) != null;
    }

    public void removeDisguise(UUID uuid) {
        String query = "DELETE FROM disguises WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not remove disguise", e);
        }
    }

    public static class DisguiseData {
        public final String name;
        public final String skinValue;
        public final String skinSignature;

        public DisguiseData(String name, String skinValue, String skinSignature) {
            this.name = name;
            this.skinValue = skinValue;
            this.skinSignature = skinSignature;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not close database connection", e);
        }
    }
}
