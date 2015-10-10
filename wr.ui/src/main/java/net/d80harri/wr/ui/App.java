package net.d80harri.wr.ui;

import net.d80harri.wr.ui.AppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.airhacks.afterburner.injection.Injector;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	AppView appView = new AppView();
        Scene scene = new Scene(appView.getView());
        stage.setTitle("White rabbit v0.0.1");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }
}