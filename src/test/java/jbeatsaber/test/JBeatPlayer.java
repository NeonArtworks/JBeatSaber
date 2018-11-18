package jbeatsaber.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.fxyz3d.shapes.primitives.SpheroidMesh;
import org.fxyz3d.utils.CameraTransformer;

import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import at.neonartworks.jbeatsaber.core.audio.AudioPlayer;
import at.neonartworks.jbeatsaber.editor.Arrow;
import at.neonartworks.jbeatsaber.editor.BeatBlock;
import at.neonartworks.jbeatsaber.editor.handler.BeatHandler;
import at.neonartworks.jbeatsaber.json.level.Level;
import at.neonartworks.jbeatsaber.json.note.CutDirection;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.note.NoteType;
import at.neonartworks.jbeatsaber.json.obstacle.Obstacle;
import at.neonartworks.jbeatsaber.json.obstacle.ObstacleType;
import at.neonartworks.jbeatsaber.util.ContentWriter;
import at.neonartworks.jbeatsaber.util.Xform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JBeatPlayer extends Application
{
	public static final int TOOLBAR_WIDTH = 150;
	private final String PATH = "S:\\_4_tmp_tmp\\BeatSaber Songs\\Skyrim Theme\\";
	private final String SONG = "song.ogg";
	private final String LEVEL = "Expert.json";
	private final double CONTROL_MULTIPLIER = 0.1;
	private final double SHIFT_MULTIPLIER = 100.0;
	private final double MOUSE_SPEED = 0.1;
	private final double ROTATION_SPEED = 1.0;
	private final double TRACK_SPEED = 0.3;
	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
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
	private MeshView[] mesh;
	private ModelImporter importer = new ObjModelImporter();
	private ModelImporter arrorImporter = new ObjModelImporter();
	private Group group;
	private List<BeatBlock> missedBlocks = new ArrayList<BeatBlock>();
	private int cnt = 0;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		PerspectiveCamera camera = new PerspectiveCamera(true);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateX(15);
		camera.setFieldOfView(5);

		CameraTransformer cameraTransform = new CameraTransformer();
		cameraTransform.getChildren().add(camera);
		cameraTransform.ry.setAngle(-30.0);
		cameraTransform.rx.setAngle(-15.0);
		Translate pivot = new Translate();
		ContentWriter cw = new ContentWriter();

		group = new Group(cameraTransform);

		Level level = cw.readJson(new File(PATH + LEVEL));
		double bpm = level.getTrack().getBeatsPerMinute();

		importer.read(JBeatPlayer.class.getResource("/scene.obj"));
		mesh = (MeshView[]) importer.getImport();
		for (MeshView mv : mesh)
		{
			mv.setScaleX(0.1d);
			mv.setScaleY(0.1d);
			mv.setScaleZ(0.1d);
			mv.setTranslateZ(8000d);
			mv.setTranslateX(15d);
			mv.setRotationAxis(new Point3D(0, 1, 0));
			mv.setRotate(180d);
		}

		camera.setTranslateZ(-bpm);

		double bpb = level.getTrack().getBeatsPerBar();
		bpms = (bpm * bpb) / 60000d;
		int totNotes = 0;
		int blueNotes = 0;
		int redNotes = 0;
		int bombNotes = 0;
		int obsCount = 0;
		Box groundPlane = new Box();

		groundPlane.setWidth(40d);
		groundPlane.setDepth(100000d);
		groundPlane.setHeight(1d);
		groundPlane.setTranslateX(15d);
		groundPlane.setTranslateY(20d);
		PhongMaterial groundMat = new PhongMaterial();
		groundMat.setDiffuseColor(new Color(1, 1, 1, .6));
		groundPlane.setMaterial(groundMat);
		group.getChildren().add(groundPlane);
		group.getChildren().addAll(mesh);
		group.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		double lastCubePos = 0;
		if (level != null)
			for (Obstacle obs : level.getTrack().getObstacles())
			{
				Box cubeMesh = new Box();
				// System.out.println(obs.getWidth() * 10d);
				cubeMesh.setWidth(obs.getWidth() * 10d);
				if (obs.getType() == ObstacleType.CEILING.getObstacleType())
				{
					cubeMesh.setHeight(20d);
					cubeMesh.setTranslateY(-20d);
				} else
				{
					cubeMesh.setHeight(40d);
				}

				BeatHandler handler = new BeatHandler();
				double offset = Note.beats2ms(obs.getTime(), bpm);
				if (offset > lastCubePos)
					lastCubePos = offset;
				cubeMesh.setTranslateZ(offset);

				cubeMesh.setTranslateX(obs.getHorizontalPosition() * 10d);

				cubeMesh.setDepth(Note.beats2ms(obs.getDuration(), bpm));

				PhongMaterial material = new PhongMaterial();
				material.setDiffuseColor(new Color(1, .7, .0, .4));
				Glow blur = new Glow(100);
				cubeMesh.setMaterial(material);
				// handler.makeMoveable(cubeMesh);
				group.getChildren().add(cubeMesh);
				cubeMesh.setEffect(blur);
				obsCount++;
			}

		importer.read(JBeatPlayer.class.getResource("/cube.obj"));
		arrorImporter.read(JBeatPlayer.class.getResource("/arrow.obj"));

		MeshView[] cube = (MeshView[]) importer.getImport();
		MeshView[] arrow = (MeshView[]) arrorImporter.getImport();
		int i = 0;
		for (Note n : level.getTrack().getNotes())
		{
			if (n.getType() != NoteType.BOMB.getNoteType())
			{

				BeatBlock cubeMesh = new BeatBlock(cube[0].getMesh());
				Arrow arrowMesh = new Arrow(arrow[0].getMesh());
				arrowMesh.setBeatBlock(cubeMesh);
				double arrorOff = 10d;

				arrowMesh.setScaleX(20d);
				arrowMesh.setScaleY(20d);
				arrowMesh.setScaleZ(20d);

				cubeMesh.setScaleX(20d);
				cubeMesh.setScaleY(20d);
				cubeMesh.setScaleZ(20d);

				boolean dontadd = false;

				double offset = Note.beats2ms(n.getTime(), bpm);
				if (offset > lastCubePos)
					lastCubePos = offset;

				CutDirection dir = CutDirection.getByID(n.getCutDirection());
				arrowMesh.setRotationAxis(new Point3D(0, 0, 1));

				if (dir.equals(CutDirection.UP))
				{
					arrowMesh.setRotate(180);
				} else if (dir.equals(CutDirection.DOWN))
				{
					arrowMesh.setRotate(0);
				} else if (dir.equals(CutDirection.DOWN_LEFT))
				{
					arrowMesh.setRotate(-45);
				} else if (dir.equals(CutDirection.DOWN_RIGHT))
				{
					arrowMesh.setRotate(45);
				} else if (dir.equals(CutDirection.UP_LEFT))
				{
					arrowMesh.setRotate(225);
				} else if (dir.equals(CutDirection.UP_RIGHT))
				{
					arrowMesh.setRotate(135);
				} else if (dir.equals(CutDirection.LEFT))
				{
					arrowMesh.setRotate(-90);
				} else if (dir.equals(CutDirection.RIGHT))
				{
					arrowMesh.setRotate(90);
				} else if (dir.equals(CutDirection.NO_DIRECTION))
				{
					dontadd = true;
				}
				arrowMesh.setCutDirection(dir);
				arrowMesh.setTranslateZ(offset - arrorOff);
				arrowMesh.setTranslateX(n.getHorizontalPosition() * 10d);
				arrowMesh.setTranslateY((n.getVerticalPosition() * 10d) - 5d);

				cubeMesh.setTranslateZ(offset);
				cubeMesh.setTranslateX(n.getHorizontalPosition() * 10d);
				cubeMesh.setTranslateY((n.getVerticalPosition() * 10d) - 5d);

				PhongMaterial material = new PhongMaterial();

				if (n.getType() == NoteType.BLUE.getNoteType())
				{
					material.setDiffuseColor(Color.BLUE);
					material.setSpecularColor(Color.BLUE);
					material.setSpecularPower(2d);
					blueNotes++;
				} else
				{
					material.setDiffuseColor(Color.RED);
					material.setSpecularColor(Color.RED);
					material.setSpecularPower(2d);
					redNotes++;
				}

				cubeMesh.setMaterial(material);
				// handler.makeMoveable(cubeMesh);
				group.getChildren().add(cubeMesh);

				if (!dontadd)
				{
					cubeMesh.setArrowMesh(arrowMesh);
					group.getChildren().add(arrowMesh);
				}
				cubeMesh.setDepthTest(DepthTest.ENABLE);
				// activateShape(cubeMesh, "cubeMesh " + i++);
				cubeMesh.toFront();
				arrowMesh.toFront();
			} else
			{
				SpheroidMesh sphere = new SpheroidMesh(10);
				double offset = Note.beats2ms(n.getTime(), bpm);
				if (offset > lastCubePos)
					lastCubePos = offset;
				sphere.setTranslateZ(offset);
				sphere.setTranslateX(n.getHorizontalPosition() * 10d);
				sphere.setTranslateY(n.getVerticalPosition() * 10d);
				PhongMaterial material = new PhongMaterial();
				sphere.setDepthTest(DepthTest.ENABLE);
				material.setDiffuseColor(Color.GRAY);
				sphere.setMaterial(material);
				// handler.makeMoveable(sphere);
				group.getChildren().add(sphere);
				// activateShape(sphere, "bombMesh " + i++);
				sphere.toFront();
				bombNotes++;
			}
			totNotes++;
		}
		groundPlane.setDepth(lastCubePos + 500d);
		groundPlane.setTranslateZ((lastCubePos + 100d) / 2);
		group.toFront();

		System.out.println("Loaded Notes: " + totNotes);
		System.out.println("Blue Notes: " + blueNotes);
		System.out.println("Red Notes: " + redNotes);
		System.out.println("Bomb Notes: " + bombNotes);
		System.out.println("Loaded Obstacles: " + obsCount);

		group.setDepthTest(DepthTest.ENABLE);

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(new Color(0.06, 0.06, 0.1, 1));
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
		primaryStage.setTitle("JBeatPlayer");
		primaryStage.show();
		primaryStage.requestFocus();
		arrorImporter.close();
		importer.close();
	}

	public void setMouseEventsToScene(Scene scene, PerspectiveCamera camera)
	{

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
				case R:
					camera.setFieldOfView(camera.getFieldOfView() + 1);
					break;
				case T:
					camera.setFieldOfView(camera.getFieldOfView() - 1);
					break;
				case ENTER:
					Thread thread = new Thread()
					{
						public void run()
						{
							audioPlayer.play(PATH + SONG);

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
									for (MeshView mv : mesh)
									{
										double tmp = mv.getTranslateZ();
										mv.setTranslateZ(tmp += 1);
									}

									for (Node n : group.getChildren())
									{
										if (n instanceof BeatBlock)
										{
											if (n.getTranslateZ() < camera.getTranslateZ())
											{

												System.out.println("MISSED BLOCK!");
												missedBlocks.add((BeatBlock) n);
											}
										}
									}
									group.getChildren().removeAll(missedBlocks);
									missedBlocks.clear();

								}
							}));
					fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
					fiveSecondsWonder.play();
					break;
				}
			});
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
				if (cnt >= 4)
				{
					mousePosX = me.getSceneX();
					mousePosY = me.getSceneY();
					mouseDeltaX = (mousePosX - mouseOldX);
					mouseDeltaY = (mousePosY - mouseOldY);
					mouseOldX = mousePosX;
					mouseOldY = mousePosY;
					cnt = 0;
				}
				cnt++;
				double modifier = 1.0;
				if (me.isControlDown())
				{
					modifier = CONTROL_MULTIPLIER;
				}
				if (me.isShiftDown())
				{
					modifier = SHIFT_MULTIPLIER;

				}
				printDebugCut(mouseDeltaX, mouseDeltaY);

				PickResult res = me.getPickResult();

				if (res.getIntersectedDistance() < 1000d)
				{
					if (res.getIntersectedNode() instanceof BeatBlock)
					{
						System.out.println(res);
						BeatBlock tmp = (BeatBlock) res.getIntersectedNode();
						MeshView arrow = tmp.getArrowMesh();
						if (arrow != null)
						{
							CutDirection dir = tmp.getArrowMesh().getCutDirection();

							if (dir.equals(CutDirection.UP))
							{
								if (mouseDeltaY < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.DOWN))
							{
								if (mouseDeltaY > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.DOWN_LEFT))
							{
								if (mouseDeltaY > 0 && mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.DOWN_RIGHT))
							{
								if (mouseDeltaY > 0 && mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.UP_LEFT))
							{
								if (mouseDeltaY < 0 && mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.UP_RIGHT))
							{
								if (mouseDeltaY < 0 && mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.LEFT))
							{
								if (mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.RIGHT))
							{
								if (mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(arrow);
								}
							} else if (dir.equals(CutDirection.NO_DIRECTION))
							{
								group.getChildren().remove(tmp);
								group.getChildren().remove(arrow);
							}

						}

					}

					if (res.getIntersectedNode() instanceof Arrow)
					{
						System.out.println(res);
						Arrow tmp = (Arrow) res.getIntersectedNode();
						if (tmp.getBeatBlock() != null)
						{
							MeshView beatBlock = tmp.getBeatBlock();

							CutDirection dir = tmp.getCutDirection();

							if (dir.equals(CutDirection.UP))
							{
								if (mouseDeltaY < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.DOWN))
							{
								if (mouseDeltaY > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.DOWN_LEFT))
							{
								if (mouseDeltaY > 0 && mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.DOWN_RIGHT))
							{
								if (mouseDeltaY > 0 && mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.UP_LEFT))
							{
								if (mouseDeltaY < 0 && mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.UP_RIGHT))
							{
								if (mouseDeltaY < 0 && mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.LEFT))
							{
								if (mouseDeltaX > 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.RIGHT))
							{
								if (mouseDeltaX < 0)
								{
									group.getChildren().remove(tmp);
									group.getChildren().remove(beatBlock);
								}
							} else if (dir.equals(CutDirection.NO_DIRECTION))
							{
								group.getChildren().remove(tmp);
								group.getChildren().remove(beatBlock);
							}
						}
					}
				}
			});
		scene.setOnMouseClicked(me ->
			{

				if (me.getButton() == MouseButton.SECONDARY)
				{
					// CAMERA_INITIAL_DISTANCE = -1200;
					// camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
					camera.setTranslateX(15);
					camera.setTranslateY(-5);
					cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
					cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
				}
			});

	}

	private void printDebugCut(double mouseDeltaX, double mouseDeltaY)
	{

		if (mouseDeltaY > 0 && mouseDeltaX < 0)
		{
			System.out.println("DOWN LEFT");

		} else if (mouseDeltaY > 0 && mouseDeltaX > 0)
		{
			System.out.println("DOWN RIGHT");

		} else if (mouseDeltaY < 0 && mouseDeltaX < 0)
		{
			System.out.println("UP LEFT");

		} else if (mouseDeltaY < 0 && mouseDeltaX > 0)
		{
			System.out.println("UP RIGHT");

		} else if (mouseDeltaX < 0)
		{
			System.out.println("LEFT");

		} else if (mouseDeltaY < 0)
		{
			System.out.println("UP");

		} else if (mouseDeltaY > 0)
		{
			System.out.println("DOWN");

		} else if (mouseDeltaX > 0)
		{
			System.out.println("RIGHT");
		}

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
