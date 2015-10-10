package net.d80harri.wr.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import net.d80harri.wr.ui.core.ServiceProxy;

public class AppPresenter implements Initializable {

	private ServiceProxy serviceProxy;
	
	public ServiceProxy getServiceProxy() {
		return serviceProxy;
	}
	
	public void setServiceProxy(ServiceProxy serviceProxy) {
		this.serviceProxy = serviceProxy;
	}
	
	@FXML
	private Label label;
	
	public void initialize(URL location, ResourceBundle resources) {
		label.setText("" + serviceProxy.hashCode());
	}



}
