package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.bluelab.jobprice.JobPrice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBJobPrice extends DBConnection {

    private static ObservableList<JobPrice> list;
    private static Map<Integer, JobPrice> map;

    private static Consumer<JobPrice> callback = client -> {
    };

    private static void setCallback(Consumer<JobPrice> c) {
        callback = c;
    }

    public static void updateData() {
        String query = "SELECT * FROM jobprice";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                JobPrice p = new JobPrice();
                p.setId(rs.getInt("id"));
                p.setPriceTable(DBPriceTable.get(rs.getInt("pricetable_id")));
                p.setJobType(DBJobType.get(rs.getInt("jobtype_id")));
                p.setPrice(rs.getDouble("price"));

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
        map = new HashMap<Integer, JobPrice>();
        list = FXCollections.observableArrayList();
        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(List<JobPrice> list) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
 
        try {
            et = em.getTransaction();
            et.begin();
 
            for (JobPrice p : list) {
                em.persist(p);
            }
            et.commit();
            
            callback.accept(new JobPrice());
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void insert(JobPrice p) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
 
        try {
            et = em.getTransaction();
            et.begin();
 
            em.persist(p);
            et.commit();
            
            callback.accept(new JobPrice());
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

       
    public static void delete(int id) {
        String query = "DELETE FROM jobprice WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new JobPrice());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void update(JobPrice p) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        
        try {
            et = em.getTransaction();
            et.begin();
 
            em.merge(p);
            et.commit();
            
            callback.accept(new JobPrice());

            DBJobPrice.updateData();
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static ObservableList<JobPrice> getList() {
        return list;
    }

    public static JobPrice get(int id) {
        return map.get(id);
    }
}
