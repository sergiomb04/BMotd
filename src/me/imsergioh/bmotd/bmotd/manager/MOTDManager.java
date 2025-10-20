package me.imsergioh.bmotd.bmotd.manager;

import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.util.ChatUtil;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MOTDManager {

    private static final BMotd plugin = BMotd.getPlugin();

    private File file;
    private Configuration config;

    private String profileName = "default";
    private List<String> motdList = new ArrayList<>();

    private Favicon favicon;

    private ServerPing.Players players;
    private ServerPing.Protocol protocol;

    private BaseComponent descriptionComponent;

    private int onlineCount;

    public MOTDManager() {
        regFile();
        registerConfigFromConfig();

        if (!config.contains("whitelistText")) {
            config.set("whitelistText", "&4&lMANTENIMIENTO!");
        }

        if (!config.contains("domainsWhitelist")) {
            List<String> domains = new ArrayList<>();
            domains.add("*.hycoremc.net");
            config.set("domainsWhitelist", domains);
        }

        saveConfig();
    }

    public boolean isNotAllowedDomain(String hostnameDomain) {
        if (domainsWhitelist().contains(hostnameDomain)) {
            return false;
        }

        for (String dominioListaBlanca : domainsWhitelist()) {
            if (dominioListaBlanca.startsWith("*.")) {
                String dominioListaBlancaSinComodin = dominioListaBlanca.substring(2);
                String regex = "^([a-z0-9]+(-[a-z0-9]+)*\\.)+" + Pattern.quote(dominioListaBlancaSinComodin) + "$";
                if (hostnameDomain.matches(regex)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<String> domainsWhitelist() {
        return config.getStringList("domainsWhitelist");
    }

    public void registerConfigFromConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        profileName = config.getString("profile");
        motdList = config.getStringList("profiles." + profileName + ".motd");
        File faviconFile = new File(plugin.getDataFolder() + "//" + config.getString("profiles." + profileName + ".icon"));

        try {
            BufferedImage image = ImageIO.read(faviconFile);
            favicon = Favicon.create(image);

            descriptionComponent = new TextComponent(ChatUtil.chatColor(getMOTD()));

            this.players = new ServerPing.Players(getMaxSlots(), BMotd.getPlugin().getProxy().getOnlineCount(), null);
            this.protocol = new ServerPing.Protocol("Requires MC 1.8 / 1.18", BMotd.getPlugin().getProxy().getProtocolVersion());

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public List<String> getProfileMotdList() {
        return config.getStringList("profiles." + profileName + ".motd");
    }

    public ServerPing.Protocol getProtocol() {
        return protocol;
    }

    public ServerPing.Players getPlayers() {
        this.players = new ServerPing.Players(getMaxSlots(), onlineCount, null);
        return players;
    }

    public String getWhitelistText(String domain) {
        String text = config.getString("whitelistText");

        String path = "domain-profiles." + domain.replace(".", "-") + ".whitelistText";
        if (config.contains(path)) {
            text = config.getString(path);
        }
        return ChatUtil.chatColor(text);
    }

    public BaseComponent getDescriptionComponent() {
        return descriptionComponent;
    }

    public Favicon getFavicon(String domain) {
        if (domain == null) return favicon;
        Favicon f = favicon;
        String path = "domain-profiles." + domain.replace(".", "-") + ".icon";
        if (config.contains(path)) {
            f = loadFromConfigPath(path);
        }
        return f;
    }

    private Favicon loadFromConfigPath(String path) {
        File file = new File(plugin.getDataFolder() + "//" + config.getString(path));
        try {
            BufferedImage image = ImageIO.read(file);
            return Favicon.create(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMOTD() {
        return motdList.get(0) + "\n" + motdList.get(1);
    }

    public boolean isActive(String configPath) {
        return config.getBoolean(configPath);
    }

    public boolean isWhitelistActive() {
        return config.getBoolean("config.whitelist-enabled");
    }

    public void addWhitelistName(String name) {
        List<String> list = config.getStringList("config.whitelist");
        list.add(name);
        config.set("config.whitelist", list);
        saveConfig();
    }

    public List<String> getWhitelistList() {
        return config.getStringList("config.whitelist");
    }

    public void removeWhitelistName(String name) {
        List<String> list = config.getStringList("config.whitelist");
        list.remove(name);
        config.set("config.whitelist", list);
        saveConfig();
    }

    public boolean hasWhitelist(String name) {
        if (config.getBoolean("config.whitelist-enabled")) {
            return config.getStringList("config.whitelist").contains(name);
        }
        return true;
    }

    public boolean toggleWhitelist() {
        config.set("config.whitelist-enabled", !config.getBoolean("config.whitelist-enabled"));
        saveConfig();
        return config.getBoolean("config.whitelist-enabled");
    }

    public int getMaxSlots() {
        return config.getInt("profiles." + profileName + ".max-slots");
    }

    private void regFile() {
        plugin.getDataFolder().mkdirs();
        this.file = new File(plugin.getDataFolder(), "config.yml");
        try {
            if (!file.exists()) {
                try (InputStream in = plugin.getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public Configuration getConfig() {
        return config;
    }
}
