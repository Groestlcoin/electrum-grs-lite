package in.multico.controller;

import in.multico.Main;
import in.multico.listener.CloseListener;
import in.multico.listener.ShowListener;
import in.multico.tool.Tool;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class CheckMnemonicController extends ControllerBased {

    public ComboBox w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w17, w18;
    private List<String> mm;

    @FXML
    private void next(ActionEvent event) {
        try {
            if (mm != null) {
                if (!mm.get(0).equals(w1.getValue())) showErr(w1.getValue(), event);
                if (!mm.get(1).equals(w2.getValue())) showErr(w2.getValue(), event);
                if (!mm.get(2).equals(w3.getValue())) showErr(w3.getValue(), event);
                if (!mm.get(3).equals(w4.getValue())) showErr(w4.getValue(), event);
                if (!mm.get(4).equals(w5.getValue())) showErr(w5.getValue(), event);
                if (!mm.get(5).equals(w6.getValue())) showErr(w6.getValue(), event);
                if (!mm.get(6).equals(w7.getValue())) showErr(w7.getValue(), event);
                if (!mm.get(7).equals(w8.getValue())) showErr(w8.getValue(), event);
                if (!mm.get(8).equals(w9.getValue())) showErr(w9.getValue(), event);
                if (!mm.get(9).equals(w10.getValue())) showErr(w10.getValue(), event);
                if (!mm.get(10).equals(w11.getValue())) showErr(w11.getValue(), event);
                if (!mm.get(11).equals(w12.getValue())) showErr(w12.getValue(), event);
                if (!mm.get(12).equals(w13.getValue())) showErr(w13.getValue(), event);
                if (!mm.get(13).equals(w14.getValue())) showErr(w14.getValue(), event);
                if (!mm.get(14).equals(w15.getValue())) showErr(w15.getValue(), event);
                if (!mm.get(15).equals(w16.getValue())) showErr(w16.getValue(), event);
                if (!mm.get(16).equals(w17.getValue())) showErr(w17.getValue(), event);
                if (!mm.get(17).equals(w18.getValue())) showErr(w18.getValue(), event);
            } else {
                mm = new ArrayList<>();
                mm.add(w1.getValue().toString());
                mm.add(w2.getValue().toString());
                mm.add(w3.getValue().toString());
                mm.add(w4.getValue().toString());
                mm.add(w5.getValue().toString());
                mm.add(w6.getValue().toString());
                mm.add(w7.getValue().toString());
                mm.add(w8.getValue().toString());
                mm.add(w9.getValue().toString());
                mm.add(w10.getValue().toString());
                mm.add(w11.getValue().toString());
                mm.add(w12.getValue().toString());
                mm.add(w13.getValue().toString());
                mm.add(w14.getValue().toString());
                mm.add(w15.getValue().toString());
                mm.add(w16.getValue().toString());
                mm.add(w17.getValue().toString());
                mm.add(w18.getValue().toString());
            }
            Main.refreshLayout(event, new SetPasswordController().getLayout(), new ShowListener() {
                @Override
                public void onShow(Object controller) {
                    ((SetPasswordController) controller).setMnemonic(mm, null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMnemonic(List<String> mm) {
        this.mm = mm;
        w1.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(0))));
        w2.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(1))));
        w3.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(2))));
        w4.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(3))));
        w5.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(4))));
        w6.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(5))));
        w7.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(6))));
        w8.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(7))));
        w9.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(8))));
        w10.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(9))));
        w11.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(10))));
        w12.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(11))));
        w13.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(12))));
        w14.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(13))));
        w15.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(14))));
        w16.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(15))));
        w17.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(16))));
        w18.setItems(FXCollections.observableList(Tool.getMnemonicWorList(mm == null ? null : mm.get(17))));
       // TODO: stub
//        w1.getTextbox().setText(mm.get(0));
//        w2.getTextbox().setText(mm.get(1));
//        w3.getTextbox().setText(mm.get(2));
//        w4.getTextbox().setText(mm.get(3));
//        w5.getTextbox().setText(mm.get(4));
//        w6.getTextbox().setText(mm.get(5));
//        w7.getTextbox().setText(mm.get(6));
//        w8.getTextbox().setText(mm.get(7));
//        w9.getTextbox().setText(mm.get(8));
//        w10.getTextbox().setText(mm.get(9));
//        w11.getTextbox().setText(mm.get(10));
//        w12.getTextbox().setText(mm.get(11));
//        w13.getTextbox().setText(mm.get(12));
//        w14.getTextbox().setText(mm.get(13));
//        w15.getTextbox().setText(mm.get(14));
//        w16.getTextbox().setText(mm.get(15));
//        w17.getTextbox().setText(mm.get(16));
//        w18.getTextbox().setText(mm.get(17));
    }

    private void showErr(Object word, final ActionEvent event) throws Exception {
        Main.showMessage(Tool.getLocString("err_data"), new CloseListener() {
            @Override
            public void onClose() {
                Main.refreshLayout(event, new ShowMnemonicController().getLayout(), new ShowListener() {
                    @Override
                    public void onShow(Object controller) {
                        ShowMnemonicController c = (ShowMnemonicController) controller;
                        c.setMnemonic(mm);
                        c.setNextCheck();
                    }
                });
            }
        });
        throw new Exception("invalid word: " + (word == null ? "" : word.toString()));
    }

    @Override
    public String getLayout() {
        return "check_mnemonic.fxml";
    }

    @Override
    protected void refresh() {

    }
}
