package me.imsergioh.bmotd.bmotd.manager;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class MOTDUtil {

    private Configuration config;

    private final HashMap<String, BaseComponent> motdMap = new HashMap<>();

    public MOTDUtil(){
        registerConfigFromConfig();
    }

    public BaseComponent getMOTDFromDomain(String domain){
        if(motdMap.containsKey(domain)){
            return motdMap.get(domain);
        }
        return null;
    }

    public void registerConfigFromConfig(){
        File file = new File(BMotd.getPlugin().getDataFolder(), "config.yml");
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        for(String all : config.getSection("domain-profiles").getKeys()){
            List<String> motdList = config.getStringList("domain-profiles."+all+".motd");
            if (motdList.isEmpty()) motdList = BMotd.getPlugin().getMotdManager().getProfileMotdList();
            String motdStr = motdList.get(0)+"\n"+ motdList.get(1);
            motdMap.put(all, new TextComponent(ChatUtil.chatColor(motdStr)));
        }
    }

}
