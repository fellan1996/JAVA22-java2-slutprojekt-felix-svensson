package model;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Buffer {

    static Queue<Item> bufferList;

    public Buffer(Queue<Item> bufferList) {
        Buffer.bufferList = bufferList;
    }

    public synchronized void add(Item item) {
        if(bufferList.size() >= 100) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else{

        bufferList.add(item);
        notify();
        System.out.print( bufferList.size() + "%\r");
        }
    }

    public synchronized void remove() {
        if(bufferList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(bufferList.size()<100){
            notify();
        }

        try{
            bufferList.remove();
        }catch(NoSuchElementException e) {
            System.out.println("rad 29 " + e);
        }
        System.out.print( bufferList.size() + "%\r");
    }


}