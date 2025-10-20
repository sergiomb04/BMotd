package me.imsergioh.bmotd.bmotd.commands;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class bmotdCMD extends Command {

    public bmotdCMD() {
        super("bmotd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            ChatUtil.sendCopyright(sender);
            return;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
            BMotd.getPlugin().reloadBMotd();
            sender.sendMessage("MOTD Reloaded!");
        }
    }
}
