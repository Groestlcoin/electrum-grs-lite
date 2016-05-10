package in.multico.controller;

import com.coinomi.core.coins.BitcoinTest;
import com.coinomi.core.coins.DogecoinTest;
import com.coinomi.core.coins.LitecoinTest;
import com.coinomi.core.exchange.shapeshift.ShapeShift;
import com.coinomi.core.exchange.shapeshift.data.ShapeShiftNormalTx;
import com.coinomi.core.exchange.shapeshift.data.ShapeShiftRate;
import com.coinomi.core.util.ExchangeRate;
import com.coinomi.core.wallet.SendRequest;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletPocketHD;
import in.multico.Main;
import in.multico.listener.CloseListener;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.bitcoinj.core.Coin;
import org.bitcoinj.crypto.KeyCrypter;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class ExchangeController extends ControllerBased implements Initializable {

    @FXML public ListView sellList;
    @FXML public ListView buyList;
    @FXML public Label giveCcy;
    @FXML public TextField giveAmt;
    @FXML public PasswordField pass;
    @FXML public Label getCcy;
    @FXML public TextField getAmt;
    @FXML public Label ava;
    @FXML public ProgressBar progress;

    private HashMap<String, WalletAccount> cIndx = new HashMap<>();
    private WalletAccount waFrom;
    private WalletAccount waTo;
    private ExchangeRate ex;
    private ShapeShift ss;

    @Override
    public String getLayout() {
        return "exchange.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ss = new ShapeShift();
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount wa : Main.getInstance().getAllAccounts()) {
            if (wa.getCoinType().getSymbol().equals(BitcoinTest.get().getSymbol()) ||
                    wa.getCoinType().getSymbol().equals(LitecoinTest.get().getSymbol()) ||
                    wa.getCoinType().getSymbol().equals(DogecoinTest.get().getSymbol())) continue;
            coins.add(wa.getCoinType().getName());
            cIndx.put(wa.getCoinType().getName(), wa);
        }
        giveAmt.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    if (getAmt.isFocused()) return;
                    String give = (String) newValue;
                    if (ex != null) getAmt.setText(ex.convert(waFrom.getCoinType(), Coin.parseCoin(give)).toPlainString());
                } catch (Exception ignored) {}
            }
        });
        getAmt.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    if (giveAmt.isFocused()) return;
                    String get = (String) newValue;
                    if (ex != null) giveAmt.setText(ex.convert(waTo.getCoinType(), Coin.parseCoin(get)).toPlainString());
                } catch (Exception ignored) {}
            }
        });
        sellList.setItems(coins);
        sellList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        waFrom = cIndx.get(new_val);
                        ava.setText(waFrom.getBalance().toFriendlyString());
                        giveCcy.setText(waFrom.getCoinType().getSymbol());
                        calcPrice();
                    }
                });
        buyList.setItems(coins);
        buyList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        waTo = cIndx.get(new_val);
                        getCcy.setText(waTo.getCoinType().getSymbol());
                        calcPrice();
                    }
                });
    }

    private void calcPrice() {
        ex = null;
        if (waFrom == null || waTo == null || waFrom.equals(waTo)) {
            return;
        }
        progress.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ShapeShiftRate rate = ss.getRate(waFrom.getCoinType(), waTo.getCoinType());
                    if (rate.isError) throw new Exception(rate.errorMessage);
                    ex = rate.rate;
                    if (ex == null || !ex.canConvert(waFrom.getCoinType(), waTo.getCoinType())) {
                        throw new Exception("can't convert");
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            getAmt.setPromptText(ex.convert(waFrom.getCoinType(), Coin.parseCoin(giveAmt.getPromptText())).toPlainString());
                        }
                    });
                } catch (Exception e) {
                    ex = null;
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisible(false);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void refresh() {}

    public void back(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    public void save(final ActionEvent event) {
        progress.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                try {
                    ShapeShiftNormalTx tx = ss.exchange(waTo.getReceiveAddress(), waFrom.getRefundAddress(false));
                    WalletPocketHD wph = (WalletPocketHD) waFrom;
                    SendRequest request = waFrom.getSendToRequest(tx.deposit, waFrom.getCoinType().value(giveAmt.getText()));
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
