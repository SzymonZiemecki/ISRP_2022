package pl.lodz.p.it.isrp;

import pl.lodz.p.it.isrp.model.PhilosopherModel;
import pl.lodz.p.it.isrp.model.TableModel;

import java.util.concurrent.Semaphore;

/**
 *
 * Zadanie realizujące funkcjonalność filozofa siedzącego przy stole.
 */
public class PhilosopherRunnable implements Runnable {

    private TableModel table;
    private PhilosopherModel philosopher;
    private Semaphore semaphore;

    public PhilosopherRunnable(TableModel table, PhilosopherModel philosopher, Semaphore semaphore) {
        this.table = table;
        this.philosopher = philosopher;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //rezerwacja lewego widelca poprzez założenie blokady wewnętrznej na reprezentującym go obiekcie
                semaphore.acquire();
                synchronized (table.getFork(philosopher.getLeftFork())) {
                    System.out.println(Thread.currentThread().getName() + " pobrał lewy widelec i czeka na pobranie widelca prawego");
                    //rezerwacja prawego widelca poprzez założenie blokady wewnętrznej na reprezentującym go obiekcie
                    synchronized (table.getFork(philosopher.getRightFork())) {
                        philosopher.eating();
                        semaphore.release();
                    }
                }
                philosopher.resting();
            }
        } catch (InterruptedException ie) {
            System.err.println(Thread.currentThread().getName() + " zakończył działanie.");
        }
    }
}
