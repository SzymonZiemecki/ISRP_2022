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

            try(DatabaseWriter databaseWriter = new DatabaseWriter(connectionUrl, dbUser, dbPassword, sortExample.getTab())){
                databaseWriter.save();
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
