package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import job.Job;

public class DBJob extends DBConnection{
    private static Map<Integer, Job> listJob;
    
    public static boolean insert(Job j) {
        String query = "INSERT INTO job (client_id, job_type_id, qtd, shipping, date, repetition, nocost, paid) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getClient().getId());
            stmt.setInt(2, j.getJobType().getId());
            stmt.setInt(3, j.getQtd());
            stmt.setDouble(4, j.getShipping());
            stmt.setDate(5, j.getDate());
            stmt.setBoolean(6, j.isRepetition());
            stmt.setBoolean(7, j.isNocost());
            stmt.setBoolean(8, j.isPaid());

            boolean result = stmt.execute();
            stmt.close();

            return result;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    private static Map<Integer, Job> getMap() {
        Map<Integer, Job> list = new HashMap<Integer, Job>();

        String query = "SELECT * FROM job";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Job j = new Job();

                j.setId(rs.getInt("id"));
                j.setClient(DBClient.get(rs.getInt("client_id")));
                j.setJobType(DBJobType.get(rs.getInt("job_type_id")));
                j.setQtd(rs.getInt("qtd"));
                j.setShipping(rs.getDouble("shipping"));
                j.setDate(rs.getDate("date", Calendar.getInstance()));
                j.setRepetition(rs.getBoolean("repetition"));
                j.setNocost(rs.getBoolean("nocost"));
                j.setPaid(rs.getBoolean("paid"));

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

    public static boolean update(Job j) {
        String query = "UPDATE job SET client_id = ?, job_type_id = ?, qtd = ?, shipping = ?, date = ?, repetition = ?, nocost = ?, paid = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getClient().getId());
            stmt.setInt(2, j.getJobType().getId());
            stmt.setInt(3, j.getQtd());
            stmt.setDouble(4, j.getShipping());
            stmt.setDate(5, j.getDate());
            stmt.setBoolean(6, j.isRepetition());
            stmt.setBoolean(7, j.isNocost());
            stmt.setBoolean(8, j.isPaid());
            stmt.setInt(9, j.getId());

            boolean r = stmt.execute();
            stmt.close();

            return r;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static boolean update(int id, boolean paid) {
        String query = "UPDATE job SET paid = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setBoolean(1, paid);
            stmt.setInt(2, id);

            boolean r = stmt.execute();
            stmt.close();

            return r;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void delete(int id) {
        String query = "DELETE FROM job WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, id);

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
    
    public static void updateList() {
        listJob = getMap();
    }

    public static List<Job> getList() {
        List<Job> list = new ArrayList<Job>(listJob.values());
        
        list.sort(new Comparator<Job>() {

            @Override
            public int compare(Job o1, Job o2) {
                return o1.getClient().getClientName().compareToIgnoreCase(o2.getClient().getClientName());
            }
        });
        
        return list;
    }
}
