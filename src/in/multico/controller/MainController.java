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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName() + " (" + acc.getBalance().toFriendlyString() + ")";
            coins.add(s);
            cIndx.put(s, acc);
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
    }

    @FXML
    public void addNewCoin(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    @FXML
    public void copyAddr(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    @FXML
    public void sendCoin(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    @FXML
    public void invoice(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    @FXML
    public void exchange(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }
}
