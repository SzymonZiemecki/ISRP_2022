package pl.lodz.p.it.isrp;

import pl.lodz.p.it.isrp.model.PhilosopherModel;
import pl.lodz.p.it.isrp.model.TableModel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 *
 * Zadanie realizujące funkcjonalność filozofa siedzącego przy stole.
 */
public class PhilosopherRunnable implements Runnable {

    private TableModel table;
    private PhilosopherModel philosopher;
    private Semaphore semaphore;
    private int mealsNumber;
    private CountDownLatch countDownLatch;
    private boolean epochalProcessing;

    public PhilosopherRunnable(TableModel table, PhilosopherModel philosopher, Semaphore semaphore) {
        this.table = table;
        this.philosopher = philosopher;
        this.semaphore = semaphore;
    }
    public PhilosopherRunnable(TableModel table, PhilosopherModel philosopher, Semaphore semaphore, int mealsNumber, CountDownLatch countDownLatch) {
        this.table = table;
        this.philosopher = philosopher;
        this.semaphore = semaphore;
        this.mealsNumber = mealsNumber;
        this.countDownLatch = countDownLatch;
        this.epochalProcessing = true;
    }

    @Override
    public void run() {
        try {
            if (epochalProcessing) {
                int finishedMeals = 0;
                while (finishedMeals < mealsNumber) {
                    this.feastConsumption();
                    finishedMeals++;
                }
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + " skończył ucztę.");
            } else {
                while (true) {
                    this.feastConsumption();
                }
            }
        }  catch (InterruptedException ie) {
            System.err.println(Thread.currentThread().getName() + " zakończył działanie.");
        }
    }
    public void feastConsumption() throws InterruptedException
    {
        //rezerwacja lewego widelca poprzez założenie blokady wewnętrznej na reprezentującym go obiekcie
        semaphore.acquire();
        synchronized (table.getFork(philosopher.getLeftFork())) {
            System.out.println(Thread.currentThread().getName() + " pobrał lewy widelec i czeka na pobranie widelca prawego.");
            //rezerwacja prawego widelca poprzez założenie blokady wewnętrznej na reprezentującym go obiekcie
            synchronized (table.getFork(philosopher.getRightFork())) {
                philosopher.eating();
                semaphore.release();
            }
        }
        philosopher.resting();
    }
}
