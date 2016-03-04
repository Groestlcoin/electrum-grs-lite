package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;
import in.multico.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

    @FXML
    private void next(ActionEvent event) {
        try {
            if (addCoin != null) {
                String password = pass.getText();
                Wallet wallet = Main.getInstance().getWallet();
                KeyParameter key = wallet.getKeyCrypter().deriveKey(password);
                wallet.createAccount(addCoin, false, key);
                Main.getInstance().setWallet(wallet);
            }
        Main.refreshLayout(event, "main.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            pass.setText("");
            Main.showMessage(Main.getLocString("wrong_pass"));
        }
    }

    public void setAppendedCoin(CoinType coin) {
        addCoin = coin;
        message.setText(Main.getLocString("to_add") + " " + coin.getName() + " " + Main.getLocString("enter_pass"));
    }

}
