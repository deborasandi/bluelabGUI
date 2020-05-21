package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
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
        map = getMap();

        list.clear();
        list.addAll(map.values());
        list.sort(new Comparator<ProductColor>() {

            @Override
            public int compare(ProductColor o1, ProductColor o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    public static void init() {
        list = FXCollections.observableArrayList(new ArrayList<ProductColor>());

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(ProductColor p) {
        String query = "insert into product_color (name) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, p.getName());

            stmt.execute();
            stmt.close();

            callback.accept(new ProductColor());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static Map<Integer, ProductColor> getMap() {
        Map<Integer, ProductColor> list = new HashMap<Integer, ProductColor>();

        String query = "SELECT * FROM product_color";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                ProductColor j = new ProductColor();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));

                list.put(j.getId(), j);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static ProductColor get(String name) {
        ProductColor j = null;

        String query = "SELECT * FROM product_color WHERE name = \"" + name + "\";";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                j = new ProductColor();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return j;
    }

    public static void delete(int id) {
        String query = "DELETE FROM product_color WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new ProductColor());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void update(ProductColor j) {
        String query = "UPDATE product_color SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());
            stmt.setInt(2, j.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new ProductColor());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<ProductColor> getList() {
        return list;
    }

    public static ProductColor get(int id) {
        return map.get(id);
    }
}
