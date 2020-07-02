package com.bluelab.database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bluelab.productcolor.ProductColor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBProductColor extends DBConnection {

    private static ObservableList<ProductColor> list;
    private static Map<Integer, ProductColor> map;

    private static Consumer<ProductColor> callback = client -> {
    };

    private static void setCallback(Consumer<ProductColor> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM productcolor p ORDER BY p.name";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                ProductColor j = new ProductColor();
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
        map = new HashMap<Integer, ProductColor>();
        list = FXCollections.observableArrayList();

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static ProductColor get(String name) {
        for (ProductColor productColor : list) {
            if(productColor.getName().equals(name))
                return productColor;
        }
        
        return null;
    }

    public static ObservableList<ProductColor> getList() {
        return list;
    }

    public static ProductColor get(int id) {
        return map.get(id);
    }
}
