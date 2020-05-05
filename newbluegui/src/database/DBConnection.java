package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.Client;
import job.Job;
import jobprice.JobPrice;
import pricetable.PriceTable;


public class DBConnection {

    private static String url = "jdbc:mysql://remotemysql.com:3306/C7e4RLuFgL?useTimezone=true&serverTimezone=UTC";
    private static String user = "C7e4RLuFgL";
    private static String password = "G8dSrzSuXN";

    private static Connection connection;

    public DBConnection() {

        try {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException ex) {

            Logger lgr = Logger.getLogger(DBConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /* Tabela de Preços */
    public static void insertPriceTable(PriceTable p) {
        String query = "insert into PriceTable (name) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, p.getName());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static List<PriceTable> listPriceTable() {
        List<PriceTable> list = new ArrayList<PriceTable>();

        String query = "SELECT * FROM PriceTable;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                PriceTable p = new PriceTable();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));

                list.add(p);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static PriceTable getPriceTable(int id) {
        PriceTable p = null;

        String query = "SELECT * FROM PriceTable WHERE id = " + id + ";";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                p = new PriceTable();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return p;
    }

    public static void deletePriceTable(int id) {
        String query = "DELETE FROM PriceTable WHERE id = ?;";

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

    public static void updatePriceTable(PriceTable p) {
        String query = "UPDATE PriceTable SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, p.getName());
            stmt.setInt(2, p.getId());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    /* Trabalho */
    public static void insertJob(Job j) {
        String query = "insert into Job (name) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static List<Job> listJob() {
        List<Job> list = new ArrayList<Job>();

        String query = "SELECT * FROM Job;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Job j = new Job();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("name"));

                list.add(j);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static Job getJob(int id) {
        Job j = null;

        String query = "SELECT * FROM Job WHERE id = " + id + ";";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                j = new Job();
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

    public static void deleteJob(int id) {
        String query = "DELETE FROM Job WHERE id = ?;";

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

    public static void updateJob(Job j) {
        String query = "UPDATE Job SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());
            stmt.setInt(1, j.getId());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    /* Preço */
    public static void insertJobPrice(JobPrice j) {
        String query = "insert into JobPrice (table_id, job_id, price) values (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getPriceTable().getId());
            stmt.setInt(2, j.getJob().getId());
            stmt.setDouble(3, j.getPrice());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static List<JobPrice> listJobPrice() {
        List<JobPrice> list = new ArrayList<JobPrice>();

        String query = "SELECT * FROM JobPrice;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobPrice j = new JobPrice();
                j.setId(rs.getInt("id"));
                j.setPriceTable(getPriceTable(rs.getInt("table_id")));
                j.setJob(getJob(rs.getInt("job_id")));
                j.setPrice(rs.getDouble("price"));

                list.add(j);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public static void deleteJobPrice(int id) {
        String query = "DELETE FROM JobPrice WHERE id = ?;";

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

    public static void updateJobPrice(JobPrice j) {
        String query = "UPDATE JobPrice SET table_id=?, job_id=?,price=? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, j.getPriceTable().getId());
            stmt.setInt(2, j.getJob().getId());
            stmt.setDouble(3, j.getPrice());
            stmt.setInt(4, j.getId());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    /* Cliente */
    public static boolean insertClient(Client c) {
        String query = "INSERT INTO Client (clientName, clientCpf, clientTel, clientCel, respName, respCpf, respTel, respCel, address, number, complement, city, state, cep, pricetable_id) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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

            boolean result = stmt.execute();
            stmt.close();

            return result;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
    
    public static List<Client> listClients() {
        List<Client> list = new ArrayList<Client>();

        String query = "SELECT * FROM Client;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setClientName(rs.getString("clientName"));
                c.setClientCpf(rs.getString("clientCpf"));
                c.setClientTel(rs.getString("clientTel"));
                c.setClientCel(rs.getString("clientCel"));
                c.setRespName(rs.getString("respName"));
                c.setRespCpf(rs.getString("respCpf"));
                c.setRespTel(rs.getString("respTel"));
                c.setRespCel(rs.getString("respCel"));
                c.setAddress(rs.getString("address"));
                c.setNumber(rs.getInt("number"));
                c.setComplement(rs.getString("complement"));
                c.setCity(rs.getString("city"));
                c.setState(rs.getString("state"));
                c.setCep(rs.getString("cep"));
                c.setPriceTable(getPriceTable(rs.getInt("pricetable_id")));

                list.add(c);
            }

            st.close();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }
    
    public static boolean updateClient(Client c) {
        String query = "UPDATE Client SET clientName = ?, clientCpf = ?, clientTel = ?, clientCel = ?, respName = ?, respCpf = ?, respTel = ?, respCel = ?, address = ?, number = ?, complement = ?, city = ?, state = ?, cep = ?, pricetable_id = ? WHERE id = ?";
    
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

            boolean r = stmt.execute();
            stmt.close();
            
            return r;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
