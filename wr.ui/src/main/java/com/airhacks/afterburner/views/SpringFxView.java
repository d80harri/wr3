package com.airhacks.afterburner.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import net.d80harri.wr.ui.core.ApplicationContextProvider;
import net.d80harri.wr.ui.itemtree.ItemTreePresenter;
import javafx.fxml.FXMLLoader;

public class SpringFxView extends FXMLView {
	FXMLLoader loadSynchronously(final URL resource, ResourceBundle bundle, final String conventionalName) throws IllegalStateException {
        final FXMLLoader loader = new FXMLLoader(resource, bundle);
        loader.setControllerFactory((Class<?> p) -> ApplicationContextProvider.getApplicationContext().getBean(p));
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load " + conventionalName, ex);
        }
        return loader;
    }
}
