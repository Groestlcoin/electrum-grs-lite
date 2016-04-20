package in.multico.controller;

import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class CoinSelectController extends ControllerBased implements Initializable {

    @FXML public ListView coinsList;
    private HashMap<String, WalletAccount> cIndx = new HashMap<>();
    private WalletAccount currWa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName();// + " (" + acc.getBalance().toFriendlyString() + ")";
            coins.add(s);
            cIndx.put(s, acc);
        }
        coinsList.setItems(coins);
        coinsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        currWa = cIndx.get(new_val);
                    }
                });
        coinsList.getSelectionModel().selectFirst();
    }

    @Override
    public String getLayout() {
        return "coin_select.fxml";
    }

    @Override
    protected void refresh() {

    }

    public void back(ActionEvent event) {

    }

    public void next(ActionEvent event) {

    }
}
