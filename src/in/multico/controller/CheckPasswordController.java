package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;
import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.bitcoinj.crypto.KeyCrypter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.spongycastle.crypto.params.KeyParameter;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 09:38
 */
public class CheckPasswordController {

    @FXML public Label message;
    @FXML public PasswordField pass;
    private CoinType addCoin;
    public enum Next {addCoin, showMnemonic}
    private Next nextStep;

    @FXML
    private void next(ActionEvent event) {
        try {
            if (Next.addCoin.equals(nextStep)) {
                if (addCoin != null) {
                    String password = pass.getText();
                    Wallet wallet = Main.getInstance().getWallet();
                    KeyCrypter crypter = wallet.getKeyCrypter();
                    if (crypter != null) {
                        KeyParameter key = crypter.deriveKey(password);
                        wallet.createAccount(addCoin, false, key);
                        Main.getInstance().setWallet(wallet);
                    }
                    Main.refreshLayout(event, "main.fxml");
                }
            }
            else if (Next.showMnemonic.equals(nextStep)) {
                String password = pass.getText();
                Wallet wallet = Main.getInstance().getWallet();
                KeyCrypter crypter = wallet.getKeyCrypter();
                if (crypter == null) {
                    Main.refreshLayout(event, "main.fxml");
                    return;
                }
                KeyParameter key = crypter.deriveKey(password);
                DeterministicSeed seed = wallet.getSeed();
                if (seed == null) {
                    Main.refreshLayout(event, "main.fxml");
                    return;
                }
                final DeterministicSeed dSeed = seed.decrypt(crypter, null, key);
                Main.refreshLayout(event, "show_mnemonic.fxml", new ShowListener() {
                    @Override
                    public void onShow(Object controller) {
                        ((ShowMnemonicController) controller).setMnemonic(dSeed.getMnemonicCode());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            pass.setText("");
            Main.showMessage(Main.getLocString("wrong_pass"));
        }
    }

    public void setNextStepMnemonic() {
        nextStep = Next.showMnemonic;
    }

    public void setAppendedCoin(CoinType coin) {
        addCoin = coin;
        message.setText(Main.getLocString("to_add") + " " + coin.getName() + " " + Main.getLocString("enter_pass"));
        nextStep = Next.addCoin;
    }



}
