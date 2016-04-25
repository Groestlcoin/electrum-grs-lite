package in.multico.model;

import com.coinomi.core.wallet.WalletAccount;
import org.bitcoinj.core.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 */
public class Tx {
    private String date;
    private String amt;
    private String usdAmt = "0.0";
    private int confirms;
    private String sr;

    public Tx(Transaction t, WalletAccount wa) {
        this.date = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(t.getUpdateTime());
        this.amt = new BigDecimal((double) t.getValue(wa).getValue() / (double) wa.getCoinType().oneCoin().getValue()).setScale(wa.getCoinType().getUnitExponent(), BigDecimal.ROUND_HALF_UP).toPlainString() + " " + wa.getCoinType().getSymbol();
        this.confirms = t.getConfidence().getDepthInBlocks();
        this.sr = calcSr(t, wa);
    }

    public int getConfirms() {
        return confirms;
    }

    public String getDate() {
        return date;
    }

    public String getAmt() {
        return amt;
    }

    public String getSr() {
        return sr.contains(" ") ? sr : sr.replace("[", "").replace("]", "");
    }

    public String getUsdAmt() {
        return usdAmt;
    }

    public void setUsdAmt(double price) {
        try {
            double damt = Double.parseDouble(amt.split(" ")[0]);
            usdAmt = new BigDecimal(damt * price).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        } catch (Exception ne) {
            usdAmt = "0.0";
        }
    }

    private String calcSr(Transaction t, WalletAccount wa) {
        Set<String> oppo = new HashSet<>();
        final Coin value = t.getValue(wa);
        List<Address> addrs = value.signum() >= 0 ? getReceiveFromAddresses(t, wa) : getSendToAddresses(t, wa);
        for (Address a : addrs) {
            oppo.add(a.toString());
        }
        return oppo.toString();
    }

    public static List<Address> getSendToAddresses(Transaction tx, WalletAccount pocket) {
        List<Address> addresses = new ArrayList<Address>();
        for (final TransactionOutput output : tx.getOutputs()) {
            try {
                if (!output.isMine(pocket)) {
                    addresses.add(output.getScriptPubKey().getToAddress(pocket.getCoinType()));
                }
            } catch (final ScriptException x) { /* ignore this output */ }
        }
        return addresses;
    }

    public static List<Address> getReceiveToAddresses(Transaction tx, WalletAccount pocket) {
        List<Address> addresses = new ArrayList<Address>();
        for (final TransactionOutput output : tx.getOutputs()) {
            try {
                if (output.isMine(pocket)) {
                    addresses.add(output.getScriptPubKey().getToAddress(pocket.getCoinType()));
                }
            } catch (final ScriptException x) { /* ignore this output */ }
        }
        return addresses;
    }

    public static List<Address> getReceiveFromAddresses(Transaction tx, WalletAccount pocket) {
        List<Address> addresses = new ArrayList<Address>();
        for (final TransactionInput input: tx.getInputs()) {
            try {
                 addresses.add(input.getFromAddress());
            } catch (final ScriptException x) { /* ignore this output */ }
        }
        return addresses;
    }

    public static void sort(List<Tx> txes) {
        Collections.sort(txes, new Comparator<Tx>() {
            @Override
            public int compare(Tx o1, Tx o2) {
                if (o1.confirms <= o2.confirms) return -1;
                return 1;
            }
        });
    }
}
