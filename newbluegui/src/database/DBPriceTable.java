package database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pricetable.PriceTable;


public class DBPriceTable extends DBConnection {

    private static Map<Integer, PriceTable> listPriceTable;

    public static void insert(PriceTable p) {
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

    public static Map<Integer, PriceTable> getMap() {
        Map<Integer, PriceTable> list = new HashMap<Integer, PriceTable>();

        String query = "SELECT * FROM price_table";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                PriceTable p = new PriceTable();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));

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

    public static PriceTable get(String name) {
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

    public static void delete(int id) {
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

    public static void update(PriceTable p) {
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

    public static void updateList() {
        listPriceTable = getMap();
    }

    public static List<PriceTable> getList() {
        List<PriceTable> list = new ArrayList<PriceTable>(listPriceTable.values());
        list.sort(new Comparator<PriceTable>() {

            @Override
            public int compare(PriceTable o1, PriceTable o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        return list;
    }

    public static PriceTable get(int id) {
        return listPriceTable.get(id);
    }
}
