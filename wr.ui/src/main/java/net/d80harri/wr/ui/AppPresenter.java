package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import net.d80harri.wr.ui.core.ServiceProxy;
import net.d80harri.wr.ui.itemtree.ItemTreeView;

public class AppPresenter implements Initializable {

	private ServiceProxy serviceProxy;
	
	public ServiceProxy getServiceProxy() {
		return serviceProxy;
	}
	
	public void setServiceProxy(ServiceProxy serviceProxy) {
		this.serviceProxy = serviceProxy;
	}
	
	@FXML
	private AnchorPane itemTreePane;
	
	public void initialize(URL location, ResourceBundle resources) {
		itemTreePane.getChildren().add(new ItemTreeView().getView());
	}



}
