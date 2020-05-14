package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jobprice.JobPrice;

public class DBJobPrice extends DBConnection{
    private static List<JobPrice> listJobPrice;
    
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

    public static void insert(JobPrice jp) {
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

    private static List<JobPrice> getList() {
        List<JobPrice> list = new ArrayList<JobPrice>();

        String query = "SELECT * FROM job_price inner join price_table where job_price.price_table_id = price_table.id order by price_table.name";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                JobPrice j = new JobPrice();
                j.setId(rs.getInt("id"));
                j.setPriceTable(DBPriceTable.get(rs.getInt("price_table_id")));
                j.setJob(DBJobType.get(rs.getInt("job_type_id")));
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

    public static void delete(int id) {
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

    public static void update(JobPrice j) {
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
    
    public static List<JobPrice> getList(boolean refresh) {
        if (refresh)
            listJobPrice = getList();
        
        return listJobPrice;
    }
}
