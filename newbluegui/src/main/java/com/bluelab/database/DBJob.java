package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bluelab.job.Job;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBJob extends DBConnection {

    private static ObservableList<Job> list;
    private static Map<Integer, Job> map;

    private static Consumer<Job> callback = client -> {
    };

    private static void setCallback(Consumer<Job> c) {
        callback = c;
    }

    private static void updateData() {
        map = getMap();

        list.clear();
        list.addAll(map.values());
        list.sort(new Comparator<Job>() {

            @Override
            public int compare(Job o1, Job o2) {
                return o1.getClient().getClientName().compareToIgnoreCase(o2.getClient().getClientName());
            }
        });
    }

    public static void init() {
        list = FXCollections.observableArrayList(new ArrayList<Job>());

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(Job j) {
        String query = "INSERT INTO job (client_id, job_price_id, color_id, qtd, shipping, date, repetition, nocost, paid, total) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getClient().getId());
            stmt.setInt(2, j.getJobPrice().getId());
            stmt.setInt(3, j.getProductColor().getId());
            stmt.setInt(4, j.getQtd());
            stmt.setDouble(5, j.getShipping());
            stmt.setDate(6, j.getDate());
            stmt.setBoolean(7, j.isRepetition());
            stmt.setBoolean(8, j.isNocost());
            stmt.setBoolean(9, j.isPaid());
            stmt.setDouble(10, j.getTotal());

            stmt.execute();
            stmt.close();

            callback.accept(new Job());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    private static Map<Integer, Job> getMap() {
        Map<Integer, Job> list = new HashMap<Integer, Job>();

        String query = "SELECT * FROM job";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Job j = new Job();

                j.setId(rs.getInt("id"));
                j.setClient(DBClient.get(rs.getInt("client_id")));
                j.setJobPrice(DBJobPrice.get(rs.getInt("job_price_id")));
                j.setProductColor(DBProductColor.get(rs.getInt("color_id")));
                j.setQtd(rs.getInt("qtd"));
                j.setShipping(rs.getDouble("shipping"));
                j.setDate(rs.getDate("date", Calendar.getInstance()));
                j.setRepetition(rs.getBoolean("repetition"));
                j.setNocost(rs.getBoolean("nocost"));
                j.setPaid(rs.getBoolean("paid"));
                j.setTotal(rs.getDouble("total"));
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

    public static void update(Job j) {
        String query = "UPDATE job SET client_id = ?, job_price_id = ?, color_id = ?, qtd = ?, shipping = ?, date = ?, repetition = ?, nocost = ?, paid = ?, total = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getClient().getId());
            stmt.setInt(2, j.getJobPrice().getId());
            stmt.setInt(3, j.getProductColor().getId());
            stmt.setInt(4, j.getQtd());
            stmt.setDouble(5, j.getShipping());
            stmt.setDate(6, j.getDate());
            stmt.setBoolean(7, j.isRepetition());
            stmt.setBoolean(8, j.isNocost());
            stmt.setBoolean(9, j.isPaid());
            stmt.setDouble(10, j.getTotal());

            stmt.execute();
            stmt.close();

            callback.accept(new Job());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void update(int id, boolean paid) {
        String query = "UPDATE job SET paid = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setBoolean(1, paid);
            stmt.setInt(2, id);

            stmt.execute();
            stmt.close();

            callback.accept(new Job());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void delete(int id) {
        String query = "DELETE FROM job WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new Job());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<Job> getList() {
        return list;
    }
}
