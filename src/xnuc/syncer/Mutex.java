package xnuc.syncer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Mutex {

    private AtomicReference<Thread> state = new AtomicReference<>();
    private java.util.Queue<Thread> syncer = new java.util.LinkedList<>();

    private synchronized boolean synceradd() {
        return syncer.add(Thread.currentThread());
    }

    public void lock() {
        if (state.compareAndSet(null, Thread.currentThread())) {
            return;
        }
        synceradd();
        LockSupport.park();
        lock();
    }

    public void unlock() {
        state.set(null);
        var next = syncer.poll();
        if (next == null) {
            return;
        }
        LockSupport.unpark(next);
    }
}