package com.bluelab.database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
 
        try {
            et = em.getTransaction();
            et.begin();
 
            em.persist(p);
            et.commit();
            
            callback.accept(new PriceTable());
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
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
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        
        try {
            et = em.getTransaction();
            et.begin();
 
            em.merge(p);
            et.commit();
            
            callback.accept(new PriceTable());

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

    public static void delete(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        PriceTable p = null;
 
        try {
            et = em.getTransaction();
            et.begin();
            p = em.find(PriceTable.class, id);
            em.remove(p);
            et.commit();
            
            callback.accept(new PriceTable());
            
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

    public static ObservableList<PriceTable> list() {
        return list;
    }

    public static PriceTable get(int id) {
        return map.get(id);
    }

}
