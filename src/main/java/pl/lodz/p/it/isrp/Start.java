package pl.lodz.p.it.isrp;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Rozwiązanie problemu sortowania tablicy (zawiera błędy które należy usunąć z
 * zastosowanie nadzorowanego wykonania programu (debug)). Diagnostykę należy
 * przeprowadzić z zastosowaniem programu narzędziowego debuggera jdb
 */
public class Start {

    private static final String connectionUrl = System.getenv("DATABASE_CONNECTION_URL") + System.getenv("DATABASE_NAME");
    private static final String dbUser = System.getenv("DATABASE_USER");
    private static final String dbPassword = System.getenv("DATABASE_PASSWORD");

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try {
            Connection conn2 = DriverManager.getConnection(connectionUrl, dbUser,dbPassword);
            if (conn2 != null) {
                System.out.println("Connected to database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (args.length == 0) {
            System.out.println("Brak podanej liczby całkowitej jako argumentu wywołania");
            System.exit(1);
        }
        try {
            SortTabNumbers sortExample = new SortTabNumbers(Integer.parseInt(args[0].trim()));

            System.out.println("Przed sortowaniem: " + sortExample); //niejawne wywołanie metody sortExample.toString()

            sortExample.sort();

            if (sortExample.checkMinOrderSort()) {
                System.out.println("Po sortowaniu: " + sortExample); //niejawne wywołanie metody sortExample.toString()
            }

            try(DatabaseWriter databaseWriter = new DatabaseWriter(connectionUrl,dbUser, dbPassword, sortExample.getTab())){
                databaseWriter.connect();
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
