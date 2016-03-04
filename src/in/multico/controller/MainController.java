package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
public class MainController implements Initializable{

    @FXML public ListView coinsList;
    @FXML public ImageView coinIcon;
    @FXML public Label coinAmt;
    @FXML public Label coinAddr;
    private HashMap <String, WalletAccount> cIndx = new HashMap<>();
    private Set<CoinType> currCoins = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName() + " (" + acc.getBalance().toFriendlyString() + ")";
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
        WalletAccount wa = cIndx.get(str);
        coinIcon.setImage(Main.getCoinImage(wa.getCoinType()));
        coinAmt.setText(wa.getBalance().toFriendlyString());
        coinAddr.setText(wa.getReceiveAddress().toString());
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
        Main.showMessage(Main.getLocString("soon"));
    }

    @FXML
    public void invoice(ActionEvent actionEvent) {
        Main.showMessage(Main.getLocString("soon"));
    }

    @FXML
    public void exchange(ActionEvent actionEvent) {
        Main.showMessage(Main.getLocString("soon"));
    }
}
