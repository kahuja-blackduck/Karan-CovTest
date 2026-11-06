package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VulnDemo {

    // 1. Hardcoded credential (HARDCODED_CREDENTIALS)
    private static final String DB_PASSWORD = "SuperSecret123";

    // 2. SQL injection (SQLI): user input concatenated into a query
    public static void findUser(String userName) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/test", "root", DB_PASSWORD);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM users WHERE name = '" + userName + "'");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        conn.close();
    }

    // 3. Null pointer dereference (FORWARD_NULL)
    public static int nameLength(String name) {
        String value = null;
        if (name != null && name.length() > 3) {
            value = name;
        }
        return value.length();   // value can still be null here
    }
// 4. Resource leak (RESOURCE_LEAK): stream is never closed
    public static int readFirstByte(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        return in.read();
    }

    public static void main(String[] args) throws Exception {
        String input = args.length > 0 ? args[0] : "test";
        System.out.println(nameLength(input));
        findUser(input);
        readFirstByte(input);
    }
}
