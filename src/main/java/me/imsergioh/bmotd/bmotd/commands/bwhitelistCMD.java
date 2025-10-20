package me.imsergioh.bmotd.bmotd.commands;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.manager.MOTDManager;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class bwhitelistCMD extends Command {

    private final BMotd plugin = BMotd.getPlugin();

    public bwhitelistCMD() {
        super("bwhitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            ChatUtil.sendCopyright(sender);
            return;
        }

        MOTDManager motdManager = plugin.getMotdManager();
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length >= 2) {
                    String name = args[1];
                    motdManager.addWhitelistName(name);
                    sender.sendMessage(ChatUtil.chatColor("&aNombre añadido a la lista blanca!"));
                } else {
                    sender.sendMessage(ChatUtil.chatColor("&cUso correcto: /bwhitelist add <nombre>"));
                }
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length >= 2) {
                    String name = args[1];
                    motdManager.removeWhitelistName(name);
                    sender.sendMessage(ChatUtil.chatColor("&cNombre eliminado de la lista blanca!"));

                    // KICK IF IS CONNECTED (TRY TO AVOID ERRORS)
                    try {
                        if (motdManager.isWhitelistActive(null)) {
                            ProxiedPlayer player = BMotd.getPlugin().getProxy().getPlayer(name);
                            if (player.isConnected()) {
                                player.disconnect(ChatUtil.chatColor(plugin.getMotdManager().getDefaultProfile().getNotWhitelistedError()));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                } else {
                    sender.sendMessage(ChatUtil.chatColor("&cUso correcto: /bwhitelist remove <nombre>"));
                }
            }

            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(ChatUtil.chatColor("&aLista blanca: &7" + motdManager.getWhitelistList()));
            }
        }
    }
}
