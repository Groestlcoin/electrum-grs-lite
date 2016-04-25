package in.multico;

import java.io.*;
import java.util.Properties;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 */

public class Settings {

    private static final String propFile = "pref.properties";
    public static final String PROP_FRESH_ADDR = "fresh_addr";
    public static final String PROP_SPEND_UNCOIFIRM = "spend_unconfirm";
    private Properties props;
    private static Settings instanse;

    public void setAlwaysRefreshAddr(boolean refresh) {
        props.put(PROP_FRESH_ADDR, refresh ? "1" : "0");
        save();
    }

    public boolean isAlwaysRefreshAddr() {
        return "1".equals(props.getProperty(PROP_FRESH_ADDR, "1"));
    }

    public void setAllowSpendUnconfirmed(boolean allow) {
        props.put(PROP_SPEND_UNCOIFIRM, allow ? "1" : "0");
        save();
    }

    public boolean isAllowSpendUnconfirmed() {
        return "1".equals(props.getProperty(PROP_SPEND_UNCOIFIRM, "1"));
    }

    public static Settings getInstanse() {
        if (instanse == null) instanse = new Settings();
        return instanse;
    }

    private Settings() {
        try {
            props = new Properties();
            props.load(new FileInputStream(propFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(new File(propFile)));
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
