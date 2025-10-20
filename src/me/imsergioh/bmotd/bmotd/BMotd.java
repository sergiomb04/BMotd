package me.imsergioh.bmotd.bmotd;

import me.imsergioh.bmotd.bmotd.commands.bmotdCMD;
import me.imsergioh.bmotd.bmotd.commands.bwhitelistCMD;
import me.imsergioh.bmotd.bmotd.commands.maintenanceCMD;
import me.imsergioh.bmotd.bmotd.listener.JoinEvent;
import me.imsergioh.bmotd.bmotd.listener.PingEvent;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.manager.MOTDUtil;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public final class BMotd extends Plugin {

    private static BMotd plugin;

    private MOTDManager motdManager;
    private MOTDUtil motdUtil;

    @Override
    public void onEnable() {
        plugin = this;
        motdManager = new MOTDManager();
        motdUtil = new MOTDUtil();

        this.getProxy().getPluginManager().registerCommand(plugin, new bmotdCMD("bmotd"));
        getProxy().getPluginManager().registerCommand(plugin, new maintenanceCMD("maintenance"));
        getProxy().getPluginManager().registerCommand(plugin, new bwhitelistCMD("bwhitelist"));

        regListener(new PingEvent());
        regListener(new JoinEvent());
    }

    public MOTDUtil getMotdUtil() {
        return motdUtil;
    }

    public void reloadBMotd(){
        motdManager = new MOTDManager();
        motdUtil = new MOTDUtil();
    }

    public MOTDManager getMotdManager() {
        return motdManager;
    }

    public static BMotd getPlugin() {
        return plugin;
    }

    private void regListener(Listener listener){
        this.getProxy().getPluginManager().registerListener(plugin, listener);
    }

}
