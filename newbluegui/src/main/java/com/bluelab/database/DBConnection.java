package com.bluelab.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bluelab.data.BlueData;


public class DBConnection {

    private static String url = "jdbc:mysql://bluelabdb.c7tsyx37zboh.us-east-2.rds.amazonaws.com:3306/bluelabdb?useTimezone=true&serverTimezone=UTC";
    private static String user = "bluelabadmin";
    private static String password = "bluelabadmin";

    protected static Connection connection;
    
    public static void init() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DBConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        DBPriceTable.updateList();
        DBJobType.updateList();
        DBProductColor.updateList();
        DBJobPrice.updateList();
        DBClient.init();
        DBJob.updateList();
        
        BlueData.init();
    }
}
