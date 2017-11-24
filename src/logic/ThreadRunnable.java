package logic;

public class ThreadRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("id: " + Thread.currentThread().getId() + "name: " + Thread.currentThread().getName());
    }
}
