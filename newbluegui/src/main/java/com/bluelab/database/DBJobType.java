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
        map = getMap();

        list.clear();
        list.addAll(map.values());
        list.sort(new Comparator<JobType>() {

            @Override
            public int compare(JobType o1, JobType o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    public static void init() {
        list = FXCollections.observableArrayList(new ArrayList<JobType>());

        updateData();

        setCallback(c -> Platform.runLater(() -> updateData()));
    }

    public static void insert(JobType j) {
        String query = "insert into job_type (name) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());

            stmt.execute();
            stmt.close();

            callback.accept(new JobType());
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static Map<Integer, JobType> getMap() {
        Map<Integer, JobType> list = new HashMap<Integer, JobType>();

        String query = "SELECT * FROM job_type";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobType j = new JobType();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));

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

    public static JobType get(String name) {
        JobType j = null;

        String query = "SELECT * FROM job_type WHERE name = \"" + name + "\";";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                j = new JobType();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return j;
    }

    public static void delete(int id) {
        String query = "DELETE FROM job_type WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();

            callback.accept(new JobType());
            
            DBJobPrice.updateData();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void update(JobType j) {
        String query = "UPDATE job_type SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());
            stmt.setInt(2, j.getId());

            stmt.execute();
            stmt.close();

            callback.accept(new JobType());
            
            DBJobPrice.updateData();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static ObservableList<JobType> getList() {
        return list;
    }

    public static JobType get(int id) {
        return map.get(id);
    }
}
