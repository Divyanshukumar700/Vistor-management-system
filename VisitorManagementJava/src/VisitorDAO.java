import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Data Access Object for visitors table. */
public class VisitorDAO {

    public void addVisitor(String name, String phone, String whomToVisit, String purpose) throws SQLException {
        String sql = "INSERT INTO visitors (name, phone, whom_to_visit, purpose) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, whomToVisit);
            ps.setString(4, purpose);
            ps.executeUpdate();
        }
    }

    public List<Visitor> getAllVisitors() throws SQLException {
        List<Visitor> list = new ArrayList<>();
        String sql = "SELECT * FROM visitors ORDER BY check_in_time DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Visitor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("whom_to_visit"),
                        rs.getString("purpose"),
                        rs.getTimestamp("check_in_time"),
                        rs.getTimestamp("check_out_time"),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }

    public void checkoutVisitor(int id) throws SQLException {
        String sql = "UPDATE visitors SET status='checked-out', check_out_time=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean validateGuard(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM guards WHERE username=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
