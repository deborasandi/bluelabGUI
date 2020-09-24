package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bluelab.pricetable.PriceTable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBPriceTable extends DBConnection {

    private static ObservableList<PriceTable> list;
    private static Map<Integer, PriceTable> map;

    private static Consumer<PriceTable> callback = pricetable -> {
    };

    private static void setCallback(Consumer<PriceTable> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM pricetable p ORDER BY p.name";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                PriceTable j = new PriceTable();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));
                
                list.add(j);
                
                map.put(j.getId(), j);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init() {
        map = new HashMap<Integer, PriceTable>();
        list = FXCollections.observableArrayList();
        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(PriceTable p) {
        String query = "insert into price_table (name) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, p.getName());

            stmt.execute();
            stmt.close();

            callback.accept(new PriceTable());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static PriceTable get(String name) {
        for (PriceTable priceTable : list) {
            if (priceTable.getName().equals(name))
                return priceTable;
        }

        return null;
    }

    public static void update(PriceTable p) {
        String query = "UPDATE price_table SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new PriceTable());
            
            DBJobPrice.updateData();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void delete(int id) {
        String query = "DELETE FROM price_table WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new PriceTable());
            
            DBJobPrice.updateData();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<PriceTable> list() {
        return list;
    }

    public static PriceTable get(int id) {
        return map.get(id);
    }

}
