package com.catnbear.utilities;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains methods for GUI modification.
 */
public class GuiModifier {

    /**
     * Disables all the Pane's buttons.
     * @param pane Parent Pane of buttons to be disabled.
     */
    public static void disablePaneButtons(Pane pane) {
        for (Node node : getAllNodes(pane)) {
            if (isButton(node)){
                node.setDisable(true);
            }
        }
    }

    /**
     * Enables all the Pane's buttons.
     * @param pane Parent Pane of buttons to be disabled.
     */
    public static void enablePaneButtons(Pane pane) {
        for (Node node : getAllNodes(pane)) {
            if (isButton(node)){
                node.setDisable(false);
            }
        }
    }

    /**
     * Enables all the GUI elements.
     * @param pane Pane of GUI elements to enable.
     */
    public static void enableAllGuiElements(Pane pane) {
        for (Node node : getAllNodes(pane)) {
            node.setDisable(false);
        }
    }

    /**
     * Disables all the GUI elements.
     * @param pane Pane of GUI elements to disable.
     */
    public static void disableAllGuiElements(Pane pane) {
        for (Node node : getAllNodes(pane)) {
            node.setDisable(true);
        }
    }

    /**
     * Changes active window to the chosen one.
     * @param pane Pane, which will be changed.
     * @param newWindowPath Path to the new window.
     * @param callingController FXML file's controller object which calls the method.
     */
    public static void changeWindow(Pane pane, String newWindowPath, Object callingController) {
        Pane parentPane = (Pane) pane.getParent();
        Window window = parentPane.getScene().getWindow();
        double x = window.getX() + getHorizontalMidpoint(window);
        double y = window.getY() + getVerticalMidpoint(window);

        ObservableList<Node> childrenList = parentPane.getChildren();
        removeAllIncludedChildren(childrenList);

        FXMLLoader loader = new FXMLLoader(callingController.getClass().getResource(newWindowPath));

        try {
            parentPane.getChildren().add(loader.load());
            window.sizeToScene();
            window.setX(x - getHorizontalMidpoint(window));
            window.setY(y - getVerticalMidpoint(window));

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static double getHorizontalMidpoint(Window window) {
        int horizontalBisectionCoefficient = 2;
        return window.getWidth() / horizontalBisectionCoefficient;
    }

    private static double getVerticalMidpoint(Window window) {
        int verticalBisectionCoefficient = 2;
        return window.getHeight() / verticalBisectionCoefficient;
    }

    /**
     * Generates ArrayList of Parent's Nodes.
     * @param root Parent of Nodes to be returned.
     * @return ArrayList of the given Parent's Nodes.
     */
    private static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendants(root, nodes);
        return nodes;
    }

    /**
     * Adds all the Parent's descendants to the given ArrayList.
     * @param parent Parent of Nodes to be returned.
     * @param nodes ArrayList of the given Parent's Nodes.
     */
    private static void addAllDescendants(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendants((Parent)node, nodes);
        }
    }

    /**
     * Checks if Node is an instance of the Button class.
     * @param node Node to be checked.
     * @return True if Node is an instance of the Button class. False if it's not.
     */
    private static boolean isButton(Node node) {
        return node.getClass().equals(Button.class);
    }

    /**
     * Removes all children included in pane's ObservableList.
     * @param childrenList List of children to be removed.
     */
    private static void removeAllIncludedChildren(ObservableList<Node> childrenList) {
        for (int childIndex = 0; childIndex < childrenList.size(); childIndex++) {
            childrenList.remove(childIndex);
        }
    }
}
