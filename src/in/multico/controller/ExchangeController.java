package in.multico.controller;

import com.coinomi.core.coins.BitcoinMain;
import com.coinomi.core.coins.BitcoinTest;
import com.coinomi.core.coins.DogecoinTest;
import com.coinomi.core.coins.LitecoinTest;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.connector.Poloniex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.math.BigDecimal;
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
    @FXML public CheckBox delay;
    @FXML public TextField price;
    @FXML public Label buyCcy;
    @FXML public TextField buyAmt;
    @FXML public PasswordField pass;
    @FXML public Label selCcy;
    @FXML public TextField selAmt;
    @FXML public Label ava;

    private HashMap<String, WalletAccount> cIndx = new HashMap<>();
    private JSONObject ticker = new JSONObject();
    private WalletAccount waFrom;
    private WalletAccount waTo;
    private static final String PRICE_NOP = "-----";

    @Override
    public String getLayout() {
        return "exchange.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Poloniex.getTicker(new Poloniex.RespListener() {
            @Override
            public void onResp(JSONObject t) {
                ticker = t;
            }
        });
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount wa : Main.getInstance().getAllAccounts()) {
            if (wa.getCoinType().getSymbol().equals(BitcoinTest.get().getSymbol()) ||
                    wa.getCoinType().getSymbol().equals(LitecoinTest.get().getSymbol()) ||
                    wa.getCoinType().getSymbol().equals(DogecoinTest.get().getSymbol())) continue;
            coins.add(wa.getCoinType().getName());
            cIndx.put(wa.getCoinType().getName(), wa);
        }
        sellList.setItems(coins);
        sellList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        waFrom = cIndx.get(new_val);
                        ava.setText(waFrom.getBalance().toFriendlyString());
                        selCcy.setText(waFrom.getCoinType().getSymbol());
                        calcPrice();
                    }
                });
        buyList.setItems(coins);
        buyList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        waTo = cIndx.get(new_val);
                        buyCcy.setText(waTo.getCoinType().getSymbol());
                        calcPrice();
                    }
                });
    }

    private void calcPrice() {
        if (waFrom == null || waTo == null || waFrom.equals(waTo) || (!waFrom.getCoinType().equals(BitcoinMain.get()) && !waTo.getCoinType().equals(BitcoinMain.get()))) {
            price.setText(PRICE_NOP);
            return;
        }
        boolean buy = waFrom.getCoinType().equals(BitcoinMain.get());
        String pair = BitcoinMain.get().getSymbol() + "_" + (buy ? waTo.getCoinType().getSymbol() : waFrom.getCoinType().getSymbol());
        JSONObject jo = ticker.optJSONObject(pair);
        if (jo == null) {
            price.setText(PRICE_NOP);
            return;
        }
        double v = jo.optDouble(buy ? "lowestAsk" : "highestBid");
        price.setText(new BigDecimal(v).setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString());
    }

    @Override
    protected void refresh() {}

    public void back(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    public void save(ActionEvent event) {
        // TODO:
    }
}
