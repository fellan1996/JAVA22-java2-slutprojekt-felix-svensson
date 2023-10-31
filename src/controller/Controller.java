package controller;

import model.Buffer;
import model.Consumer;
import model.Item;
import model.Producer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {


    static Queue<Item> bufferList = new LinkedList<Item>();
    private static final Buffer buffer = new Buffer(bufferList);
    private static List<Producer> producers;
    private static JTextArea consoleOutput;
    private static JTextArea textfileOutput;
    private static JProgressBar progressBar;
    private static Timer timer;

    public Controller() {
        SwingUtilities.invokeLater(Controller::createAndShowGUI);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgressBar();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Producer-Consumer Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        JButton addProducerButton = new JButton("Add Producer");
        JButton addConsumersButton = new JButton("Start");
        JButton removeProducerButton = new JButton("Remove Producer");

        producers = new ArrayList<>();

        consoleOutput = new JTextArea(10, 30);
        textfileOutput = new JTextArea(10, 30);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        JScrollPane scrollPane = new JScrollPane(consoleOutput);
        JScrollPane textFileScrollPane = new JScrollPane(textfileOutput);

        logProducersToFile();
        loadFileContent();

        addProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProducer();
                displayAmountOfProducers();
                logProducersToFile();
                loadFileContent();
            }
        });
        removeProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!producers.isEmpty()) {
                    removeProducer();
                    displayAmountOfProducers();
                    logProducersToFile();
                    loadFileContent();
                } else {
                    consoleOutput.append("No producers to remove\n");
                }
            }
        });
        addConsumersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addConsumersButton.setEnabled(false); // Disable the button
                int numOfConsumers = (int) (Math.random() * 13) + 3;
                for (int i = 1; i <= numOfConsumers; i++) {
                    addConsumer();
                }
            }
        });

        frame.add(addProducerButton);
        frame.add(addConsumersButton);
        frame.add(removeProducerButton);
        frame.add(progressBar);
        frame.add(scrollPane);
        frame.add(textFileScrollPane);

        frame.setVisible(true);

        timer.start();
    }

    private static void updateProgressBar() {
        int bufferSize = bufferList.size();
        int maxBufferSize = 100; // Adjust this if needed
        int percentage = (int) (bufferSize * 100.0 / maxBufferSize);
        progressBar.setValue(percentage);
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
    private static void logProducersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/files/producers.txt"))) {
            writer.write("Amount of Producers: " + producers.size() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadFileContent() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/producers.txt"))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textfileOutput.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
