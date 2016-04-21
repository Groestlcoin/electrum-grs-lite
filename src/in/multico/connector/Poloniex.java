package in.multico.connector;

import org.json.JSONObject;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class Poloniex extends ConnectorBased {

    public static void getTicker(final RespListener pl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = get("https://poloniex.com/public?command=returnTicker");
                    JSONObject jo = new JSONObject(s);
                    pl.onResp(jo);
                } catch (Exception e) {
                    e.printStackTrace();
                    pl.onResp(new JSONObject());
                }
            }
        }).start();
    }

    public interface RespListener {
        void onResp(JSONObject ticker);
    }
}
