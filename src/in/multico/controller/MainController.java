package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletPocketHD;
import in.multico.Main;
import in.multico.Settings;
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
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.KeyChain;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vp
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

    private String getAddr() {
        Address address;
        if (Settings.getInstanse().isAlwaysRefreshAddr()) {
            address = currWa.getReceiveAddress();
        } else {
            address = ((WalletPocketHD) currWa).getLastUsedAddress(KeyChain.KeyPurpose.RECEIVE_FUNDS);
            if (address == null) {
                address = currWa.getReceiveAddress();
            }
        }
        if (address != null) return address.toString();
        else return "";
    }

    private void setCoin(String str) {
        currWa = cIndx.get(str);
        coinIcon.setImage(Main.getCoinImage(currWa.getCoinType()));
        coinAmt.setText(currWa.getBalance().toFriendlyString());
        coinAddr.setText(getAddr());
        ObservableList<Tx> ttx = FXCollections.observableArrayList();
        for (Transaction tx : currWa.getTransactions().values()) {
            ttx.add(new Tx(tx, currWa));
        }
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
        Main.refreshLayout(event, "add_coin.fxml", new ShowListener() {
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
        Main.refreshLayout(event, "settings.fxml");
    }

    @FXML
    public void sendCoin(ActionEvent actionEvent) {
        Main.refreshLayout(actionEvent, "pay.fxml", new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((PayController)controller).setCurWallet(currWa);
            }
        });
    }

    @FXML
    public void invoice(ActionEvent actionEvent) {
        Main.showMessage(Main.getLocString("soon"));
    }

    @FXML
    public void exchange(ActionEvent actionEvent) {
        Main.showMessage(Main.getLocString("soon"));
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
