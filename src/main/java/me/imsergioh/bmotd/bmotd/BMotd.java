package me.imsergioh.bmotd.bmotd;

import lombok.Getter;
import me.imsergioh.bmotd.bmotd.commands.bmotdCMD;
import me.imsergioh.bmotd.bmotd.commands.bwhitelistCMD;
import me.imsergioh.bmotd.bmotd.commands.maintenanceCMD;
import me.imsergioh.bmotd.bmotd.config.PluginConfig;
import me.imsergioh.bmotd.bmotd.instance.Profile;
import me.imsergioh.bmotd.bmotd.listener.JoinEvent;
import me.imsergioh.bmotd.bmotd.listener.PingEvent;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ConfigUtil;
import me.imsergioh.bmotd.bmotd.util.FileUtil;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

@Getter
public final class BMotd extends Plugin {

    @Getter
    private static BMotd plugin;

    private PluginConfig config;
    private MOTDManager motdManager;

    @Override
    public void onEnable() {
        plugin = this;
        registerDefaultConfig();
        registerDefaultProfile();
        motdManager = new MOTDManager();

        registerCommands(
                new bmotdCMD(),
                new maintenanceCMD(),
                new bwhitelistCMD()
        );

        regListeners(new PingEvent(), new JoinEvent());
    }

    private void registerDefaultConfig() {
        File file = FileUtil.getConfig("config.json");
        if (!file.exists()) {
            FileUtil.createFile(file);
            ConfigUtil.write(file.getAbsolutePath(), PluginConfig.getDefault());
        }
        config = ConfigUtil.read(file.getAbsolutePath(), PluginConfig.class);
    }

    private void registerDefaultProfile() {
        File file = FileUtil.getConfig("profiles/default.json");
        if (!file.exists()) {
            FileUtil.createFile(file);
            ConfigUtil.write(file.getAbsolutePath(), Profile.getDefault());
        }
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

    public void reloadBMotd() {
        registerDefaultConfig();
        registerDefaultProfile();
        motdManager = new MOTDManager();
    }
}
