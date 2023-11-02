package model;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Buffer {

    private static final Queue<Item> bufferList = new LinkedList<>();
    private static Buffer instance;

    private Buffer() {

    }
    public static Buffer getInstance() {
        if(instance == null) {
            instance = new Buffer();
        }
        return instance;
    }

    public static Queue<Item> getBufferList() {
        return bufferList;
    }

    public synchronized void add(Item item) {
        if (bufferList.size() >= 100) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {

            bufferList.add(item);
        }
        notify();
    }

    public synchronized void remove() {
        if (bufferList.size() > 0) {
            try {
                bufferList.remove();
            } catch (NoSuchElementException e) {
                System.out.println("In Buffer " + e);
            }
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        notify();
        System.out.print(bufferList.size() + "%\r");
    }


}