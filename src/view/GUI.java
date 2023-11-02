package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import static controller.Controller.*;

public class GUI {

    private static GUI instance;
    private static Timer timer1;
    private static Timer timer10;
    private static JTextArea textfileOutput;
    private static JProgressBar progressBar;
    private GUI() {
//        createAndShowGUI();
        timer1 = new Timer(1000, e -> updateProgressBar());// A method in Controller
        timer10 = new Timer(10000, e -> logEventToFile("showInventory"));// A method in Controller
    }
    public static GUI getInstance() {
        if(instance == null) {
            instance = new GUI();
        }
        return instance;
    }
    public void setProgressBar(int value) {
        progressBar.setValue(value);
        if(value <= 10) {
            Border redBorder = new LineBorder(Color.RED, 2);
            progressBar.setBorder(redBorder);
        }else if (value >= 90) {
            Border greenBorder = new LineBorder(Color.GREEN, 2);
            progressBar.setBorder(greenBorder);
        }else{
            progressBar.setBorder(null);
        }
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