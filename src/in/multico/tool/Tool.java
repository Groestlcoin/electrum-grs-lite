package in.multico.tool;

import com.coinomi.core.coins.CoinType;
import in.multico.Main;
import javafx.scene.image.Image;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by vp
 * on 08.06.16.
 */
public class Tool {

    public static String md5(String path) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(path)));
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocString(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.strings", Tool.getLocale());
        return bundle.getString(key);
    }

    public static Locale getLocale() {
        Locale loc = Locale.getDefault();
        Main.log("system locale: " + loc);
        if (loc.getLanguage().equals("ru")) {
            return loc;
        }
        return new Locale("en", "EN");
    }

    public static boolean isWindows() {
        String s = System.getProperty("os.name");
        s = s.toLowerCase();
        return s.contains("win");
    }

    public static boolean isUnix() {
        String s = System.getProperty("os.name");
        s = s.toLowerCase();
        return (s.contains("nix") || s.contains("nux") || s.contains("aix"));
    }

    public static Image getCoinImage(CoinType coin) {
        String name = coin.getName();
        if (name.contains("beta")) name = name.split(" ")[0];
        name = name.replaceAll(" ", "_");
        name = name.toLowerCase();
        InputStream stream = Main.class.getResourceAsStream("icons/" + name + ".png");
        return new Image(stream);
    }
}
