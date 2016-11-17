package com.catnbear.utilities;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import java.io.IOException;

/**
 * Contains methods for GUI modification.
 */
public class GuiModifier {

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
     * Removes all children included in pane's ObservableList.
     * @param childrenList List of children to be removed.
     */
    private static void removeAllIncludedChildren(ObservableList<Node> childrenList) {
        for (int childIndex = 0; childIndex < childrenList.size(); childIndex++) {
            childrenList.remove(childIndex);
        }
    }
}
