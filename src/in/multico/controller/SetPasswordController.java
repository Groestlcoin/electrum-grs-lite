package in.multico.controller;

import com.coinomi.core.coins.BitcoinMain;
import com.coinomi.core.wallet.Wallet;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.tool.Tool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.List;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class SetPasswordController extends ControllerBased{

    public TextField pass, repass;
    private List<String> mm;
    private List<WalletAccount> waList;

    @FXML
    private void next(ActionEvent event) {
        if (pass.getText().length() < 4) Main.showMessage(Tool.getLocString("err_pass_len"));
        if (!pass.getText().equals(repass.getText())) Main.showMessage(Tool.getLocString("err_passs_match"));
        try {
            Wallet wallet = new Wallet(mm);
            KeyCrypterScrypt crypter = new KeyCrypterScrypt();
            KeyParameter aesKey = crypter.deriveKey(pass.getText());
            wallet.encrypt(crypter, aesKey);
            if (waList == null) {
                wallet.createAccount(BitcoinMain.get(), false, aesKey);
            } else {
                for (WalletAccount wa : waList) {
                    wa.encrypt(crypter, aesKey);
                    wallet.addAccount(wa);
                }
            }
            Main.getInstance().setWallet(wallet);
            Main.refreshLayout(event, new MainController().getLayout());
        } catch (Exception e) {
            e.printStackTrace();
            Main.showMessage(Tool.getLocString("err_wallet_create") + ": " + e.getMessage());
        }
    }

    public void setMnemonic(List<String> mm, List<WalletAccount> waList) {
        this.mm = mm;
        this.waList = waList;
    }

    @Override
    public String getLayout() {
        return "set_password.fxml";
    }

    @Override
    protected void refresh() {

    }
}
