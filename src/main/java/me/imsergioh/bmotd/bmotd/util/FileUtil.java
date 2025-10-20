package me.imsergioh.bmotd.bmotd.util;

import me.imsergioh.bmotd.bmotd.BMotd;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File getConfig(String path) {
        return new File(BMotd.getPlugin().getDataFolder() + "/" + path);
    }

    public static void createFile(File file) {
        createParents(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createParents(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

}
