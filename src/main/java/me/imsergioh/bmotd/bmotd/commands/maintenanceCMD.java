package me.imsergioh.bmotd.bmotd.commands;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class maintenanceCMD extends Command {

    private final BMotd plugin = BMotd.getPlugin();

    public maintenanceCMD() {
        super("maintenance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            ChatUtil.sendCopyright(sender);
            return;
        }


        boolean enabled = plugin.getMotdManager().toggleGlobalWhitelist();

        String feedBackMessage = enabled ?
                "&fMantenimiento: &aActivado" :
                "&fMantenimiento: &cDesactivado";
        broadcastAdmins(feedBackMessage);

        if (enabled) {
            for (ProxiedPlayer player : BMotd.getPlugin().getProxy().getPlayers()) {
                if (!plugin.getMotdManager().hasWhitelist(player.getName())) {
                    player.disconnect(ChatUtil.chatColor(plugin.getMotdManager().getDefaultProfile().getNotWhitelistedError()));
                }
            }
        }
    }

    private void broadcastAdmins(String message){
        for (ProxiedPlayer player : BMotd.getPlugin().getProxy().getPlayers()) {
            if (!player.hasPermission("*")) continue;
            player.sendMessage(ChatUtil.chatColor(message));
        }
        BMotd.getPlugin().getProxy().getConsole().sendMessage(ChatUtil.chatColor(message));
    }

}
