package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.bluelab.client.Client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBClient extends DBConnection {

    private static ObservableList<Client> list;
    private static Map<Integer, Client> map;

    private static Consumer<Client> callback = client -> {
    };

    private static void setCallback(Consumer<Client> c) {
        callback = c;
    }

    private static void updateData() {
        String query = "SELECT * FROM client c ORDER BY c.name";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            list.clear();
            map.clear();

            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setCpf(rs.getString("cpf"));
                c.setTel(rs.getString("tel"));
                c.setCel(rs.getString("cel"));
                c.setRespName(rs.getString("respname"));
                c.setRespCpf(rs.getString("respcpf"));
                c.setRespTel(rs.getString("resptel"));
                c.setRespCel(rs.getString("respcel"));
                c.setAddress(rs.getString("address"));
                c.setNumber(rs.getInt("number"));
                c.setCompl(rs.getString("compl"));
                c.setDistrict(rs.getString("district"));
                c.setCity(rs.getString("city"));
                c.setState(rs.getString("state"));
                c.setCep(rs.getString("cep"));
                c.setPriceTable(DBPriceTable.get(rs.getInt("pricetable_id")));
                c.setEmail(rs.getString("email"));

                list.add( c);
                
                map.put(c.getId(), c);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init() {
        map = new HashMap<Integer, Client>();
        list = FXCollections.observableArrayList();

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(Client c) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            em.persist(c);
            et.commit();

            callback.accept(new Client());
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

    public static void update(Client c) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            em.merge(c);
            et.commit();

            callback.accept(new Client());
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
        String query = "DELETE FROM client WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new Client());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<Client> getList() {
        return list;
    }

    public static Client get(int id) {
        return map.get(id);
    }
}
