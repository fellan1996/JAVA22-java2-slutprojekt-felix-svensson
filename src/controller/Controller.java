package controller;

import model.Buffer;
import model.Consumer;
import model.Item;
import model.Producer;
import view.GUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Controller {

    //TODO -- 1 -- Lägga till klassdiagrammet på GitHub beskrivningen
//TODO -- 1 -- Skapa ett klassdiagram
//TODO -- 1 -- Jag har en massa static. Synpunkter?
//TODO -- 1 -- Var ska notify vara?
//TODO -- 1 -- Ska producers.txt med i git?
//TODO -- 1 -- Vad tycks om min Main.java? Konstigt upplägg?
//TODO -- 1 -- Tar jag bort en producer på rätt sätt?
//TODO -- 1 -- Hoppar fram och tillbaka mellan view och Controller. Onödigt? värt det? Exempel: timer1/updateProgressBar()/setProgressBar()
//TODO -- 1 -- Två stycken if-statements som gör samma sak. Värt det eller bättre att köra ihop dem i ett?
    static Queue<Item> bufferList = Buffer.getBufferList();
    private static final Buffer buffer = Buffer.getInstance();
    private static GUI view;
    public static List<Producer> producers;
    private static int inventoryPercentage;
    private static StringBuilder newFileContent;


    public static void startProgram(GUI view) {
        Controller.view = view;
        newFileContent = new StringBuilder();
        producers = new ArrayList<>();
        view.createAndShowGUI();
    }

    public static void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    public static void addProducer() {
        Producer producer = new Producer(buffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producers.add(producer);
    }

    public static void removeProducer() {
        producers.get(0).stop();
        producers.remove(0);
    }


    public static void updateProgressBar() {
        int bufferSize = bufferList.size();
        int maxBufferSize = 100; // Adjust this if needed
        inventoryPercentage = (int) (bufferSize * 100.0 / maxBufferSize);
        view.setProgressBar(inventoryPercentage);
        if (inventoryPercentage <= 10) {
            logEventToFile("low");
        } else if (inventoryPercentage >= 90) {
            logEventToFile("high");
        }
    }

    public static void logEventToFile(String event) {
        newFileContent = new StringBuilder();
        switch (event) {
            case "added" -> newFileContent.append("Producer added\n");
            case "removed" -> newFileContent.append("Producer removed\n");
            case "low" -> newFileContent.append("WARNING: Inventory low. I suggest adding some producers\n");
            case "high" ->
                    newFileContent.append("WARNING: Inventory High. I suggest pressing the start button or removing some producers\n");
            case "showInventory" ->
                    newFileContent.append("Inventory is at ").append(inventoryPercentage).append("% capacity\n");
        }
        logProducersToFile();
    }

    public static void logProducersToFile() {
        newFileContent.append("\nAmount of Producers: ").append(producers.size()).append("\nIf the amount of items in inventory goes down to 0% then the consumers stop buying \nIf it goes up to 100% then the producers stop producing \n");
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
            view.setTextArea(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
