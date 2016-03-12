package in.multico.controller;

import com.coinomi.core.wallet.SendRequest;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletPocketHD;
import in.multico.Main;
import in.multico.listener.CloseListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.crypto.KeyCrypter;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class PayController extends ControllerBased {


    @FXML public Label ava;
    @FXML public Label ccy;
    @FXML public TextField amt;
    @FXML public TextField addr;
    @FXML public TextField pass;
    private WalletAccount wa;

    @Override
    protected void refresh() {

    }

    public void setCurWallet(WalletAccount wa) {
        this.wa = wa;
        this.ava.setText(wa.getBalance().toFriendlyString());
        this.ccy.setText(wa.getCoinType().getSymbol());
    }

    public void back(ActionEvent event) {
        Main.refreshLayout(event, "main.fxml");
    }

    public void send(final ActionEvent event) {
        String address = addr.getText();
        try {
            Address a = wa.getCoinType().address(address);
            String sum = amt.getText();
            if (sum.isEmpty()) throw new Exception("empty amt");
            Coin coin = Coin.parseCoin(sum);
            WalletPocketHD wph = (WalletPocketHD) wa;
            SendRequest request = SendRequest.to(a, coin);
            KeyCrypter crypter = wph.getKeyCrypter();
            if (crypter == null) throw new Exception("empty crypter");
            request.aesKey = crypter.deriveKey(pass.getText());
            request.signInputs = true;
            wph.completeAndSignTx(request);
            if (!wph.broadcastTxSync(request.tx)) {
                throw new Exception("Error broadcasting transaction: " + request.tx.getHashAsString());
            }
            Main.showMessage(Main.getLocString("coins_sent"), new CloseListener() {
                @Override
                public void onClose() {
                    Main.refreshLayout(event, "main.fxml");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Main.showMessage(Main.getLocString("err_data"));
        }
    }
}
