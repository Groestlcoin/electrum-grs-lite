package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.Settings;
import in.multico.connector.Coincap;
import in.multico.listener.ShowListener;
import in.multico.model.Tx;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;
import org.bitcoinj.core.Transaction;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class MainController extends ControllerBased implements Initializable{

    @FXML public ListView coinsList;
    @FXML public ImageView coinIcon;
    @FXML public Label coinAmt;
    @FXML public Label coinAddr;
    @FXML public TableView txTable;
    @FXML public TableColumn txTableDate;
    @FXML public TableColumn txTableAmt;
    @FXML public TableColumn txTableStatus;
    @FXML public TableColumn txTableSR;
    @FXML public TableColumn txTableAmtUSD;

    private HashMap <String, WalletAccount> cIndx = new HashMap<>();
    private Set<CoinType> currCoins = new HashSet<>();
    private WalletAccount currWa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName();// + " (" + acc.getBalance().toFriendlyString() + ")";
            coins.add(s);
            cIndx.put(s, acc);
            currCoins.add(acc.getCoinType());
        }
        coinsList.setItems(coins);
        coinsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        setCoin(new_val);
                    }
                });
        coinsList.getSelectionModel().selectFirst();
    }

    private void setCoin(String str) {
        currWa = cIndx.get(str);
        Coincap.getPrice(currWa.getCoinType().getSymbol(), new Coincap.PriceListener() {
            @Override
            public void onPrice(double price) {
                ObservableList<Tx> ttx2 = FXCollections.observableArrayList();
                for (Transaction tx : currWa.getTransactions().values()) {
                    Tx t = new Tx(tx, currWa);
                    t.setUsdAmt(price);
                    ttx2.add(t);
                }
                fillTxTable(ttx2);
            }
        });
        coinIcon.setImage(Main.getCoinImage(currWa.getCoinType()));
        coinAmt.setText(currWa.getBalance().toFriendlyString());
        coinAddr.setText(Main.getAddr(currWa));
        ObservableList<Tx> ttx = FXCollections.observableArrayList();
        for (Transaction tx : currWa.getTransactions().values()) {
            Tx t = new Tx(tx, currWa);
            ttx.add(t);
        }
        fillTxTable(ttx);
    }

    private void fillTxTable(ObservableList<Tx> ttx) {
        Tx.sort(ttx);
        txTable.setItems(ttx);
        txTableDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tx, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDate());
            }
        });
        txTableAmt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tx, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getAmt());
            }
        });
        txTableAmtUSD.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tx, String> p) {
                String usdAmt = p.getValue().getUsdAmt();
                return new ReadOnlyObjectWrapper(usdAmt);
            }
        });
        txTableStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tx, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getConfirms());
            }
        });
        txTableSR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tx, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tx, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getSr());
            }
        });
    }

    @FXML
    public void addNewCoin(ActionEvent event) {
        Main.refreshLayout(event, new AddCoinController().getLayout(), new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((AddCoinController) controller).setCurrCoinsList(currCoins);
            }
        });
    }

    @FXML
    public void copyAddr(ActionEvent actionEvent) {
        ClipboardContent content = new ClipboardContent();
        content.putString(coinAddr.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    public void settings(ActionEvent event) {
        Main.refreshLayout(event, new SettingsController().getLayout());
    }

    @FXML
    public void sendCoin(ActionEvent actionEvent) {
        Main.refreshLayout(actionEvent, new PayController().getLayout(), new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((PayController)controller).setCurWallet(currWa);
            }
        });
    }

    @FXML
    public void invoice(ActionEvent event) {
        Main.refreshLayout(event, new InvoiceController().getLayout(), new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((InvoiceController)controller).setCurrWa(currWa);
            }
        });
    }

    @FXML
    public void exchange(ActionEvent event) {
        if (Settings.getInstanse().getPoloKey() == null) {
            Main.refreshLayout(event, new AddExchangeController().getLayout());
        } else {
            Main.refreshLayout(event, new ExchangeController().getLayout());
        }
    }

    @Override
    public String getLayout() {
        return "main.fxml";
    }

    @Override
    public void refresh() {
        ObservableList items = coinsList.getSelectionModel().getSelectedItems();
        if (items.size() == 1) {
            String sel = (String) items.get(0);
            setCoin(sel);
        }
    }
}
