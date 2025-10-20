package me.imsergioh.bmotd.bmotd.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.imsergioh.bmotd.bmotd.BMotd;
import me.imsergioh.bmotd.bmotd.config.PluginConfig;
import me.imsergioh.bmotd.bmotd.instance.Profile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static PluginConfig readConfig() {
        return read(FileUtil.getConfig("config.json").getAbsolutePath(), PluginConfig.class);
    }

    public static Profile readProfile(String fileName) {
        return read(fileName, Profile.class);
    }

    public static <T> T read(String fileName, Class<T> clazz) {
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static void write(String fileName, Object o) {
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(o, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProfileFileName(String name) {
        return new File(BMotd.getPlugin().getDataFolder() + "/profiles", name + ".json").getAbsolutePath();
    }

    public static String getDomainProfileFileName(String name) {
        return new File(BMotd.getPlugin().getDataFolder() + "/domain-profiles", name + ".json").getAbsolutePath();
    }

}
