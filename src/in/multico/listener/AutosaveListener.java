package in.multico.listener;

import org.bitcoinj.wallet.WalletFiles;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 07:23
 */
public class AutosaveListener implements WalletFiles.Listener {
    @Override
    public void onBeforeAutoSave(File file) {
        System.out.println("begin autosave wallet...");
    }

    @Override
    public void onAfterAutoSave(File file) {
        System.out.println("end autosave wallet. File: " + file.getAbsolutePath());
    }
}
