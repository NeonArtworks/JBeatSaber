package jbeatsaber.test;

import org.fxyz3d.shapes.primitives.CubeMesh;
import org.fxyz3d.shapes.primitives.SpringMesh;
import org.fxyz3d.utils.CameraTransformer;

import com.sun.javafx.sg.prism.NGPhongMaterial;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Sampler extends Application
{
	public static final int TOOLBAR_WIDTH = 150;

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

	private double CAMERA_INITIAL_DISTANCE = -1200;
	private double CAMERA_INITIAL_DISTANCE2 = 0;
	private final double CAMERA_INITIAL_X_ANGLE = 0;
	private final double CAMERA_INITIAL_Y_ANGLE = 0;
	private final double CAMERA_NEAR_CLIP = 0.1;
	private final double CAMERA_FAR_CLIP = 10000.0;

	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateX(10);
		camera.setTranslateZ(-100);

		camera.setFieldOfView(20);
		final Point anchor = new Point();
		CameraTransformer cameraTransform = new CameraTransformer();
		cameraTransform.getChildren().add(camera);
		cameraTransform.ry.setAngle(-30.0);
		cameraTransform.rx.setAngle(-15.0);
		Translate pivot = new Translate();
		SpringMesh spring = new SpringMesh(10, 2, 2, 8 * 2 * Math.PI, 200, 100, 0, 0);

		spring.setCullFace(CullFace.NONE);
		spring.setTextureModeVertices3D(1530, p -> p.f);

		Group group = new Group(cameraTransform, spring);

		group.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.BISQUE);
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
				switch (me.getCode())
				{
				case UP:
					camera.setTranslateZ(CAMERA_INITIAL_DISTANCE += 10);
					break;
				case DOWN:
					camera.setTranslateZ(CAMERA_INITIAL_DISTANCE -= 10);
					break;
				case LEFT:
					camera.setTranslateX(CAMERA_INITIAL_DISTANCE2 -= 5);
					break;
				case RIGHT:
					camera.setTranslateX(CAMERA_INITIAL_DISTANCE2 += 5);
					break;
				}
			});
		scene.setOnMouseClicked(me ->
			{
				if (me.getButton() == MouseButton.SECONDARY)
				{
					CAMERA_INITIAL_DISTANCE = -1200;
					camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
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
