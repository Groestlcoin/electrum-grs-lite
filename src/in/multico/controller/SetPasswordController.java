package in.multico.controller;

import com.coinomi.core.coins.BitcoinMain;
import com.coinomi.core.wallet.Wallet;
import in.multico.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 09:38
 */
public class SetPasswordController {

    public TextField pass, repass;
    private List<String> mm;

    @FXML
    private void next(ActionEvent event) {
        if (pass.getText().length() < 4) Main.showMessage(Main.getLocString("err_pass_len"));
        if (!pass.getText().equals(repass.getText())) Main.showMessage(Main.getLocString("err_passs_match"));
        try {
            Wallet wallet = new Wallet(mm);
            KeyCrypterScrypt crypter = new KeyCrypterScrypt();
            KeyParameter aesKey = crypter.deriveKey(pass.getText());
            wallet.encrypt(crypter, aesKey);
            wallet.createAccount(BitcoinMain.get(), false, aesKey);
            Main.getInstance().setWallet(wallet);
            Main.refreshLayout(event, "main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Main.showMessage(Main.getLocString("err_wallet_create") + ": " + e.getMessage());
        }
    }

    public void setMnemonic(List<String> mm) {
        this.mm = mm;
    }

}
