package jbeatsaber.test;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.fxyz3d.shapes.primitives.CubeMesh;
import org.fxyz3d.utils.CameraTransformer;

import at.neonartworks.jbeatsaber.core.audio.AudioPlayer;
import at.neonartworks.jbeatsaber.json.level.Level;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.note.NoteType;
import at.neonartworks.jbeatsaber.util.ContentWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JBeatPlayer extends Application
{
	public static final int TOOLBAR_WIDTH = 150;
	private final String PATH = "S:\\_4_tmp_tmp\\BeatSaber Songs\\Believer\\";
	
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
	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();
	private ProgressBar prBar = new ProgressBar();	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		PerspectiveCamera camera = new PerspectiveCamera(true);
		PointLight light = new PointLight(Color.WHITE);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateX(0);
		camera.setTranslateZ(-100);
		camera.setFieldOfView(20);

		CameraTransformer cameraTransform = new CameraTransformer();
		cameraTransform.getChildren().add(camera);
		cameraTransform.ry.setAngle(-30.0);
		cameraTransform.rx.setAngle(-15.0);
		Translate pivot = new Translate();
		ContentWriter cw = new ContentWriter();
		
		Group group = new Group(cameraTransform, light);
		
		System.out.println(audioPlayer.calculateDuration(new File(PATH + "song.ogg")));
		
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
				group.getChildren().add(cubeMesh);
			}

		group.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);
		group.getChildren().stream().filter(node -> !(node instanceof Camera))
				.forEach(node -> node.setOnMouseClicked(event ->
					{
						pivot.setX(node.getTranslateX());
						pivot.setY(node.getTranslateY());
						pivot.setZ(node.getTranslateZ());
					}));

		setMouseEventsToScene(scene, camera);

		primaryStage.setScene(scene);
		primaryStage.setTitle("JBeatSaber");
		primaryStage.show();
	}

	private static void playClip(File clipFile)
			throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException
	{
		class AudioListener implements LineListener
		{
			private boolean done = false;

			@Override
			public synchronized void update(LineEvent event)
			{
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE)
				{
					done = true;
					notifyAll();
				}
			}

			public synchronized void waitUntilDone() throws InterruptedException
			{
				while (!done)
				{
					wait();
				}
			}
		}
		AudioListener listener = new AudioListener();
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			try
			{
				clip.start();
				listener.waitUntilDone();
			} finally
			{
				clip.close();
			}
		} finally
		{
			audioInputStream.close();
		}
	}

	public void setMouseEventsToScene(Scene scene, PerspectiveCamera camera)
	{
		scene.setOnMousePressed(me ->
			{
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			});
		scene.setOnScroll(event ->
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
		scene.setOnMouseDragged(me ->
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
		scene.setOnKeyPressed(me ->
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
		scene.setOnMouseClicked(me ->
			{
				if (me.getButton() == MouseButton.SECONDARY)
				{
					//CAMERA_INITIAL_DISTANCE = -1200;
					//camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
					cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
					cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
				}
			});
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
