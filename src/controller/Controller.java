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

//TODO -- 1 -- Lägga till färgmarkeringar om antalet enheter blir för högt/lågt
//TODO -- 1 -- Organisera koden så att den följer MVC och facade pattern
//TODO -- 2 -- Skriva en kort beskrivning av programmet på GitHub
//TODO -- 2 -- Skriva en längre beskrivning av min lösning på google drive
//TODO -- 2 -- Skapa ett klassdiagram
    static Queue<Item> bufferList = new LinkedList<Item>();
    private static final Buffer buffer = new Buffer(bufferList);
    private static List<Producer> producers;
    private static JTextArea textfileOutput;
    private static JProgressBar progressBar;
    private static Timer timer1;
    private static Timer timer10;
    private static int inventoryPercentage;
    private static StringBuilder newFileContent;

    public Controller() {
        SwingUtilities.invokeLater(Controller::createAndShowGUI);

        timer1 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgressBar();
            }
        });
        timer10 = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logEventToFile("showInventory");
            }
        });
    }


    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Producer-Consumer Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new FlowLayout());

        JButton addProducerButton = new JButton("Add Producer");
        JButton addConsumersButton = new JButton("Start");
        JButton removeProducerButton = new JButton("Remove Producer");

        producers = new ArrayList<>();

        newFileContent = new StringBuilder();
        textfileOutput = new JTextArea(10, 50);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        JScrollPane textFileScrollPane = new JScrollPane(textfileOutput);

        logProducersToFile();

        addProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProducer();
                logEventToFile("added");
            }
        });
        removeProducerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!producers.isEmpty()) {
                    removeProducer();
                    logEventToFile("removed");
                } else {
//                    consoleOutput.append("No producers to remove\n");
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
        frame.add(textFileScrollPane);

        frame.setVisible(true);

        timer1.start();
        timer10.start();
    }

    private static void updateProgressBar() {
        int bufferSize = bufferList.size();
        int maxBufferSize = 100; // Adjust this if needed
        inventoryPercentage = (int) (bufferSize * 100.0 / maxBufferSize);
        progressBar.setValue(inventoryPercentage);
    }

    private static void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
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

    private static void logEventToFile(String event) {
        newFileContent = new StringBuilder();
        switch (event) {
            case "added" -> newFileContent.append("Producer added\n");
            case "removed" -> newFileContent.append("Producer removed\n");
            case "showInventory" ->
                    newFileContent.append("Inventory is at ").append(inventoryPercentage).append("% capacity\n");
        }
        logProducersToFile();
    }

    private static void logProducersToFile() {
        newFileContent.append("\n Amount of Producers: ").append(producers.size()).append("\n").append("If the amount of items in inventory goes down to 10% then the consumers stop buying \n").append("If it goes above 90% then the producers stop producing \n");
        int count = 1;
        for (Producer producer : producers) {
            newFileContent.append("Producer").append(count).append(" frequency: ").append(producer.getFrequency()).append("\n");
            count++;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/files/producers.txt"))) {
            writer.write(newFileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadFileContent();
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
