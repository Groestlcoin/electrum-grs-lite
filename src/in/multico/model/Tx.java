package in.multico.model;

import com.coinomi.core.wallet.WalletAccount;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vp
 * on 06.03.16.
 */
public class Tx {
    private String date;
    private String amt;
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

    private String calcSr(Transaction t, WalletAccount wa) {
        NetworkParameters np = wa.getCoinType().isTestnet() ? TestNet3Params.get() : MainNetParams.get();
        Set<String> oppo = new HashSet<>();
        if (t.getValueSentToMe(wa).getValue() > 0) {
            for (TransactionInput in : t.getInputs()) {
                oppo.add(in.getFromAddress().toString());
            }
        } else {
            for (TransactionOutput out : t.getOutputs()) {
                Address fromP2PKHScript = out.getAddressFromP2PKHScript(np);
                if (fromP2PKHScript != null) {
                    oppo.add(fromP2PKHScript.toString());
                }
                Address fromP2SH = out.getAddressFromP2SH(np);
                if (fromP2SH != null) {
                    oppo.add(fromP2SH.toString());
                }
            }
        }
        return oppo.toString();
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
