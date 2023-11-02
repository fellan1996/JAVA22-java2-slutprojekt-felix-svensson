package model;


public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int frequency = (int) (Math.random()*10)+1;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.frequency* 1000L);
                buffer.remove();

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}