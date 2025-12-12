package com.resolutestudios.unstablesmp.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TabListUtils {

    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    /**
     * Hides a player from the tab list for all online players
     */
    public static void hideFromTabList(Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        PlayerInfoData data = new PlayerInfoData(
            profile,
            0,
            EnumWrappers.NativeGameMode.SPECTATOR,
            WrappedChatComponent.fromText(player.getName())
        );
        
        List<PlayerInfoData> dataList = Collections.singletonList(data);
        packet.getPlayerInfoDataLists().write(0, dataList);
        
        // Send to all online players
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(onlinePlayer, packet);
            } catch (Exception e) {
                // Ignore errors
            }
        }
    }

    /**
     * Shows a player in the tab list for all online players
     */
    public static void showInTabList(Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        PlayerInfoData data = new PlayerInfoData(
            profile,
            player.getPing(),
            EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
            WrappedChatComponent.fromText(player.getDisplayName())
        );
        
        List<PlayerInfoData> dataList = Collections.singletonList(data);
        packet.getPlayerInfoDataLists().write(0, dataList);
        
        // Send to all online players
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(onlinePlayer, packet);
            } catch (Exception e) {
                // Ignore errors
            }
        }
    }
}
