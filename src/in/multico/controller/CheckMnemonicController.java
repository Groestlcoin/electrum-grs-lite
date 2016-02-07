package in.multico.controller;

import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import np.com.ngopal.control.AutoFillTextBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 09:38
 */
public class CheckMnemonicController implements Initializable{

    public AutoFillTextBox w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w17, w18;
    private List<String> mm;

    @FXML
    private void next(ActionEvent event) {
        if (mm != null) {
            if (!mm.get(0).equals(w1.getText())) showErr();
            if (!mm.get(1).equals(w2.getText())) showErr();
            if (!mm.get(2).equals(w3.getText())) showErr();
            if (!mm.get(3).equals(w4.getText())) showErr();
            if (!mm.get(4).equals(w5.getText())) showErr();
            if (!mm.get(5).equals(w6.getText())) showErr();
            if (!mm.get(6).equals(w7.getText())) showErr();
            if (!mm.get(7).equals(w8.getText())) showErr();
            if (!mm.get(8).equals(w9.getText())) showErr();
            if (!mm.get(9).equals(w10.getText())) showErr();
            if (!mm.get(10).equals(w11.getText())) showErr();
            if (!mm.get(11).equals(w12.getText())) showErr();
            if (!mm.get(12).equals(w13.getText())) showErr();
            if (!mm.get(13).equals(w14.getText())) showErr();
            if (!mm.get(14).equals(w15.getText())) showErr();
            if (!mm.get(15).equals(w16.getText())) showErr();
            if (!mm.get(16).equals(w17.getText())) showErr();
            if (!mm.get(17).equals(w18.getText())) showErr();
        } else {
            mm = new ArrayList<>();
            mm.set(0, w1.getText());
            mm.set(0, w2.getText());
            mm.set(0, w3.getText());
            mm.set(0, w4.getText());
            mm.set(0, w5.getText());
            mm.set(0, w6.getText());
            mm.set(0, w7.getText());
            mm.set(0, w8.getText());
            mm.set(0, w9.getText());
            mm.set(0, w10.getText());
            mm.set(0, w11.getText());
            mm.set(0, w12.getText());
            mm.set(0, w13.getText());
            mm.set(0, w14.getText());
            mm.set(0, w15.getText());
            mm.set(0, w16.getText());
            mm.set(0, w17.getText());
            mm.set(0, w18.getText());
        }
        Main.refreshLayout(event, "set_password.fxml", new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((SetPasswordController)controller).setMnemonic(mm);
            }
        });
    }

    public void setMnemonic(List<String> mm) {
        this.mm = mm;
       // TODO: stub
        w1.getTextbox().setText(mm.get(0));
        w2.getTextbox().setText(mm.get(1));
        w3.getTextbox().setText(mm.get(2));
        w4.getTextbox().setText(mm.get(3));
        w5.getTextbox().setText(mm.get(4));
        w6.getTextbox().setText(mm.get(5));
        w7.getTextbox().setText(mm.get(6));
        w8.getTextbox().setText(mm.get(7));
        w9.getTextbox().setText(mm.get(8));
        w10.getTextbox().setText(mm.get(9));
        w11.getTextbox().setText(mm.get(10));
        w12.getTextbox().setText(mm.get(11));
        w13.getTextbox().setText(mm.get(12));
        w14.getTextbox().setText(mm.get(13));
        w15.getTextbox().setText(mm.get(14));
        w16.getTextbox().setText(mm.get(15));
        w17.getTextbox().setText(mm.get(16));
        w18.getTextbox().setText(mm.get(17));
    }

    private void showErr() {
        Main.showMessage("Ошибка! Проверьте введённые данные.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> mnemonicWorList = Main.getInstance().getMnemonicWorList();
        ObservableList<String> ol = FXCollections.observableList(mnemonicWorList);
        w1.setData(ol);
        w2.setData(ol);
        w3.setData(ol);
        w4.setData(ol);
        w5.setData(ol);
        w6.setData(ol);
        w7.setData(ol);
        w8.setData(ol);
        w9.setData(ol);
        w10.setData(ol);
        w11.setData(ol);
        w12.setData(ol);
        w13.setData(ol);
        w14.setData(ol);
        w15.setData(ol);
        w16.setData(ol);
        w17.setData(ol);
        w18.setData(ol);
    }
}
