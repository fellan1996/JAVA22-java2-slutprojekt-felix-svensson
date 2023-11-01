package model;




public class Producer implements Runnable {
    Buffer buffer = null;
    final int frequency = (int) (Math.random()*10)+1;
    boolean isRunning = true;

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
                Thread.sleep(frequency*1000);

                buffer.add(new Item(""+((int)(Math.random()*100))));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.currentThread().interrupt();
    }

}