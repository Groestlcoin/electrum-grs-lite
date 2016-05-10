package in.multico.model;

import com.coinomi.core.coins.Value;
import com.coinomi.core.coins.families.NxtFamily;
import com.coinomi.core.wallet.AbstractAddress;
import com.coinomi.core.wallet.AbstractTransaction;
import com.coinomi.core.wallet.AbstractWallet;

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

    public Tx(AbstractTransaction t, AbstractWallet wa) {
        this.date = t.getTimestamp() > 0 ? new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(time2mills(t.getTimestamp())) : "";
        this.amt = new BigDecimal((double) t.getValue(wa).getValue() / (double) wa.getCoinType().oneCoin().getValue()).setScale(wa.getCoinType().getUnitExponent(), BigDecimal.ROUND_HALF_UP).toPlainString() + " " + wa.getCoinType().getSymbol();
        this.confirms = t.getDepthInBlocks();
        this.sr = calcSr(t, wa);
    }

    private long time2mills(long t) {
        if (t < 1000000000000L) return t * 1000L;
        else return t;
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

    private String calcSr(AbstractTransaction t, AbstractWallet wa) {
        Set<String> oppo = new HashSet<>();
        Value value = t.getValue(wa);
        List<AbstractAddress> addrs = value.signum() >= 0 ? getReceivedFromAddresses(t, wa) : getSendToAddresses(t, wa);
        for (AbstractAddress a : addrs) {
            oppo.add(a.toString());
        }
        return oppo.toString();
    }

    private List<AbstractAddress> getReceivedFromAddresses(AbstractTransaction tx, AbstractWallet pocket) {
        List<AbstractAddress> addresses = new ArrayList<AbstractAddress>();
        if (pocket.getCoinType() instanceof NxtFamily) {
            return tx.getReceivedFrom();
        } else {
            for (AbstractTransaction.AbstractOutput output : tx.getSentTo()) {
                if (!pocket.isAddressMine(output.getAddress())) continue;
                addresses.add(output.getAddress());
            }
        }
        return addresses;
    }

    private List<AbstractAddress> getSendToAddresses(AbstractTransaction tx, AbstractWallet pocket) {
        List<AbstractAddress> addresses = new ArrayList<AbstractAddress>();
        for (final AbstractTransaction.AbstractOutput output : tx.getSentTo()) {
            if (pocket.isAddressMine(output.getAddress())) continue;
            addresses.add(output.getAddress());
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
