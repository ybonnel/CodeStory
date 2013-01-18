package fr.ybonnel.codestory.path.jajascript;


import java.util.Iterator;

public class LegacyFastFifo implements Iterable<LegacySolution> {

    private LegacySolution[] tabOfElements;             // queue elements
    private int nbElements;           // number of elements on queue
    private int last;       // index of next available slot
    private int first;
    private FifoIter iter;


    // cast needed since no generic array creation in Java
    public LegacyFastFifo(int capacity) {
        nbElements = capacity;
        tabOfElements = new LegacySolution[capacity];
        first = 0;
        last = nbElements - 1;
        iter = new FifoIter();
    }

    public LegacySolution getFirst() {
        return tabOfElements[first];
    }

    public void enqueue(LegacySolution item) {
        tabOfElements[last] = item;
        last = (last + 1) % nbElements; // wrap-around
        first = (first + 1) % nbElements; // wrap-around

    }

    private class FifoIter implements Iterator<LegacySolution> {

        private int actuelIndex;
        private boolean nextCalled;

        private void init() {
            nextCalled = false;
            actuelIndex = first;
        }

        @Override
        public boolean hasNext() {
            return actuelIndex != first || !nextCalled;
        }

        @Override
        public LegacySolution next() {
            nextCalled = true;
            LegacySolution item = tabOfElements[actuelIndex];
            actuelIndex = (actuelIndex + 1) % nbElements;
            return item;
        }

        @Override
        public void remove() {
            throw new IllegalArgumentException("Method remove not implemented");
        }
    }


    @Override
    public Iterator<LegacySolution> iterator() {
        iter.init();
        return iter;
    }
}
