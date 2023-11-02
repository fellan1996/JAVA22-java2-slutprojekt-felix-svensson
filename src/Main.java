import controller.Controller;
import view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI view = GUI.getInstance();
            Controller controller = new Controller(view);
            view.createAndShowGUI();
        });
    }
}