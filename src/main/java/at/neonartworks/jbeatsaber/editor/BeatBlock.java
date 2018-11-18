package at.neonartworks.jbeatsaber.editor;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

public class BeatBlock extends MeshView
{
	private Arrow arrowMesh;

	public BeatBlock()
	{

	}

	public BeatBlock(Mesh mesh)
	{
		setMesh(mesh);
	}

	public Arrow getArrowMesh()
	{
		return arrowMesh;
	}

	public void setArrowMesh(Arrow arrowMesh)
	{
		this.arrowMesh = arrowMesh;
	}

}
