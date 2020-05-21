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
        map = getMap();

        list.clear();
        list.addAll(map.values());
        list.sort(new Comparator<Client>() {

            @Override
            public int compare(Client o1, Client o2) {
                return o1.getClientName().compareToIgnoreCase(o2.getClientName());
            }
        });
    }

    public static void init() {
        list = FXCollections.observableArrayList(new ArrayList<Client>()); 
        
        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(Client c) {
        String query = "INSERT INTO client (name, cpf, tel, cel, resp_name, resp_cpf, resp_tel, resp_cel, address, number, compl, "
                + "city, state, cep, price_table_id) VALUES" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, c.getClientName());
            stmt.setString(2, c.getClientCpf());
            stmt.setString(3, c.getClientTel());
            stmt.setString(4, c.getClientCel());
            stmt.setString(5, c.getRespName());
            stmt.setString(6, c.getRespCpf());
            stmt.setString(7, c.getRespTel());
            stmt.setString(8, c.getRespCel());
            stmt.setString(9, c.getAddress());
            stmt.setInt(10, c.getNumber());
            stmt.setString(11, c.getComplement());
            stmt.setString(12, c.getCity());
            stmt.setString(13, c.getState());
            stmt.setString(14, c.getCep());
            stmt.setInt(15, c.getPriceTable().getId());

            stmt.execute();
            stmt.close();

            callback.accept(new Client());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    private static Map<Integer, Client> getMap() {
        Map<Integer, Client> list = new HashMap<Integer, Client>();

        String query = "SELECT * FROM client";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setClientName(rs.getString("name"));
                c.setClientCpf(rs.getString("cpf"));
                c.setClientTel(rs.getString("tel"));
                c.setClientCel(rs.getString("cel"));
                c.setRespName(rs.getString("resp_name"));
                c.setRespCpf(rs.getString("resp_cpf"));
                c.setRespTel(rs.getString("resp_tel"));
                c.setRespCel(rs.getString("resp_cel"));
                c.setAddress(rs.getString("address"));
                c.setNumber(rs.getInt("number"));
                c.setComplement(rs.getString("compl"));
                c.setCity(rs.getString("city"));
                c.setState(rs.getString("state"));
                c.setCep(rs.getString("cep"));
                c.setPriceTable(DBPriceTable.get(rs.getInt("price_table_id")));

                list.put(c.getId(), c);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static void update(Client c) {
        String query = "UPDATE client SET name = ?, cpf = ?, tel = ?, cel = ?, resp_name = ?, resp_cpf = ?, resp_tel = ?, resp_cel = ?, "
                + "address = ?, number = ?, compl = ?, city = ?, state = ?, cep = ?, price_table_id = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, c.getClientName());
            stmt.setString(2, c.getClientCpf());
            stmt.setString(3, c.getClientTel());
            stmt.setString(4, c.getClientCel());
            stmt.setString(5, c.getRespName());
            stmt.setString(6, c.getRespCpf());
            stmt.setString(7, c.getRespTel());
            stmt.setString(8, c.getRespCel());
            stmt.setString(9, c.getAddress());
            stmt.setInt(10, c.getNumber());
            stmt.setString(11, c.getComplement());
            stmt.setString(12, c.getCity());
            stmt.setString(13, c.getState());
            stmt.setString(14, c.getCep());
            stmt.setInt(15, c.getPriceTable().getId());
            stmt.setInt(16, c.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new Client());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
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