package me.imsergioh.bmotd.bmotd.util;

import me.imsergioh.bmotd.bmotd.BMotd;
import net.md_5.bungee.api.CommandSender;

public class ChatUtil {

    public static void sendCopyright(CommandSender sender) {
        sender.sendMessage(chatColor("&cPlugin BMotd v" + getVersion() + " created by ImSergioh"));
    }

    public static String chatColor(String text){
        return text.replace("&", "§");
    }

    private static String getVersion() {
        return BMotd.getPlugin().getDescription().getVersion();
    }

}
