package me.imsergioh.bmotd.bmotd.config;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.bmotd.bmotd.util.ConfigUtil;
import me.imsergioh.bmotd.bmotd.util.FileUtil;

import java.util.Arrays;
import java.util.List;

@Getter
public class PluginConfig {

    private final String profile;
    @Setter
    private boolean globalWhitelistEnabled;
    private final String notWhitelistedError;
    private final String whitelistText;
    private final List<String> whitelistNames;
    private final List<String> domainsWhitelist;

    public PluginConfig(String profile, boolean globalWhitelistEnabled, String notWhitelistedError, String whitelistText, List<String> whitelistNames, List<String> domainsWhitelist) {
        this.profile = profile;
        this.globalWhitelistEnabled = globalWhitelistEnabled;
        this.notWhitelistedError = notWhitelistedError;
        this.whitelistText = whitelistText;
        this.whitelistNames = whitelistNames;
        this.domainsWhitelist = domainsWhitelist;
    }

    public void save() {
        ConfigUtil.write(FileUtil.getConfig("config.json").getAbsolutePath(), this);
    }

    public static PluginConfig getDefault() {
        return new PluginConfig("default",
                true,
                "&cMantenimiento.",
                "&4&lMANTENIMIENTO",
                Arrays.asList("ImSergioh", "md_5"),
                Arrays.asList("play.imsergioh.me", "imsergioh.me", "play.smartmc.us", "*.smartmc.us"));
    }
}
