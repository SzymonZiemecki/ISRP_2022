package pl.lodz.p.it.isrp;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseWriter implements AutoCloseable{
    private final long[] numbers;
    private String tableName;

    private static final String connectionUrl = System.getenv("DATABASE_CONNECTION_URL") + System.getenv("DATABASE_NAME");
    private static final String dbUser = System.getenv("DATABASE_USER");
    private static final String dbPassword = System.getenv("DATABASE_PASSWORD");
    private Connection conn;

    public DatabaseWriter(long[] numbers) {
        this.numbers = numbers;
    }

    public void connect(){
        try {
            this.conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
            if(this.conn !=null) {
                System.out.println("Polaczono z baza danych");
            }
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
