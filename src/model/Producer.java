package model;




public class Producer implements Runnable {
    Buffer buffer = null;
    int frequency = (int) (Math.random()*5)+1;
    boolean isRunning = true;

    public Producer(Buffer buffer) {
        this.buffer = buffer;

    }

    public void stop() {
        isRunning = false;
    }
    @Override
    public void run() {

        while (isRunning) {
            try {
                Thread.sleep(this.frequency*1000);

                buffer.add(new Item(""+((int)(Math.random()*100))));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread.currentThread().interrupt();
    }

}