package me.imsergioh.bmotd.bmotd.commands;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class bmotdCMD extends Command {

    private BMotd plugin = BMotd.getPlugin();

    public bmotdCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("*")){
            MOTDManager motdManager = plugin.getMotdManager();

            if(args[0].equalsIgnoreCase("reload")){
                BMotd.getPlugin().reloadBMotd();
                sender.sendMessage("MOTD Reloaded!");
            } else {
                sender.sendMessage(ChatUtil.chatColor("Correct usage: /bmotd reload"));
            }
        } else {
            sender.sendMessage(ChatUtil.chatColor("&cPlugin BMotd vPRIVATE created by ImSergioh"));
        }
    }
}
