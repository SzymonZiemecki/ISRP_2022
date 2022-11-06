package pl.lodz.p.it.isrp;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseWriter implements AutoCloseable{
    private final String connectionUrl;
    private final String dbUser;
    private final String dbPassword;
    private final long[] numbers;
    private String tableName;

    private Connection conn;

    public DatabaseWriter(String connectionUrl, String dbUser, String dbPassword, long[] numbers) {
        this.connectionUrl = connectionUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.numbers = numbers;
    }

    public void connect(){
        try {
            this.conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
            System.out.println("Polaczono z baza danych");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        conn.close();
        System.out.println("Zakonczono polaczenie z baza danych");
    }
}
