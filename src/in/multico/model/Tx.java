package in.multico.model;

import com.coinomi.core.wallet.WalletAccount;
import org.bitcoinj.core.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
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
        this.amt = t.getValue(wa).toPlainString() + " " + wa.getCoinType().getSymbol();
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
        return sr;
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
        for (Address a : getAddresses(t, wa, value.signum() >= 0)) {
            oppo.add(a.toString());
        }
        return oppo.toString();
    }

    @CheckForNull
    public static List<Address> getAddresses(@Nonnull final Transaction tx, @Nonnull final WalletAccount pocket, boolean toMe) {
        List<Address> addresses = new ArrayList<Address>();
        for (final TransactionOutput output : tx.getOutputs()) {
            try {
                if (output.isMine(pocket) == toMe) {
                    addresses.add(output.getScriptPubKey().getToAddress(pocket.getCoinType()));
                }
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
