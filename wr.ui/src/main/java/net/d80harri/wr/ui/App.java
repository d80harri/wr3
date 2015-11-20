package net.d80harri.wr.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	AppView appView = new AppView(new AppPresenter());
        Scene scene = new Scene(appView);
        stage.setTitle("White rabbit v0.0.1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}