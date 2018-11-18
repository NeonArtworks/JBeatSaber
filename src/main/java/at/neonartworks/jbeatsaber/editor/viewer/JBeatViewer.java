package at.neonartworks.jbeatsaber.editor.viewer;

import java.io.File;

import org.fxyz3d.shapes.primitives.CubeMesh;
import org.fxyz3d.utils.CameraTransformer;

import at.neonartworks.jbeatsaber.core.audio.AudioPlayer;
import at.neonartworks.jbeatsaber.json.level.Level;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.note.NoteType;
import at.neonartworks.jbeatsaber.util.ContentWriter;
import at.neonartworks.jbeatsaber.util.Xform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class JBeatViewer extends AnchorPane
{
	public static final int TOOLBAR_WIDTH = 150;
	private String PATH = " ";

	private final double CONTROL_MULTIPLIER = 0.1;
	private final double SHIFT_MULTIPLIER = 10.0;
	private final double MOUSE_SPEED = 0.1;
	private final double ROTATION_SPEED = 1.0;
	private final double TRACK_SPEED = 0.3;

	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	private double bpms = 1;
	private double CAMERA_INITIAL_DISTANCE = -1200;
	private double CAMERA_INITIAL_DISTANCE2 = 0;
	private final double CAMERA_INITIAL_X_ANGLE = 0;
	private final double CAMERA_INITIAL_Y_ANGLE = 0;
	private final double CAMERA_NEAR_CLIP = 0.1;
	private final double CAMERA_FAR_CLIP = 10000.0;
	private AudioPlayer audioPlayer = new AudioPlayer();
	private final Xform cameraXform = new Xform();
	private final Xform cameraXform2 = new Xform();
	private final Xform cameraXform3 = new Xform();
	private PerspectiveCamera camera;
	private CameraTransformer cameraTransform;;
	private Translate pivot;
	private ContentWriter cw = new ContentWriter();
	private Group beatGroup = new Group();
	private Rectangle rect = new Rectangle();
	private AnchorPane parent;

	public JBeatViewer(AnchorPane parent)
	{
		this.parent = parent;
		this.parent.getChildren().add(this);
		rect.setWidth(720d);
		rect.setHeight(490d);
		rect.setFill(Color.BLACK);
		//this.parent.getChildren().add(rect);
		this.parent.getChildren().add(beatGroup);
		setBottomAnchor(this.parent, 0d);
		setTopAnchor(this.parent, 0d);
		setLeftAnchor(this.parent, 0d);
		setRightAnchor(this.parent, 0d);
		this.parent.setBottomAnchor(this, 0d);
		this.parent.setTopAnchor(this, 0d);
		this.parent.setLeftAnchor(this, 0d);
		this.parent.setRightAnchor(this, 0d);
		init();

	}

	public void loadSong(String path)
	{
		PATH = path;
		beatGroup.getChildren().clear();
		Level level = cw.readJson(new File(PATH + "Expert.json"));
		double bpm = level.getTrack().getBeatsPerMinute();
		double bpb = level.getTrack().getBeatsPerBar();
		bpms = (bpm * bpb) / 60000d;

		if (level != null)
			for (Note n : level.getTrack().getNotes())
			{
				CubeMesh cubeMesh = new CubeMesh(10);
				double offset = Note.beats2ms(n.getTime(), bpm);
				cubeMesh.setTranslateZ(offset);
				cubeMesh.setTranslateX(n.getHorizontalPosition() * 10d);
				cubeMesh.setTranslateY(n.getVerticalPosition() * 10d);
				PhongMaterial material = new PhongMaterial();
				if (n.getType() == NoteType.BLUE.getNoteType())
					material.setDiffuseColor(Color.BLUE);
				else
					material.setDiffuseColor(Color.RED);
				cubeMesh.setMaterial(material);
				beatGroup.getChildren().add(cubeMesh);

			}
		initChildren();
	}

	private void init()
	{
		camera = new PerspectiveCamera(true);
		cameraTransform = new CameraTransformer();
		pivot = new Translate();
		initCamera();
		setMouseEventsToScene(this.parent, camera);

	}

	private void initChildren()
	{
		this.parent.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		this.parent.getChildren().stream().filter(node -> !(node instanceof Camera)).forEach(node -> node.setOnMouseClicked(event ->
			{
				pivot.setX(node.getTranslateX());
				pivot.setY(node.getTranslateY());
				pivot.setZ(node.getTranslateZ());
			}));
		//this.parent.getChildren().add(beatGroup);
	}

	private void initCamera()
	{
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateX(0);
		camera.setTranslateZ(-100);
		camera.setFieldOfView(20);
		cameraTransform.getChildren().add(camera);
		cameraTransform.ry.setAngle(-30.0);
		cameraTransform.rx.setAngle(-15.0);
	}

	private void setMouseEventsToScene(AnchorPane parent, PerspectiveCamera camera)
	{
		parent.setOnMousePressed(me ->
			{

				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			});
		parent.setOnScroll(event ->
			{
				if (event.getDeltaY() < 0)
				{
					CAMERA_INITIAL_DISTANCE *= 1.1;
				} else
				{
					CAMERA_INITIAL_DISTANCE *= 0.9;
				}
				camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
			});
		parent.setOnMouseDragged(me ->
			{
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);
				double modifier = 1.0;
				if (me.isControlDown())
				{
					modifier = CONTROL_MULTIPLIER;
				}
				if (me.isShiftDown())
				{
					modifier = SHIFT_MULTIPLIER;
				}
				if (me.isPrimaryButtonDown())
				{

					cameraXform.ry.setAngle(
							cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * modifier * ROTATION_SPEED);
					cameraXform.rx.setAngle(
							cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * modifier * ROTATION_SPEED);
				} else if (me.isSecondaryButtonDown())
				{
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
					camera.setTranslateZ(newZ);
				} else if (me.isMiddleButtonDown())
				{
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
				}
			});
		parent.setOnKeyPressed(me ->
			{

				double transY = camera.getTranslateY();
				double transZ = camera.getTranslateZ();
				double modifier = 1.0;
				if (me.isControlDown())
				{
					modifier = CONTROL_MULTIPLIER;
				}
				if (me.isShiftDown())
				{
					modifier = SHIFT_MULTIPLIER;
				}
				switch (me.getCode())
				{
				case UP:
					camera.setTranslateY(transY -= 10 * modifier);
					break;
				case DOWN:
					camera.setTranslateY(transY += 10 * modifier);
					break;
				case LEFT:
					camera.setTranslateX(CAMERA_INITIAL_DISTANCE2 -= 5 * modifier);
					break;
				case RIGHT:
					camera.setTranslateX(CAMERA_INITIAL_DISTANCE2 += 5 * modifier);
					break;
				case W:
					camera.setTranslateZ(transZ += 10 * modifier);
					break;
				case S:
					camera.setTranslateZ(transZ -= 10 * modifier);
					break;
				case ENTER:
					Thread thread = new Thread()
					{
						public void run()
						{
							audioPlayer.play(PATH + "song.ogg");

						}
					};
					thread.setDaemon(true);
					thread.start();

					Timeline fiveSecondsWonder = new Timeline(
							new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>()
							{

								@Override
								public void handle(ActionEvent event)
								{
									double transZ = camera.getTranslateZ();
									camera.setTranslateZ(transZ += 1);
								}
							}));
					fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
					fiveSecondsWonder.play();
					break;
				}
			});
		parent.setOnMouseClicked(me ->
			{
				if (me.getButton() == MouseButton.SECONDARY)
				{
					// CAMERA_INITIAL_DISTANCE = -1200;
					// camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
					cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
					cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
				}
			});
	}

}
