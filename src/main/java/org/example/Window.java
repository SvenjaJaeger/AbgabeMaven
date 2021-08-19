package org.example;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Window extends Parent {
    private final int DISC_SIZE = 80;

    private Pane discRoot;
    private Text status1;
    private Text status2;

    private Game game;


    public Window() {

        super();
//Pane = root Node
        Pane gamePane = new Pane();
        discRoot = new Pane();
        gamePane.getChildren().add(discRoot);
        gamePane.getChildren().add(makeGrid());
        gamePane.getChildren().addAll(makeColumns());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setMinSize(125, 125);
        grid.setPadding(new Insets(10));
        grid.add(gamePane, 0, 0);

        status1 = new Text();
        status2 = new Text();

        getChildren().add(grid);

    }


    /**
     * Hier wird das Feld erstellt
     * +1 to make it bigger than the actual grid
     * set Center um von links oben zu starten
     */
    private Shape makeGrid() {
        Shape shape = new Rectangle((game.getCols() + 1) * DISC_SIZE, (game.getRows() + 1) * DISC_SIZE);
        for (int y = 0; y < game.getRows(); y++) {
            for (int x = 0; x < game.getCols(); x++) {
                Circle circle = new Circle(DISC_SIZE / 2);
                circle.setCenterX(DISC_SIZE / 2);
                circle.setCenterY(DISC_SIZE / 2);
                circle.setTranslateX(x * (DISC_SIZE + 5) + DISC_SIZE / 4);
                circle.setTranslateY(y * (DISC_SIZE + 5) + DISC_SIZE / 4);
                shape = Shape.subtract(shape, circle);
            }
        }
        shape.setFill(Color.DARKBLUE);
        return shape;
    }

    /**
     * @DIe SCheiben werden erstellt
     */
    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();
        for (int x = 0; x < game.getCols(); x++) {
            Rectangle rect = new Rectangle(DISC_SIZE, (game.getRows() + 1) * DISC_SIZE);
            rect.setTranslateX(x * (DISC_SIZE + 5) + DISC_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);
            final int column = x;
            rect.setOnMouseClicked(e -> humanMove(column));
            list.add(rect);
        }
        return list;
    }

    /**
     * Fled ist zu beginn leer bzw ohne sichttbare Scheiben
     */
    private void newGame(boolean c1, boolean c2) {
        discRoot.getChildren().clear();
        game = new Game(c1, c2,
                (Color color,boolean animated,boolean marker,int column,int row) -> placeDisc(color, animated, marker, column, row),
                (String s) -> status2.setText(s));
        if (c1 && c2) computerMove();
    }

    /**
     * Spiel beginnt sobald der Spiler den ersten Zug gemacht hat
     * Zug ist fertig, wenn der Computer ebenfalls gesetzt hta
     */
    private void humanMove(int col) {
        if (game == null) {
            newGame(false,true);
        }
        if (game.humanMove(col)) {
            if (game.nextIsComputer()) {
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), (e) -> computerMove()));
                timer.play();
            }
        }
    }

    /**
     * Computer wird aufgefordert in 1sec zu setzten
     */
    private void computerMove() {
        if (game.computerMove()) {
            if (game.nextIsComputer()) {
                Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), (e) -> computerMove()));
                timer.play();
            }
        }
    }

    /**
     * Es wird überprüft ob ob die stelle schon einen wert hat, bzw ob sie schon besetzt ist
     * wenn nicht, wird diese bestzt und die fallende animation startet
     */
    private void placeDisc(Color color, boolean animated, boolean marked, int column, int row) {
        Circle disc = new Circle(DISC_SIZE / (marked?4:2), color);
        disc.setCenterX(DISC_SIZE / 2);
        disc.setCenterY(DISC_SIZE / 2);
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (DISC_SIZE + 5) + DISC_SIZE / 4);
        if (marked || !animated) {
            disc.setTranslateY((Game.getRows()-row-1) * (DISC_SIZE + 5) + DISC_SIZE / 4);
        }
        else {
            TranslateTransition animation = new TranslateTransition(Duration.seconds(0.6), disc);
            animation.setToY((Game.getRows()-row-1) * (DISC_SIZE + 5) + DISC_SIZE / 4);
            animation.play();
        }
    }

}

