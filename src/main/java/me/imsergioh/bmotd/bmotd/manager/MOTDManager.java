package me.imsergioh.bmotd.bmotd.manager;

import lombok.Getter;
import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.instance.Profile;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import me.imsergioh.bmotd.bmotd.util.ConfigUtil;
import me.imsergioh.bmotd.bmotd.util.FileUtil;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MOTDManager {

    private static final BMotd plugin = BMotd.getPlugin();
    private static final Map<String, Profile> domainProfiles = new HashMap<>();

    @Getter
    private Profile defaultProfile;

    private Favicon favicon;
    private ServerPing.Players players;

    public MOTDManager() {
        File file = FileUtil.getConfig("profiles/" + plugin.getConfig().getProfile() + ".json");
        if (!file.exists()) {
            ConfigUtil.write(file.getAbsolutePath(), Profile.getDefault());
        }
        defaultProfile = ConfigUtil.read(file.getAbsolutePath(), Profile.class);

        registerValuesOfConfig();
        loadDomainProfiles();
    }

    public boolean isNotAllowedDomain(String hostnameDomain) {
        for (String dominioListaBlanca : domainsWhitelist()) {
            if (dominioListaBlanca.startsWith("*.")) {
                String dominioListaBlancaSinComodin = dominioListaBlanca.substring(2);
                String regex = "^([a-z0-9]+(-[a-z0-9]+)*\\.)+" + Pattern.quote(dominioListaBlancaSinComodin) + "$";
                if (hostnameDomain.matches(regex)) {
                    return false;
                }
            }
        }
        return !domainsWhitelist().contains(hostnameDomain);
    }

    public List<String> domainsWhitelist() {
        return BMotd.getPlugin().getConfig().getDomainsWhitelist();
    }

    public void registerValuesOfConfig() {
        File faviconFile = new File(plugin.getDataFolder() + "//" + defaultProfile.getIcon());

        try {
            BufferedImage image = ImageIO.read(faviconFile);
            favicon = Favicon.create(image);
            this.players = new ServerPing.Players(getMaxSlots(null), BMotd.getPlugin().getProxy().getOnlineCount(), null);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public List<String> getProfileMotdList() {
        return defaultProfile.getMotd();
    }

    public ServerPing.Players getPlayers(String domain) {
        this.players = new ServerPing.Players(getMaxSlots(domain),
                BMotd.getPlugin().getProxy().getPlayers().size(),
                null);
        return players;
    }

    public String getWhitelistText(String domain) {
        String text = getProfile(domain).getWhitelistText();
        return ChatUtil.chatColor(text);
    }

    public Favicon getFavicon(String domain) {
        if (domain == null) return favicon;
        return loadFromConfigPath(getProfile(domain).getIcon());
    }

    private Favicon loadFromConfigPath(String path) {
        File file = new File(plugin.getDataFolder() + "//" + path);
        try {
            BufferedImage image = ImageIO.read(file);
            return Favicon.create(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWhitelistActive(String domain) {
        Profile profile = getProfile(domain);
        if (profile != null) return profile.isWhitelist();
        return plugin.getConfig().isGlobalWhitelistEnabled();
    }

    public void addWhitelistName(String name) {
        List<String> list = plugin.getConfig().getWhitelistNames();
        list.add(name);
        plugin.getConfig().save();
    }

    public List<String> getWhitelistList() {
        return plugin.getConfig().getWhitelistNames();
    }

    public void removeWhitelistName(String name) {
        List<String> list = plugin.getConfig().getWhitelistNames();
        list.remove(name);
        plugin.getConfig().save();
    }

    public boolean hasWhitelist(String name) {
        List<String> list = plugin.getConfig().getWhitelistNames();
        return list.contains(name);
    }

    public boolean toggleGlobalWhitelist() {
        boolean toggled = !plugin.getConfig().isGlobalWhitelistEnabled();
        plugin.getConfig().setGlobalWhitelistEnabled(toggled);
        return toggled;
    }

    public int getMaxSlots(String domain) {
        return getProfile(domain).getMaxSlots();
    }

    public Profile getProfile(String domain) {
        Profile domainProfile = domainProfiles.get(domain);
        if (domainProfile != null) {
            return domainProfile;
        }
        return defaultProfile;
    }

    private static void loadDomainProfiles() {
        domainProfiles.clear();
        File dir = new File(plugin.getDataFolder()+"/domain-profiles");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (File file : dir.listFiles()) {
            String name = file.getName().replace(".json", "");
            Profile profile = ConfigUtil.read(file.getAbsolutePath(), Profile.class);
            domainProfiles.put(name, profile);
        }
    }

    public BaseComponent getMOTDFromDomain(String domain){
        MOTDManager motdManager = BMotd.getPlugin().getMotdManager();
        List<String> list = motdManager.getProfile(domain).getMotd();
        String motdStr = list.get(0)+"\n"+ list.get(1);
        return new TextComponent(ChatUtil.chatColor(motdStr));
    }
}
