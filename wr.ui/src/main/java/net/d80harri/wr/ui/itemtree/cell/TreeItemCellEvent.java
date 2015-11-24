package net.d80harri.wr.ui.itemtree.cell;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * @author Harald
 *
 */
public class TreeItemCellEvent extends Event {
	private static final long serialVersionUID = 3699285621625278032L;

	public static final EventType<TreeItemCellEvent> BASE = new EventType<TreeItemCellEvent>(
			"BASE");

	public static final EventType<TreeItemCellEvent> OUTDENT = new EventType<TreeItemCellEvent>(
			BASE, "MOVETO_PARENT");

	public static final EventType<TreeItemCellEvent> MOVE_UP = new EventType<TreeItemCellEvent>(
			BASE, "MOVETO_PREVIOUS");

	public static final EventType<TreeItemCellEvent> CREATE_AFTER = new EventType<TreeItemCellEvent>(
			BASE, "CREATE_AFTER");
	
	public static final EventType<TreeItemCellEvent> TOGGLE_EXPAND = new EventType<TreeItemCellEvent>(
			BASE, "TOGGLE_EXPAND");

	private String title;

	public TreeItemCellEvent(
			EventType<? extends TreeItemCellEvent> eventType) {
		super(eventType);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}