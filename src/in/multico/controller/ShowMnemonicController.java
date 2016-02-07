package in.multico.controller;

import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 09:38
 */
public class ShowMnemonicController {

    public Label w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w17, w18;
    private boolean nextCheck = false;
    private List<String> mm;

    public void setNextCheck() {
        this.nextCheck = true;
    }

    @FXML
    private void next(ActionEvent event) {
        if (nextCheck) {
            Main.refreshLayout(event, "check_mnemonic.fxml", new ShowListener() {
                @Override
                public void onShow(Object controller) {
                    ((CheckMnemonicController)controller).setMnemonic(mm);
                }
            });
        } else {
            Main.refreshLayout(event, "main.fxml");
        }
    }

    public void setMnemonic(List<String> mm) {
        this.mm = mm;
        w1.setText(mm.get(0));
        w2.setText(mm.get(1));
        w3.setText(mm.get(2));
        w4.setText(mm.get(3));
        w5.setText(mm.get(4));
        w6.setText(mm.get(5));
        w7.setText(mm.get(6));
        w8.setText(mm.get(7));
        w9.setText(mm.get(8));
        w10.setText(mm.get(9));
        w11.setText(mm.get(10));
        w12.setText(mm.get(11));
        w13.setText(mm.get(12));
        w14.setText(mm.get(13));
        w15.setText(mm.get(14));
        w16.setText(mm.get(15));
        w17.setText(mm.get(16));
        w18.setText(mm.get(17));
    }

}
