package org.example;

/**
 * Hello world!
 *
 */
public class App extends Appl{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Window window = new Window();
        stage.setTitle("Vier Gewinnt");
        stage.setScene(new Scene(window));
        stage.show();




    }
}
