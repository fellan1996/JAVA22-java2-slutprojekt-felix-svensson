package view;

import javax.swing.*;
import java.awt.*;

import static controller.Controller.*;

public class GUI {

    private static Timer timer1;
    private static Timer timer10;
    private static JTextArea textfileOutput;
    private static JProgressBar progressBar;
    public GUI() {
//        createAndShowGUI();
        timer1 = new Timer(1000, e -> updateProgressBar());// A method in Controller
        timer10 = new Timer(10000, e -> logEventToFile("showInventory"));// A method in Controller
    }
    public void setProgressBar(int value) {
        progressBar.setValue(value);
    }
    public void setTextArea (String content) {
        textfileOutput.setText(content);
    }
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Producer-Consumer Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new FlowLayout());

        JButton addProducerButton = new JButton("Add Producer");
        JButton addConsumersButton = new JButton("Start");
        JButton removeProducerButton = new JButton("Remove Producer");

//        producers = new ArrayList<>();

//        newFileContent = new StringBuilder();
        textfileOutput = new JTextArea(30, 50);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        JScrollPane textFileScrollPane = new JScrollPane(textfileOutput);

        logProducersToFile();

        addProducerButton.addActionListener(e -> {
            addProducer();
            logEventToFile("added");
        });
        removeProducerButton.addActionListener(e -> {
            if (!producers.isEmpty()) {
                removeProducer();
                logEventToFile("removed");
            } else {
//              consoleOutput.append("No producers to remove\n");
            }
        });
        addConsumersButton.addActionListener(e -> {
            addConsumersButton.setEnabled(false); // Disable the button
            int numOfConsumers = (int) (Math.random() * 13) + 3;
            System.out.println(numOfConsumers);
            for (int i = 1; i <= numOfConsumers; i++) {
                addConsumer();
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
}