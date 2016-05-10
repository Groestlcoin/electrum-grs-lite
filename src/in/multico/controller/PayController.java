package in.multico.controller;

import com.coinomi.core.coins.Value;
import com.coinomi.core.wallet.AbstractAddress;
import com.coinomi.core.wallet.SendRequest;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletPocketHD;
import in.multico.Main;
import in.multico.listener.CloseListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
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
    @FXML public ProgressBar progress;
    private WalletAccount wa;

    @Override
    public String getLayout() {
        return "pay.fxml";
    }

    @Override
    protected void refresh() {

    }

    public void setCurWallet(WalletAccount wa) {
        this.wa = wa;
        this.ava.setText(wa.getBalance().toFriendlyString());
        this.ccy.setText(wa.getCoinType().getSymbol());
    }

    public void back(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    public void send(final ActionEvent event) {
        progress.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = addr.getText();
                boolean success = false;
                try {
                    AbstractAddress a = wa.getCoinType().newAddress(address);
                    String sum = amt.getText();
                    if (sum.isEmpty()) throw new Exception("empty amt");
                    Value coin = wa.getCoinType().value(sum);
                    WalletPocketHD wph = (WalletPocketHD) wa;
                    SendRequest request = wa.getSendToRequest(a, coin);
                    KeyCrypter crypter = wph.getKeyCrypter();
                    if (crypter == null) throw new Exception("empty crypter");
                    request.aesKey = crypter.deriveKey(pass.getText());
                    request.signTransaction = true;
                    wph.completeAndSignTx(request);
                    if (!wph.broadcastTxSync(request.tx)) {
                        throw new Exception("Error broadcasting transaction: " + request.tx.getHashAsString());
                    }
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final boolean finalSuccess = success;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisible(false);
                        if (finalSuccess) {
                            Main.showMessage(Main.getLocString("coins_sent"), new CloseListener() {
                                @Override
                                public void onClose() {
                                    Main.refreshLayout(event, new MainController().getLayout());
                                }
                            });
                        } else {
                            Main.showMessage(Main.getLocString("err_data"));
                        }
                    }
                });
            }
        }).start();
    }
}
