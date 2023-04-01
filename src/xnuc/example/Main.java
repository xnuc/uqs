package xnuc.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var logic = new Logic();
        for (int i = 0; i < 10; i++) {
            new Thread(logic).start();
        }
        Thread.sleep(1000);
        java.util.logging.Logger.getGlobal().info(
                String.format("thread:%s ts:%d \n over:%d",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis(),
                        logic.cnt));

        logic = new MutexLogic();
        for (int i = 0; i < 10; i++) {
            new Thread(logic).start();
        }
        Thread.sleep(1000);
        java.util.logging.Logger.getGlobal().info(
                String.format("thread:%s ts:%d \n over:%d",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis(),
                        logic.cnt));
    }
}

class Logic implements Runnable {

    Integer cnt = Integer.valueOf(0);

    public void run() {
        var local = cnt;
        java.util.logging.Logger.getGlobal().info(
                String.format("thread:%s ts:%d \n want:%d->%d",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis(),
                        cnt, local + 1));
        cnt = ++local;
    }
}

class MutexLogic extends Logic {

    xnuc.syncer.Mutex mutex = new xnuc.syncer.Mutex();

    @Override
    public void run() {
        mutex.lock();
        super.run();
        mutex.unlock();
    }
}