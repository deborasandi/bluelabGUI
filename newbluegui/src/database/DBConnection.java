package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.Client;
import jobprice.JobPrice;
import jobtype.JobType;
import pricetable.PriceTable;


public class DBConnection {

    private static String url = "jdbc:mysql://bluelabdb.c7tsyx37zboh.us-east-2.rds.amazonaws.com:3306/bluelabdb?useTimezone=true&serverTimezone=UTC";
    private static String user = "bluelabadmin";
    private static String password = "bluelabadmin";

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
        String query = "insert into price_table (name) values (?)";

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

        String query = "SELECT * FROM price_table;";

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

        String query = "SELECT * FROM price_table WHERE id = " + id + ";";

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

    public static PriceTable getPriceTable(String name) {
        PriceTable p = null;

        String query = "SELECT * FROM price_table WHERE name = \"" + name + "\";";

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
        String query = "DELETE FROM price_table WHERE id = ?;";

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
        String query = "UPDATE price_table SET name = ? WHERE id = ?;";

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
    public static void insertJobType(JobType j) {
        String query = "insert into job_type (name) values (?)";

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

    public static List<JobType> listJobType() {
        List<JobType> list = new ArrayList<JobType>();

        String query = "SELECT * FROM job_type;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobType j = new JobType();
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

    public static JobType getJobType(int id) {
        JobType j = null;

        String query = "SELECT * FROM job_type WHERE id = " + id + ";";

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
    
    public static JobType getJobType(String name) {
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

    public static void deleteJob(int id) {
        String query = "DELETE FROM job_type WHERE id = ?;";

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

    public static void updateJob(JobType j) {
        String query = "UPDATE job_type SET name = ? WHERE id = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, j.getName());
            stmt.setInt(2, j.getId());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    /* Preço */
    public static void insertJobPrice(List<JobPrice> list) {
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
                stmt.setInt(i + 2, j.getJob().getId());
                stmt.setDouble(i + 3, j.getPrice());

                i += 3;
            }

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static void insertJobPrice(JobPrice jp) {
        String query = "insert into job_price (price_table_id, job_type_id, price) values (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, jp.getPriceTable().getId());
            stmt.setInt(2, jp.getJob().getId());
            stmt.setDouble(3, jp.getPrice());

            stmt.execute();
            stmt.close();
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public static List<JobPrice> listJobPrice() {
        List<JobPrice> list = new ArrayList<JobPrice>();

        String query = "SELECT * FROM job_price;";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobPrice j = new JobPrice();
                j.setId(rs.getInt("id"));
                j.setPriceTable(getPriceTable(rs.getInt("price_table_id")));
                j.setJob(getJobType(rs.getInt("job_type_id")));
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
        String query = "DELETE FROM job_price WHERE id = ?;";

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
        String query = "UPDATE job_price SET price_table_id=?, job_type_id=?,price=? WHERE id = ?;";

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

        String query = "SELECT * FROM client;";

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
                c.setPriceTable(getPriceTable(rs.getInt("price_table_id")));

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

            boolean r = stmt.execute();
            stmt.close();

            return r;
        }
        catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
