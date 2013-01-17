package fr.ybonnel.codestory.path.jajascript;


import java.util.Iterator;

public class FastFifo implements Iterable<JajascriptService.Solution> {

    private JajascriptService.Solution[] tabOfElements;             // queue elements
    private int nbElements;           // number of elements on queue
    private int last;       // index of next available slot
    private int first;
    private FifoIter iter;


    // cast needed since no generic array creation in Java
    public FastFifo(int capacity) {
        nbElements = capacity;
        tabOfElements = new JajascriptService.Solution[capacity];
        first = 0;
        last = nbElements-1;
        iter = new FifoIter();
    }

    public JajascriptService.Solution getFirst() {
        return tabOfElements[first];
    }

    public void enqueue(JajascriptService.Solution item) {
        tabOfElements[last] = item;
        last = (last + 1) % nbElements; // wrap-around
        first = (first + 1) % nbElements; // wrap-around

    }

    private class FifoIter implements Iterator<JajascriptService.Solution> {

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
        public JajascriptService.Solution next() {
            nextCalled = true;
            JajascriptService.Solution item = tabOfElements[actuelIndex];
            actuelIndex = (actuelIndex+1)%nbElements;
            return item;
        }

        @Override
        public void remove() {
            throw new IllegalArgumentException("Method remove not implemented");
        }
    }


    @Override
    public Iterator<JajascriptService.Solution> iterator() {
        iter.init();
        return iter;
    }
}
