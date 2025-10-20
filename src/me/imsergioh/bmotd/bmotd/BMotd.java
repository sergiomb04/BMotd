package me.imsergioh.bmotd.bmotd;

import me.imsergioh.bmotd.bmotd.commands.bmotdCMD;
import me.imsergioh.bmotd.bmotd.commands.bwhitelistCMD;
import me.imsergioh.bmotd.bmotd.commands.maintenanceCMD;
import me.imsergioh.bmotd.bmotd.listener.JoinEvent;
import me.imsergioh.bmotd.bmotd.listener.PingEvent;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.manager.MOTDUtil;
import net.md_5.bungee.api.plugin.Command;
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

        registerCommands(
                new bmotdCMD(),
                new maintenanceCMD(),
                new bwhitelistCMD()
        );

        regListeners(new PingEvent(), new JoinEvent());
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

    private void registerCommands(Command... commands) {
        for (Command command : commands) {
            getProxy().getPluginManager().registerCommand(plugin, command);
        }
    }

    private void regListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getProxy().getPluginManager().registerListener(plugin, listener);
        }
    }

}
