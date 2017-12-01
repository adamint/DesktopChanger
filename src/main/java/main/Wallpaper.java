package main;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Wallpaper {
    public interface User32 extends Library {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        boolean SystemParametersInfo(int one, int two, String s, int three);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        for (long i = 0; i < Long.MAX_VALUE; i++) setBackground("adamratzman");
    }

    public static String getRandomImage() throws IOException {
        return Jsoup.connect("https://www.wikihow.tech/Special:Randomizer").ignoreContentType(true)
                .get().body().getElementsByTag("img").get(1).attr("data-src");
    }

    public static void setBackground(String user) {
        try {
            String path = "C:\\Users\\" + user + "\\Desktop\\Backgrounds\\" + UUID.randomUUID().toString() + ".jpg";
            try (InputStream in = new URL(getRandomImage()).openStream()) {
                Files.copy(in, Paths.get(path));
            }
            User32.INSTANCE.SystemParametersInfo(0x0014, 0, path, 1);
        } catch (Exception e) {
            setBackground(user);
        }
    }
}