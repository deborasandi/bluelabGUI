package com.bluelab.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        map = getMap();

        list.clear();
        list.addAll(map.values());
        list.sort(new Comparator<JobPrice>() {

            @Override
            public int compare(JobPrice o1, JobPrice o2) {
                return o1.getPriceTable().getName().compareToIgnoreCase(o2.getPriceTable().getName());
            }
        });
    }

    public static void init() {
        list = FXCollections.observableArrayList(new ArrayList<JobPrice>());

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(List<JobPrice> list) {
        String query = "insert into job_price (price_table_id, job_type_id, price) values (?, ?, ?)";

        for (int i = 1; i < list.size(); i++) {
            query += ",(?, ?, ?)";
        }

        query += ";";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            int i = 0;
            for (JobPrice j : list) {
                stmt.setInt(i + 1, j.getPriceTable().getId());
                stmt.setInt(i + 2, j.getJobType().getId());
                stmt.setDouble(i + 3, j.getPrice());

                i += 3;
            }

            stmt.execute();
            stmt.close();

            callback.accept(new JobPrice());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void insert(JobPrice jp) {
        String query = "insert into job_price (price_table_id, job_type_id, price) values (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, jp.getPriceTable().getId());
            stmt.setInt(2, jp.getJobType().getId());
            stmt.setDouble(3, jp.getPrice());

            stmt.execute();
            stmt.close();

            callback.accept(new JobPrice());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static Map<Integer, JobPrice> getMap() {
        Map<Integer, JobPrice> list = new HashMap<Integer, JobPrice>();

        String query = "SELECT * FROM job_price";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobPrice p = new JobPrice();
                p.setId(rs.getInt("id"));
                p.setPriceTable(DBPriceTable.get(rs.getInt("price_table_id")));
                p.setJobType(DBJobType.get(rs.getInt("job_type_id")));
                p.setPrice(rs.getDouble("price"));

                list.put(p.getId(), p);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static void delete(int id) {
        String query = "DELETE FROM job_price WHERE id = ?;";

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

    public static void update(JobPrice j) {
        String query = "UPDATE job_price SET price_table_id=?, job_type_id=?,price=? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getPriceTable().getId());
            stmt.setInt(2, j.getJobType().getId());
            stmt.setDouble(3, j.getPrice());
            stmt.setInt(4, j.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new JobPrice());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void updatePrice(JobPrice j) {
        String query = "UPDATE job_price SET price=? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setDouble(1, j.getPrice());
            stmt.setInt(2, j.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new JobPrice());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<JobPrice> getList() {
        return list;
    }

    public static JobPrice get(int id) {
        return map.get(id);
    }
}