package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.bluelab.payment.JobPayment;
import com.bluelab.payment.Payment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBJobPayment extends DBConnection {

    private static ObservableList<JobPayment> list;
    private static Map<Integer, JobPayment> map;

    private static Consumer<JobPayment> callback = payment -> {
    };

    private static void setCallback(Consumer<JobPayment> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM jobpayment";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                JobPayment p = new JobPayment();
                p.setId(rs.getInt("id"));
                p.setJob(DBJob.get(rs.getInt("job_id")));
                p.setPayment(DBPayment.get(rs.getInt("payment_id")));
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
        map = new HashMap<Integer, JobPayment>();
        list = FXCollections.observableArrayList();

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(JobPayment p) {
//        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
//        EntityTransaction et = null;
//
//        try {
//            et = em.getTransaction();
//            et.begin();
//
//            em.persist(p);
//            et.commit();
//
//            callback.accept(new JobPayment());
//        }
//        catch (Exception ex) {
//            if (et != null) {
//                et.rollback();
//            }
//            ex.printStackTrace();
//        }
//        finally {
//            em.close();
//        }
    }

    public static void update(JobPayment p) {
//        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
//        EntityTransaction et = null;
//
//        try {
//            et = em.getTransaction();
//            et.begin();
//
//            em.merge(p);
//            et.commit();
//
//            callback.accept(new JobPayment());
//        }
//        catch (Exception ex) {
//            if (et != null) {
//                et.rollback();
//            }
//            ex.printStackTrace();
//        }
//        finally {
//            em.close();
//        }
    }

    public static void delete(int id) {
        String query = "DELETE FROM jobpayment WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new JobPayment());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<JobPayment> getList() {
        return list;
    }

    public static JobPayment get(int job_id, int payment_id) {
        for (JobPayment j : list) {
            if (j.getJob().getId() == job_id && j.getPayment().getId() == payment_id) {
                return j;
            }
        }

        return null;
    }

    public static List<JobPayment> list(Payment p) {
        List<JobPayment> l = new ArrayList<JobPayment>();

        list.forEach(j -> {
            if (j.getPayment().getId() == p.getId()) {
                l.add(j);
            }
        });

        return l;
    }
}
