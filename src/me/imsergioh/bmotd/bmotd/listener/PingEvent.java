package me.imsergioh.bmotd.bmotd.listener;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingEvent implements Listener {

    private final BMotd plugin = BMotd.getPlugin();

    @EventHandler
    public void onEvent(ProxyPingEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();

        PendingConnection connection = event.getConnection();
        String domain = null;
        try {
            domain = connection.getVirtualHost().getHostName().toLowerCase();
        } catch (Exception e) {
            cancel(event);
            return;
        }

        if (motdManager.isNotAllowedDomain(domain)) {
            cancel(event);
            return;
        }

        ServerPing serverPing = event.getResponse();
        event.setResponse(serverPing);

        if (plugin.getMotdManager().getConfig().getBoolean("config.whitelist-enabled")) {
            serverPing.setVersion(new net.md_5.bungee.api.ServerPing.Protocol(motdManager.getWhitelistText(domain), 4));
        }

        if (plugin.getMotdUtil().getMOTDFromDomain(domain.replace(".", "-")) != null) {
            serverPing.setDescriptionComponent(plugin.getMotdUtil().getMOTDFromDomain(domain.replace(".", "-")));
        } else {
            serverPing.setDescriptionComponent(motdManager.getDescriptionComponent());
        }

        serverPing.setFavicon(motdManager.getFavicon(domain));

        serverPing.setPlayers(motdManager.getPlayers());
        event.setResponse(serverPing);

        System.out.println("[BMOTD] " + event.getConnection().getAddress() + " pinged with domain -> " + domain);
    }

    private void cancel(ProxyPingEvent event) {
        event.setResponse(null);
        event.getConnection().disconnect();
    }

}
