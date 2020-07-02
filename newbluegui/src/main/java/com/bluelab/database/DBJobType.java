package com.bluelab.database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.bluelab.jobtype.JobType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBJobType extends DBConnection {

    private static ObservableList<JobType> list;
    private static Map<Integer, JobType> map;

    private static Consumer<JobType> callback = client -> {
    };

    private static void setCallback(Consumer<JobType> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM jobtype p ORDER BY p.name";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                JobType j = new JobType();
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
        map = new HashMap<Integer, JobType>();
        list = FXCollections.observableArrayList();

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(JobType j) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
 
        try {
            et = em.getTransaction();
            et.begin();
 
            em.persist(j);
            et.commit();
            
            callback.accept(new JobType());
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static JobType get(String name) {
        for (JobType j : list) {
            if (j.getName().equals(name))
                return j;
        }

        return null;
    }

    public static void delete(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        JobType p = null;
 
        try {
            et = em.getTransaction();
            et.begin();
            p = em.find(JobType.class, id);
            em.remove(p);
            et.commit();
            
            callback.accept(new JobType());
            
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

    public static void update(JobType j) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        
        try {
            et = em.getTransaction();
            et.begin();
 
            em.merge(j);
            et.commit();
            
            callback.accept(new JobType());

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

    public static ObservableList<JobType> getList() {
        return list;
    }

    public static JobType get(int id) {
        return map.get(id);
    }
}
