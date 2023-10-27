package controller;

import model.Buffer;
import model.Consumer;
import model.Producer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public Controller() {
        SwingUtilities.invokeLater(() -> createAndShowGUI());

    }

    private static final Buffer buffer = new Buffer();
    private static final List<Producer> producers = new ArrayList<>();
    private static JTextArea consoleOutput;



    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Producer-Consumer Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        JButton addProducerButton = new JButton("Add Producer");
        JButton addTheConsumersBtn = new JButton("Start");
        JButton removeProducerButton = new JButton("Remove Producer");

        consoleOutput = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(consoleOutput);

        addProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProducer();
                displayAmountOfProducers();
            }
        });
        addTheConsumersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTheConsumersBtn.setEnabled(false); // Disable the button
                int numOfConsumers = (int) (Math.random() * 13) + 3;
                for (int i = 1; i <= numOfConsumers; i++) {
                    addConsumer();
                }
            }
        });
        removeProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!producers.isEmpty()) {
                    removeProducer();
                    displayAmountOfProducers();
                } else {
                    consoleOutput.append("No producers to remove\n");
                }
            }
        });

        frame.add(addProducerButton);
        frame.add(addTheConsumersBtn);
        frame.add(removeProducerButton);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private static void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    private static void displayAmountOfProducers() {
        consoleOutput.append("Amount of Producers: " + producers.size() + "\n");
    }

    private static void addProducer() {
        Producer producer = new Producer(buffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producers.add(producer);
    }

    private static void removeProducer() {
        producers.get(0).stop();
        producers.remove(0);
    }
}
