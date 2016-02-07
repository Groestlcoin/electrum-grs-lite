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
    private static final String NEW_COIN = "New coin...";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> coins = FXCollections.observableArrayList();
        for (WalletAccount acc : Main.getInstance().getAllAccounts()) {
            String s = acc.getCoinType().getName() + " (" + acc.getBalance().toFriendlyString() + ")";
            coins.add(s);
            cIndx.put(s, acc);
        }
        coins.add(NEW_COIN);
        coinsList.setItems(coins);
        coinsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                        setCoin(new_val);
                    }
                });
        coinsList.getSelectionModel().selectFirst();
//        Window window = coinIcon.getScene().getWindow();
//        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                System.out.println("close!");
//            }
//        });
    }

    private void setCoin(String str) {
        if (NEW_COIN.equals(str)) {
            addNewCoin();
            return;
        }
        WalletAccount wa = cIndx.get(str);
        coinIcon.setImage(Main.getCoinImage(wa.getCoinType()));
        coinAmt.setText(wa.getBalance().toFriendlyString());
    }

    private void addNewCoin() {
        Main.showMessage("В разработке...");
    }

    public void copyAddr(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }


    public void sendCoin(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    public void invoice(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }

    public void exchange(ActionEvent actionEvent) {
        Main.showMessage("В разработке...");
    }
}
