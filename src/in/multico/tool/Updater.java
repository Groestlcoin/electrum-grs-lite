package in.multico.tool;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import in.multico.Main;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by vp
 * on 08.06.16.
 */
public class Updater {

    private static final String URL_JAR = "https://www.dropbox.com/s/a7xpiasxcv98ngg/multicoin.jar?dl=1";
    private static final String URL_EXE = "";
    private static final String URL_CHSM = "https://www.dropbox.com/s/jkg03ya3nc1zamm/md5?dl=1";
    private UpdateListener updateListener;
    private File tmpFile, currFile;

    public Updater(final UpdateListener ul) {
        updateListener = ul;
        currFile = new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public void prepare() {
        if (!currFile.getAbsolutePath().endsWith(".jar")) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String md5 = Tool.md5(currFile.getAbsolutePath());
                    Request request = new Request.Builder().url(URL_CHSM).build();
                    Response response = new OkHttpClient().newCall(request).execute();
                    String md5r = response.body().string();
                    Main.log("current: " + md5 + " remote: " + md5r);
                    if (!md5.equals(md5r)) {
                        request = new Request.Builder().url(URL_JAR).build();
                        response = new OkHttpClient().newCall(request).execute();
                        BufferedInputStream in = new BufferedInputStream(response.body().byteStream());
                        byte[] buf = new byte[1024];
                        int c;
                        tmpFile = new File(currFile.getParent() + File.separator + "tmp.jar");
                        FileOutputStream os = new FileOutputStream(tmpFile);
                        while ((c = in.read(buf)) != -1) {
                            os.write(buf, 0, c);
                        }
                        os.flush();
                        os.close();
                        in.close();
                        Main.log("received: " + tmpFile.getAbsolutePath());
                        updateListener.onReady();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void finish() {
        try {
            FileChannel srcChannel = new FileInputStream(tmpFile).getChannel();
            FileChannel destChannel = new FileOutputStream(currFile).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
            tmpFile.delete();
            updateListener.onFinish();
        } catch (Exception e) {
            updateListener.onError(e);
        }
    }

    public interface UpdateListener {
        void onReady();
        void onFinish();
        void onError(Exception e);
    }
}
