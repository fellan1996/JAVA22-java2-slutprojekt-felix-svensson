package model;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final int frequency = (int) (Math.random()*10)+1;
    private boolean isRunning = true;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public int getFrequency() {
        return frequency;
    }

    public void stop() {
        isRunning = false;
    }
    @Override
    public void run() {

        while (isRunning) {
            try {
                Thread.sleep(frequency* 1000L);

                buffer.add(new Item());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.currentThread().interrupt();
    }

}