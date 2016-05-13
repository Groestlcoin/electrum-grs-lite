package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.AbstractTransaction;
import com.coinomi.core.wallet.AbstractWallet;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.connector.Coincap;
import in.multico.listener.ShowListener;
import in.multico.model.Tx;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

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
    @FXML public Label eqvAmt;
    @FXML public Label totalAmt;

    private HashMap <String, AbstractWallet> cIndx = new HashMap<>();
    private Set<CoinType> currCoins = new HashSet<>();
    private AbstractWallet currWa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName();// + " (" + acc.getBalance().toFriendlyString() + ")";
            coins.add(s);
            cIndx.put(s, (AbstractWallet) acc);
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
        List<String> coins = new ArrayList<>();
        final List<WalletAccount> allAccounts = Main.getInstance().getAllAccounts();
        for (WalletAccount wa : allAccounts) {
            if (wa.getCoinType().isTestnet()) continue;
            coins.add(wa.getCoinType().getSymbol());
        }
        Coincap.getInstance().getPrices(coins, new Coincap.PricesListener() {
            @Override
            public void onPrice(Map<String, Double> prices) {
                double all = 0.0;
                setEqvAmt("0.00");
                for (String coin : prices.keySet()) {
                    for (WalletAccount wa : allAccounts) {
                        if (wa.getCoinType().getSymbol().equals(coin)) {
                            all += wa.getBalance().getValue() / wa.getCoinType().oneCoin().getValue() * prices.get(coin);
                            break;
                        }
                    }
                    if (currWa.getCoinType().getSymbol().equals(coin)) {
                        double price = prices.get(coin);
                        ObservableList<Tx> ttx2 = FXCollections.observableArrayList();
                        long value = currWa.getBalance().getValue();
                        long one = currWa.getCoinType().oneCoin().getValue();
                        final double eqv = value / one * price;
                        String eq = new BigDecimal(eqv).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        setEqvAmt(eq);
                        for (Object tx : currWa.getTransactions().values()) {
                            Tx t = new Tx((AbstractTransaction) tx, currWa);
                            t.setUsdAmt(price);
                            ttx2.add(t);
                        }
                        fillTxTable(ttx2);
                    }
                }
                final double finalAll = all;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        totalAmt.setText(new BigDecimal(finalAll).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " USD");
                    }
                });
            }
        });
        coinIcon.setImage(Main.getCoinImage(currWa.getCoinType()));
        coinAmt.setText(currWa.getBalance().toFriendlyString());
        coinAddr.setText(Main.getAddr(currWa));
        ObservableList<Tx> ttx = FXCollections.observableArrayList();
        for (Object tx : currWa.getTransactions().values()) {
            Tx t = new Tx((AbstractTransaction) tx, currWa);
            ttx.add(t);
        }
        fillTxTable(ttx);
    }

    private void setEqvAmt(final String amt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                eqvAmt.setText("(" + amt + " USD)");
            }
        });
    }

    private void fillTxTable(final ObservableList<Tx> ttx) {
        Tx.sort(ttx);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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

                final ContextMenu tableContextMenu = new ContextMenu();
                final MenuItem copy = new MenuItem("Copy address");
                copy.disableProperty().bind(Bindings.isEmpty(txTable.getSelectionModel().getSelectedItems()));
                copy.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Tx selected = (Tx) txTable.getSelectionModel().getSelectedItem();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(selected.getSr());
                        Clipboard.getSystemClipboard().setContent(content);
                    }
                });
                tableContextMenu.getItems().add(copy);
                txTable.setContextMenu(tableContextMenu);
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
        Main.refreshLayout(event, new ExchangeController().getLayout());
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
