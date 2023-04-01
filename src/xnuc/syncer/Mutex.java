package xnuc.syncer;

public class Mutex {

    private java.util.concurrent.atomic.AtomicReference<Thread> state = new java.util.concurrent.atomic.AtomicReference<>();
    private java.util.Queue<Thread> syncer = new java.util.LinkedList<>();

    private synchronized boolean synceradd() {
        return syncer.add(Thread.currentThread());
    }

    public void lock() {
        if (state.compareAndSet(null, Thread.currentThread())) {
            return;
        }
        synceradd();
        java.util.concurrent.locks.LockSupport.park();
        lock();
    }

    public void unlock() {
        state.set(null);
        var next = syncer.poll();
        if (next == null) {
            return;
        }
        java.util.concurrent.locks.LockSupport.unpark(next);
    }
}