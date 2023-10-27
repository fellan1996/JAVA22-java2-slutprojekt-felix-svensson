package model;


public class Consumer implements Runnable {
    Buffer buffer = null;
    boolean isRunning = true;
    int frequency = (int) (Math.random()*5)+1;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;

    }

    @Override
    public void run() {
//TODO Make it so that a random number of consumers between 3 and 15 are created. Each consumer will take a random amount of time between one and ten to consume one item.


        while (isRunning) {
            try {
                Thread.sleep(this.frequency*1000);
                buffer.remove();

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }

}