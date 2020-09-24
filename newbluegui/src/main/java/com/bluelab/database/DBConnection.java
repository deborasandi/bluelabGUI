package com.bluelab.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnection {

    private static String url = DBParams.URL.valueOf();
    private static String user = DBParams.USER.valueOf();
    private static String password = DBParams.PASSWORD.valueOf();

    protected static Connection connection;

    public static void init() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DBConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        DBPriceTable.init();
        DBJobType.init();
        DBProductColor.init();
        DBJobPrice.init();
        DBClient.init();
        DBJob.init();
        DBPayment.init();
        DBJobPayment.init();
    }
}
