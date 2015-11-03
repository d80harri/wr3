package net.d80harri.wr.ui.components;

import javafx.scene.Parent;

import org.assertj.core.api.Assertions;
import org.loadui.testfx.GuiTest;
import org.mockito.MockitoAnnotations;

public class FittingHeightTextAreaTest extends GuiTest{
	private FittingHeightTextArea view;
	
	@Override
	protected Parent getRootNode() {
		MockitoAnnotations.initMocks(this);
		view = new FittingHeightTextArea();
		return view;
	}
	
	public void shallRequestFocus() {
		view.requestFocus();
		Assertions.assertThat(view.getTextArea().isFocused()).isTrue();
	}
}
