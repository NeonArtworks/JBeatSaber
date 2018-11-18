package at.neonartworks.jbeatsaber.editor.handler;

import at.neonartworks.jbeatsaber.util.DragContext;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * <h1>GNodeMouseHandler</h1>
 * <p>
 * This class is responsible for Node Handling. Especially the moving inside of
 * the NodeGraph
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class BeatHandler
{
	private static DragContext dragContext = new DragContext();

	public BeatHandler()
	{
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>()
	{

		public void handle(MouseEvent event)
		{
			Node node = (Node) event.getSource();
			System.out.println("Pressed");
			if (event.isPrimaryButtonDown())
			{

				double scale = 1;
				dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
				dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

				node.setCursor(Cursor.HAND);
			}
		}

	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>()
	{

		public void handle(MouseEvent event)
		{
			Node n = (Node) event.getSource();
			System.out.println("Dragged!");
			if (event.isPrimaryButtonDown())
			{
				n.setCursor(Cursor.MOVE);

				double offsetX = event.getScreenX() + dragContext.x;
				double offsetY = event.getScreenY() + dragContext.y;

				// adjust the offset in case we are zoomed
				double scale = 1;

				offsetX /= scale;
				offsetY /= scale;

				n.relocate(offsetX, offsetY);
			}

		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>()
	{

		public void handle(MouseEvent event)
		{
			System.out.println("Realeased!");
		}
	};

	public void makeMoveable(final Node node)
	{
		
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);

	}

}