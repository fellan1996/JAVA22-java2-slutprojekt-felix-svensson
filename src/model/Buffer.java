package model;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Buffer {

    Queue<Item> buffer = new LinkedList<Item>();

    public synchronized void add(Item item) {
        buffer.add(item);
        notify();
        System.out.print( buffer.size() + "%\r");
    }

    public synchronized void remove() {
        if(buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try{
            buffer.remove();
        }catch(NoSuchElementException e) {
            System.out.println("rad 29 " + e);
        }
        System.out.print( buffer.size() + "%\r");
    }


}