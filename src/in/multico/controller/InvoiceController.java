package in.multico.controller;

import com.coinomi.core.wallet.AbstractAddress;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletPocketHD;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import in.multico.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class InvoiceController extends ControllerBased {

    @FXML public Label coinSymbol;
    @FXML public TextField amt;
    @FXML public ImageView qr;
    @FXML public ComboBox addr;
    private String currAddr;
    private String currAmt;
    private String currCoin;

    @Override
    public String getLayout() {
        return "invoice.fxml";
    }

    @Override
    protected void refresh() {

    }

    public void back(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    private void repaintQR() {
        StringBuilder sb = new StringBuilder();
        sb.append(currCoin).append(":").append(currAddr);
        if (currAmt != null) {
            sb.append("?amount=").append(currAmt);
        }
        int width = 360;
        int height = 360;
        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = new QRCodeWriter().encode(sb.toString(), BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        qr.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }

    public void setCurrWa(WalletAccount currWa) {
        currCoin = currWa.getCoinType().getName();
        if (currCoin.contains(" ")) currCoin = currCoin.split(" ")[0];
        currCoin = currCoin.toLowerCase();
        currAddr = Main.getAddr(currWa);
        repaintQR();
        for (AbstractAddress a :((WalletPocketHD) currWa).getUsedAddresses()) {
            if (a.toString().equals(currAddr)) continue;
            addr.getItems().add(a.toString());
        }
        addr.getItems().add(currAddr);
        addr.getSelectionModel().selectLast();
        addr.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currAddr = (String) newValue;
                repaintQR();
            }
        });
        coinSymbol.setText(currWa.getCoinType().getSymbol());
        amt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double val = 0.0;
                try {
                    val = Double.parseDouble(newValue);
                } catch (Exception ignored) {}
                if (val > 0) {
                    currAmt = newValue;
                } else {
                    currAmt = null;
                }
                repaintQR();
            }
        });
    }
}
