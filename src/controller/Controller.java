package controller;

import model.Buffer;
import model.Consumer;
import model.Producer;


public class MainController {

    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Producer producer = new Producer(buffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

    }
}