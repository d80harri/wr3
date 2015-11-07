package net.d80harri.wr.ui.itemtree.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import net.d80harri.wr.ui.core.IView;
import net.d80harri.wr.ui.itemtree.cell.ITreeItemCellView.TreeItemCellEvent;

public interface ITreeItemCellView extends IView<ITreeItemCellPresenter, ITreeItemCellView> {

	public static class TreeItemCellEvent extends Event {
		private static final long serialVersionUID = 3699285621625278032L;

		public static final EventType<TreeItemCellEvent> BASE = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				"BASE");

		public static final EventType<TreeItemCellEvent> INDENT = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_CHILD_OF_PREVIOUS");

		public static final EventType<TreeItemCellEvent> DELETE = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "DELETE");

		public static final EventType<TreeItemCellEvent> MERGEWITH_PREVIOUS = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MERGEWITH_PREVIOUS");

		public static final EventType<TreeItemCellEvent> MERGEWITH_NEXT = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MERGEWITH_NEXT");

		public static final EventType<TreeItemCellEvent> OUTDENT = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_PARENT");

		public static final EventType<TreeItemCellEvent> MOVE_DOWN = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_NEXT");

		public static final EventType<TreeItemCellEvent> MOVE_UP = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "MOVETO_PREVIOUS");

		public static final EventType<TreeItemCellEvent> CREATE_AFTER = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "CREATE_AFTER");

		public static final EventType<TreeItemCellEvent> GOTO_PREVIOUS = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "GOTO_PREVIOUS");

		public static final EventType<TreeItemCellEvent> GOTO_NEXT = new EventType<ITreeItemCellView.TreeItemCellEvent>(
				BASE, "GOTO_NEXT");

		public static final EventType<TreeItemCellEvent> TOGGLE_EXPAND = new EventType<ITreeItemCellView.TreeItemCellEvent>(
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

	public abstract void setActivated(final boolean activated);

	public abstract boolean isActivated();

	public abstract BooleanProperty activatedProperty();

	public abstract void setTitle(String title);

	public abstract String getTitle();

	public abstract StringProperty titleProperty();

	public abstract void appendToTitle(String title);

	public <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler);

}
