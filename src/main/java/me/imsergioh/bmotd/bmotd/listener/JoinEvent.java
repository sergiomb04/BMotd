package me.imsergioh.bmotd.bmotd.listener;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.net.InetSocketAddress;

public class JoinEvent implements Listener {

    private final BMotd plugin = BMotd.getPlugin();

    @EventHandler
    public void onHandshake(PlayerHandshakeEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();
        String domain;
        try {
            domain = event.getConnection().getVirtualHost().getHostName().toLowerCase();
        } catch (Exception e) {
            event.getConnection().disconnect(TextComponent.fromLegacyText("Unknown host"));
            return;
        }

        if (motdManager.isNotAllowedDomain(domain)) {
            event.getConnection().disconnect(TextComponent.fromLegacyText("Unknown host"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(ServerConnectedEvent event){
        performCheck(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent1(ServerConnectEvent event) {
        performCheck(event.getPlayer());
    }

    private void performCheck(ProxiedPlayer player) {
        MOTDManager motdManager = plugin.getMotdManager();

        // Return si player is whitelisted
        if (motdManager.hasWhitelist(player.getName())) return;

        InetSocketAddress virtualHost = player.getPendingConnection().getVirtualHost();
        if (virtualHost != null) {
            String domain = virtualHost.getHostName().toLowerCase();
            if (motdManager.isWhitelistActive(domain)) {
                player.disconnect(TextComponent.fromLegacyText(ChatUtil.chatColor(motdManager.getProfile(domain).getNotWhitelistedError())));
            }
        } else {
            if (motdManager.isWhitelistActive(null)) {
                player.disconnect(TextComponent.fromLegacyText(ChatUtil.chatColor(motdManager.getDefaultProfile().getNotWhitelistedError())));
            }
        }
    }

}
