package pl.lodz.p.it.isrp.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Model filozofa.
 */
public class PhilosopherModel {

    private static final int MAX_EAT_TIME = 30;
    private static final int MIN_EAT_TIME = MAX_EAT_TIME / 2;
    private static final int MAX_REST_TIME = MAX_EAT_TIME / 3;
    private boolean isEating = false;
    private long mealsCounter = 0;
    private long restsCounter = 0;
    private int time;
    private int id;

    public PhilosopherModel(final int id) {
        this.id = id;
    }

    public int getLeftFork() {
        return id - 1;
    }

    public int getRightFork() {
        return id;
    }

    public void eating() throws InterruptedException {
        isEating = true;
        mealsCounter++;
        time = ThreadLocalRandom.current().nextInt(MIN_EAT_TIME, MAX_EAT_TIME);
        System.out.println(this);
        Thread.sleep(time);
    }

    public void resting() throws InterruptedException {
        isEating = false;
        restsCounter++;
        time = ThreadLocalRandom.current().nextInt(MAX_REST_TIME);
        System.out.println(this);
        Thread.sleep(time);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Wątek reprezentujący filozofa " + id + " obecenie ");
        if (isEating) {
            sb.append("je posiłek ").append(mealsCounter);
        } else {
            sb.append("odpoczywa ").append(restsCounter);
        }
        sb.append(" przez czas ").append(time);
        sb.append(" ms");
        return sb.toString();
    }
}
