package me.imsergioh.bmotd.bmotd.instance;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Profile {

    private final int maxSlots;
    private final String icon;
    private final List<String> motd;
    private final boolean whitelist;
    private final String notWhitelistedError;
    private final String whitelistText;

    public Profile(int maxSlots, String icon,
                   List<String> motd,
                   boolean whitelist,
                   String notWhitelistedError,
                   String whitelistText) {
        this.maxSlots = maxSlots;
        this.icon = icon;
        this.motd = motd;
        this.whitelist = whitelist;
        this.notWhitelistedError = notWhitelistedError;
        this.whitelistText = whitelistText;
    }

    public static Profile getDefault() {
        return new Profile(
                200,
                "/icon/default.png",
                Arrays.asList("&aLine1", "&9Line2"),
                false,
                "&cServidor cerrado.",
                "&4&lCERRADO");
    }

}
