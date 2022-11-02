package pl.lodz.p.it.isrp.model;
/**
 *
 * Model stołu z widelcami
 */
public class TableModel {

    private final Object forks[];
    
    public TableModel(final int forksNumber) {
        if (forksNumber<2) {
            throw new IllegalArgumentException("Liczba widelców na stole musi być wartością dodatnią i zgodną z liczbą filozofów, a podano "+ forksNumber);
        }
        forks = new Object[forksNumber];
        for(int i=0; i<=forksNumber; i++) {
            forks[i] = new Object();
        }
    }
    
    public Object getFork(final int number) {
        return forks[number%forks.length];
    }

    @Override
    public String toString() {
        return "Tablica z widelcami: "+forks.toString();
    }
}
