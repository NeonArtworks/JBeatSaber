package at.neonartworks.jbeatsaber.editor;

import at.neonartworks.jbeatsaber.json.note.CutDirection;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

public class Arrow extends MeshView
{
	public BeatBlock beatBlock;
	public CutDirection cutDirection;

	public CutDirection getCutDirection()
	{
		return cutDirection;
	}

	public void setCutDirection(CutDirection cutDirection)
	{
		this.cutDirection = cutDirection;
	}

	public Arrow(Mesh mesh)
	{
		setMesh(mesh);
	}

	public BeatBlock getBeatBlock()
	{
		return beatBlock;
	}

	public void setBeatBlock(BeatBlock beatBlock)
	{
		this.beatBlock = beatBlock;
	}

}
