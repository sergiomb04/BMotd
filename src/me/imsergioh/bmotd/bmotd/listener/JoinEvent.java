package me.imsergioh.bmotd.bmotd.listener;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class JoinEvent implements Listener {

    private BMotd plugin = BMotd.getPlugin();

    @EventHandler
    public void onHandshake(PlayerHandshakeEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();
        String domain = null;
        try {
            domain = event.getConnection().getVirtualHost().getHostName().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            event.getConnection().disconnect(TextComponent.fromLegacyText("Unknown host"));
            return;
        }

        if (!motdManager.isAllowedDomain(domain)) {
            event.getConnection().disconnect(TextComponent.fromLegacyText("Unknown host"));
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(ServerConnectedEvent event){
        MOTDManager motdManager = plugin.getMotdManager();

        if(!motdManager.hasWhitelist(event.getPlayer().getName())){
            event.getPlayer().disconnect(TextComponent.fromLegacyText(ChatUtil.chatColor(motdManager.getConfig().getString("messages.not-whitelist")).replace("\\n", "\n")));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent1(ServerConnectEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();

        if (!motdManager.hasWhitelist(event.getPlayer().getName())) {
            event.getPlayer().disconnect(TextComponent.fromLegacyText(ChatUtil.chatColor(motdManager.getConfig().getString("messages.not-whitelist")).replace("\\n", "\n")));
        }
    }

}
