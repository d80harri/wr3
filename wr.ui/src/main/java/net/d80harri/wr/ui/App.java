package net.d80harri.wr.ui;

import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.util.SpringAwareBeanMapper;
import net.d80harri.wr.ui.core.WrUiAppContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	private WrUiAppContext context = WrUiAppContext.get();
	
    @Override
    public void start(Stage stage) throws Exception {
    	AppView appView = new AppView(new AppPresenter(context.getBean(Service.class), context.getBean(SpringAwareBeanMapper.class)));
        Scene scene = new Scene(appView);
        stage.setTitle("White rabbit v0.0.1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}