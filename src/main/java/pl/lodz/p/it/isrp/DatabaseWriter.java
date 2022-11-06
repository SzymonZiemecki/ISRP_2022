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
        try {
            this.conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
            System.out.println("Polaczono z baza danych");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void save(){
        createTable();
        insertValues();
    }

    private void createTable() {
        try (Statement stmt = conn.createStatement()) {
            ZonedDateTime currentTime = ZonedDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss_SSS_zz");
            this.tableName = "sorted_" + currentTime.format(formatter);
            String sql = "CREATE TABLE " + tableName +
                    "(value BIGINT NOT NULL, " +
                    " timestamp TIMESTAMP, " +
                    " PRIMARY KEY ( value ))";
            stmt.executeUpdate(sql);
            System.out.println("Utworzono tabele: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertValues() {
        for(long number : numbers){
            try (PreparedStatement insertNumber = conn.prepareStatement(
                    "INSERT INTO " + tableName + " (value, timestamp) VALUES (?, ?)"
            )) {
                insertNumber.setLong(1, number);
                insertNumber.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                insertNumber.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void close() throws Exception {
        conn.close();
        System.out.println("Zakonczono polaczenie z baza danych");
    }
}
