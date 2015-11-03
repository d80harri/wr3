package net.d80harri.wr.ui.itemtree.cell;

import net.d80harri.wr.service.Service;
import net.d80harri.wr.service.model.ItemDto;
import net.d80harri.wr.ui.core.PresenterBase;

public class TreeItemCellPresenter extends PresenterBase<ItemDto, ITreeItemCellView> implements ITreeItemCellPresenter {

	private Service service;
	
	public TreeItemCellPresenter(ItemDto model, ITreeItemCellView view) {
		super(model, view);
	}
	
	public void saveOrUpdate() {
		ItemDto item = new ItemDto();
		service.saveOrUpdate(item);
	}
	
	public Service getService() {
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
}
