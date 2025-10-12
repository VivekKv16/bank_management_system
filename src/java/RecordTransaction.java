import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class RecordTransaction  {

    public static void recordTransactions(Connection con, String type, double amount, long accountNumber) throws SQLException {
        String iqry = "insert into transactions(type, amount, AccountNumber, timestamp) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(iqry)) {
            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setLong(3, accountNumber);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }
}
