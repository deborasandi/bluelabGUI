package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bluelab.payment.Payment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBPayment extends DBConnection {

    private static ObservableList<Payment> list;
    private static Map<Integer, Payment> map;

    private static Consumer<Payment> callback = payment -> {
    };

    private static void setCallback(Consumer<Payment> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM payment c ORDER BY c.date";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setClient(DBClient.get(rs.getInt("client_id")));
                p.setJob(DBJob.get(rs.getInt("job_id")));
                p.setDate(rs.getDate("date", Calendar.getInstance()));

                list.add(p);
                
                map.put(p.getId(), p);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init() {
        map = new HashMap<Integer, Payment>();
        list = FXCollections.observableArrayList();

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(Payment p) {
        String query = "insert into payment (client_id, job_id, date) values (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, p.getClient().getId());
            stmt.setInt(2, p.getJob().getId());
            stmt.setDate(3, p.getDate());

            stmt.execute();
            stmt.close();

            callback.accept(new Payment());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void update(Payment p) {
        String query = "UPDATE payment SET client_id = ?, job_id = ?, date = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, p.getClient().getId());
            stmt.setInt(2, p.getJob().getId());
            stmt.setDate(3, p.getDate());
            stmt.setInt(4, p.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new Payment());
            
            DBJobPrice.updateData();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void delete(int jobId) {
        String query = "DELETE FROM payment WHERE job_id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, jobId);

            stmt.execute();
            stmt.close();

            callback.accept(new Payment());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<Payment> getList() {
        return list;
    }

    public static Payment get(int id) {
        return map.get(id);
    }
}
