package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import in.multico.Main;
import in.multico.SyncService;
import in.multico.listener.ShowListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Set;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class AddCoinController extends ControllerBased {

    @FXML public ListView addCoinsList;
    private HashMap<String, CoinType> cIndx = new HashMap<>();
    private CoinType selCoin;

    @FXML
    private void add(ActionEvent event) {
        if (selCoin != null) {
            Main.refreshLayout(event, "check_password.fxml", new ShowListener() {
                @Override
                public void onShow(Object controller) {
                    ((CheckPasswordController) controller).setAppendedCoin(selCoin);
                }
            });
        } else {
            Main.refreshLayout(event, "main.fxml");
        }
    }

    public void setCurrCoinsList(Set<CoinType> curr) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (CoinType coinType : SyncService.SUPPORTED_COINS) {
            if (curr.contains(coinType)) continue;
            coins.add(coinType.getName());
            cIndx.put(coinType.getName(), coinType);
        }
        addCoinsList.setItems(coins);
        addCoinsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        selCoin = cIndx.get(new_val);
                    }
                });
    }

    @Override
    protected void refresh() {

    }
}
