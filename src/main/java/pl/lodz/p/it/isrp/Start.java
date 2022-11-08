package pl.lodz.p.it.isrp;

import pl.lodz.p.it.isrp.model.PhilosopherModel;
import pl.lodz.p.it.isrp.model.TableModel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Aplikacja przygotowana do celów diagnostycznych, która prezentuje przykładowe
 * rozwiązanie problemu ucztujących filozofów. Krótki opis problemu: przy
 * okrągłym stole zasiadają filozofowie tak, by pomiędzy sąsiadującymi
 * filozofami znajdował się na stole dokłanie jeden widelec. Każdy filozof
 * naprzemiennie wykonuje dwie czynności: jedzenie posiłku (eating) oraz
 * odpoczynek (resting). Aby rozpocząć jedzenie filozof potrzebuje dwóch
 * najbliższych mu widelców jednocześnie, dodatkowo filozof nie może odłożyć
 * podniesionego już widelca, do czasu kiedy nie zje posiłku. Zatem dwóch
 * sąsiadujących ze sobą przy stole filozofów nie może jednocześnie jeść
 * posiłku. Filozofowie nie mogą też wzajemnie się komunikować, w synchronizacji
 * ich działań należy wykorzystać dedykowane mechanizmy synchronizacji.
 *
 * Przygotowany kod aplikacji zawiera błąd powodujący wystąpienie wzajemnej
 * blokady (deadlock), której przyczynę należy zdiagnozować i poprawić z
 * wykorzystaniem technik diagnostycznych stosując nadzorowane uruchomienie
 * programu (debug).
 *
 */
public class Start {

    public static final int THREADS_STATE_DELAY = 60;
    private static Thread[] threads;
    private static int PHILOSOPHERS_NUMBER;

    private static final Thread.UncaughtExceptionHandler exeptionHandler = (t, e) -> {
        System.err.println("Wątek " + t.getName() + " zgłosił nieobsłużony wyjątek typu:" + e.getClass().getName() + ". Szczegóły: " + e.getMessage());
        System.exit(3);
    };
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(exeptionHandler);
        switch (args.length)
        {
            case 0:
                System.out.println("Brak podanej liczby całkowitej jako argumentu wywołania.");
                System.exit(1);
                break;
            case 1:
                try {
                    PHILOSOPHERS_NUMBER = Integer.parseInt(args[0].trim());
                    threads = new Thread[PHILOSOPHERS_NUMBER];
                    TableModel table = new TableModel(PHILOSOPHERS_NUMBER);
                    Semaphore semaphore = new Semaphore(PHILOSOPHERS_NUMBER-1);
                    for (int i = 0; i < PHILOSOPHERS_NUMBER; i++) {
                        PhilosopherModel philosopher = new PhilosopherModel(i + 1);
                        threads[i] = new Thread(new PhilosopherRunnable(table, philosopher, semaphore));
                        threads[i].setName("Wątek reprezentujący filozofa " + (i + 1));
                        threads[i].start();
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Podany argument nie jest liczbą.");
                    System.exit(2);
                } catch (Throwable ex) {
                    System.err.println("Wystąpił wyjątek typu: " + ex.getClass().getName() + " Szczegóły: " + ex.getMessage());
                    System.exit(3);
                }
                break;
            case 2:
                try {
                    final int epochNumber = Integer.parseInt(args[0].trim());
                    PHILOSOPHERS_NUMBER = Integer.parseInt(args[1].trim());
                    if (epochNumber < 2) {
                        System.out.println("Ilość epok musi być większa od 1.");
                        System.exit(1);
                        break;
                    }
                    else if (PHILOSOPHERS_NUMBER < 2) {
                        System.out.println("Liczba filozofów musi być większa od 1.");
                        System.exit(1);
                        break;
                    }
                    for (int i = 1; i <= epochNumber; i++) {
                        System.out.println("\n----------------------------------------------------------");
                        System.out.println("\nEtap: " + i + "\n");
                        int mealsAndPhilosophersNumber = (int) (Math.random() * (PHILOSOPHERS_NUMBER - 2) + 2);
                        System.out.println("Liczba filozofów w etapie " + i + " wynosi: " + mealsAndPhilosophersNumber);
                        TableModel table = new TableModel(mealsAndPhilosophersNumber);
                        threads = new Thread[mealsAndPhilosophersNumber];
                        Semaphore semaphore = new Semaphore(mealsAndPhilosophersNumber - 1);
                        CountDownLatch countDownLatch = new CountDownLatch(mealsAndPhilosophersNumber);
                        for (int j = 0; j < mealsAndPhilosophersNumber; j++) {
                            int mealsNumber = (int) (Math.random() * (PHILOSOPHERS_NUMBER - 2) + 2);
                            PhilosopherModel philosopher = new PhilosopherModel(j + 1);
                            threads[j] = new Thread(new PhilosopherRunnable(table, philosopher, semaphore, mealsNumber, countDownLatch));
                            threads[j].setName("Wątek reprezentujący filozofa " + (j + 1));
                            System.out.println(threads[j].getName() + ", liczba posiłków do zjedzenia przez filozofa: " + mealsNumber);
                        }
                        System.out.println("\nRozpoczęcie uczty dla etapu nr. " + i + "\n");
                        for (int j = 0; j < mealsAndPhilosophersNumber; j++) {
                            threads[j].start();
                        }
                        countDownLatch.await();
                        System.out.println("\nZakończenie uczty dla etapu nr. " + i);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Jeden lub oba z podanych argumentów nie są liczbami.");
                    System.exit(2);
                } catch (Throwable ex) {
                    System.err.println("Wystpił wyjątek typu: " + ex.getClass().getName() + " Szczegóły: " + ex.getMessage());
                    System.exit(3);
                }
                break;

        }

    }
}
