package com.vvmarkets.configs;

import com.vvmarkets.core.Utils;
import com.vvmarkets.utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RemoteConfig {
    public enum ConfigType {
        GENERAL("general"),
        PRINTER("printer"),
        PRINTER_SECOND("printerSecond");

        private String type;

        ConfigType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    public enum ConfigSubType {
        NAME("name"),
        STORE_NAME("storeName"),
        LABEL_ROW("labelRow"),
        LABEL_FOOTER("labelFooter"),
        LABEL_HEADER("labelHeader"),
        LABEL_WIDTH("labelWidth");

        private String type;

        ConfigSubType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    public static String getConfig(ConfigType configType, ConfigSubType subType) {
        String res = null;

        try (Connection c = db.getConnection()) {
            ResultSet rs;
            try (PreparedStatement ps = c.prepareStatement(
                    "select cco.value " +
                            "from cash_config_options cco " +
                            "join cash_configs cc on cco.cash_config_id = cc.id " +
                            "where cc.name = ? and cco.name = ? limit 1"
            )) {
                ps.setString(1, configType.getType());
                ps.setString(2, subType.getType());
                rs = ps.executeQuery();
                if (rs.next()) {
                    res = rs.getString(1);
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot get config for cash");
        }

        return res;
    }
}
