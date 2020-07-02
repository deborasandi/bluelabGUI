package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.bluelab.client.Client;
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
                p.setDate(rs.getDate("date", Calendar.getInstance()));
                p.setValue(rs.getDouble("value"));

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
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            em.persist(p);
            et.commit();

            callback.accept(new Payment());
        }
        catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public static void update(Payment p) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            em.merge(p);
            et.commit();

            callback.accept(new Payment());
        }
        catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public static void delete(int id) {
        String query = "DELETE FROM payment WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

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
