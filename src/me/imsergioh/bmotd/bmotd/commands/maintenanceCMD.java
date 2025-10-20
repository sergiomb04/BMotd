package me.imsergioh.bmotd.bmotd.commands;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class maintenanceCMD extends Command {

    private BMotd plugin = BMotd.getPlugin();

    public maintenanceCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("*")){
            boolean enabled = plugin.getMotdManager().toggleWhitelist();
            if(enabled){
                for (ProxiedPlayer player : BMotd.getPlugin().getProxy().getPlayers()) {
                    if(!plugin.getMotdManager().hasWhitelist(player.getName())){
                        player.disconnect(ChatUtil.chatColor(plugin.getMotdManager().getConfig().getString("messages.not-whitelist")));
                    }
                }
                broadcastAdmins("&fMantenimiento: &aActivado.");
                
            } else {
                broadcastAdmins("&fMantenimiento: &cDesactivado.");
            }
        } else {
            sender.sendMessage(ChatUtil.chatColor("&cPlugin BMotd vPRIVATE created by ImSergioh"));
        }
    }

    private void broadcastAdmins(String message){
        for(ProxiedPlayer player : BMotd.getPlugin().getProxy().getPlayers()){
            if(player.hasPermission("*")){
                player.sendMessage(ChatUtil.chatColor(message));
            }
        }
        BMotd.getPlugin().getProxy().getConsole().sendMessage(ChatUtil.chatColor(message));
    }

}
