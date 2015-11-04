package net.d80harri.wr.ui.itemtree.cell;

import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.ui.core.PresenterBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TreeItemCellPresenter extends PresenterBase<ItemDto, ITreeItemCellView> implements ITreeItemCellPresenter {

	@Autowired
	private Service service;
	
	public TreeItemCellPresenter(ITreeItemCellView view) {
		super(new ItemDto(), view);
	}
	
	public void initialize() {
		getView().titleProperty().addListener((obs, o, n) -> getModel().setTitle(n));		
	}
	
	public void saveOrUpdate() {
		setModel(service.saveOrUpdate(getModel()));
	}
	
	public Service getService() {
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
}
