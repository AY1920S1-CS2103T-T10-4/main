package seedu.address.ui.util;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;

/**
 * A class to create a bubble with contents inside it.
 */
public class BubbleGenerator {
    private String bubbleContent;
    private StackPane bubbleContainer;
    private double size;
    private double borderSize;

    public BubbleGenerator(int content, double size) {
        this.bubbleContainer = new StackPane();
        this.bubbleContent = content + "";
        this.size = size;
        this.borderSize = borderSize;
    }

    public BubbleGenerator(String content, double size) {
        this.bubbleContainer = new StackPane();
        this.bubbleContent = content;
        this.size = size;
        this.borderSize = borderSize;
    }

    public StackPane getBubble() {
        String color = ColorGenerator.generateColorList(1).get(0);
        Ellipse outer = new Ellipse(size / 2, size / 2);
        Label bubbleLabel = new Label(bubbleContent);
        bubbleLabel.setStyle("-fx-text-fill: black;");
        outer.setStyle("-fx-fill: " + color + ";");
        bubbleContainer.getChildren().addAll(outer, bubbleLabel);
        return bubbleContainer;
    }
}
