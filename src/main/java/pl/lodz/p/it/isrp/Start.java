package pl.lodz.p.it.isrp;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Rozwiązanie problemu sortowania tablicy (zawiera błędy które należy usunąć z
 * zastosowanie nadzorowanego wykonania programu (debug)). Diagnostykę należy
 * przeprowadzić z zastosowaniem programu narzędziowego debuggera jdb
 */
public class Start {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        String connectionUrl = "jdbc:derby:/home/student/IdeaProjects/ISRP_2022/ISRP_DB";
        String dbUser = "root";
        String dbPassword = "password";
        if (args.length == 0) {
            System.out.println("Brak podanej liczby całkowitej jako argumentu wywołania");
            System.exit(1);
        }
        try {
            SortTabNumbers sortExample = new SortTabNumbers(Integer.parseInt(args[0].trim()));

            System.out.println("Przed sortowaniem: " + sortExample); //niejawne wywołanie metody sortExample.toString()

            sortExample.sort();

            try(Connection conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
                Statement stmt = conn.createStatement();
            ) {
                ZonedDateTime currentTime = ZonedDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss_SSS_zz");
                String tableName = "sorted_" + currentTime.format(formatter);
                String sql = "CREATE TABLE " + tableName +
                        "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        " value BIGINT NOT NULL, " +
                        " timestamp DATE, " +
                        " PRIMARY KEY ( id ))";
                stmt.executeUpdate(sql);
                System.out.println("Created table in given database...");

                long[] array = sortExample.getTab();
                for(long number : array){
                    try (PreparedStatement insertNumber = conn.prepareStatement(
                            "INSERT INTO " + tableName + " (value, timestamp) VALUES (?, ?)"
                    )) {
                        insertNumber.setLong(1, number);
                        insertNumber.setDate(2, Date.valueOf(LocalDate.now()));
                        insertNumber.execute();
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (sortExample.checkMinOrderSort()) {
                System.out.println("Po sortowaniu: " + sortExample); //niejawne wywołanie metody sortExample.toString()
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Podany argument nie jest liczbą");
            System.exit(2);
        } catch (Throwable ex) {
            System.out.println("Zakończenie programu w wyniku zgłoszenia wyjątku typu " + ex.getClass().getName());
            System.exit(3);
        }
    }
}
