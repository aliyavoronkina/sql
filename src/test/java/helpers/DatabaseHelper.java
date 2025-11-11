package helpers;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHelper {
    private static final String url = "jdbc:mysql://localhost:3306/app";
    private static final String user = "root";    // меняем на root
    private static final String pass = "root";    // меняем на root

    public static void clearAuthCodes() {
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            runner.update(conn, "DELETE FROM auth_codes");
        } catch (Exception e) {
            throw new RuntimeException("Error clearing auth codes", e);
        }
    }

    public static String getVerificationCode() {
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            return runner.query(conn,
                    "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1",
                    new ScalarHandler<>()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error getting verification code", e);
        }
    }
}