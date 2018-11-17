package at.neonartworks.jbeatsaber.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import at.neonartworks.jbeatsaber.core.audio.AudioConverter;
import at.neonartworks.jbeatsaber.json.difficulty.Difficulty;
import at.neonartworks.jbeatsaber.json.difficulty.DifficultyLevel;
import at.neonartworks.jbeatsaber.json.environment.Environment;
import at.neonartworks.jbeatsaber.json.info.Info;
import at.neonartworks.jbeatsaber.json.level.Level;
import at.neonartworks.jbeatsaber.json.level.Track;
import at.neonartworks.jbeatsaber.json.note.CutDirection;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.note.NoteType;
import at.neonartworks.jbeatsaber.position.HorizontalPosition;
import at.neonartworks.jbeatsaber.position.VerticalPosition;
import at.neonartworks.jbeatsaber.util.ContentWriter;
import v4lk.lwbd.BeatDetector;
import v4lk.lwbd.BeatDetector.AudioType;
import v4lk.lwbd.BeatDetector.DetectorSensitivity;
import v4lk.lwbd.util.Beat;

public class JBeatSaber
{
	private double bpm;
	private String songName;
	private String mapAuthor;
	private String songSubName;
	private String coverArt;
	private Environment environment;
	private Map<Difficulty, Level> levelMap = new HashMap<Difficulty, Level>();
	private Info info;
	private String songPath;
	private File songFile;
	private File coverArtFile;
	private GenerationOptions options = GenerationOptions.getInstance();
	private int previewStart;
	private int previewDuration;
	private int beatsPerBar;
	private int noteJumpSpeed;
	private int shuffle;
	private int shufflePeriod;
	private String outputPath;
	private ContentWriter writer;

	public JBeatSaber(double bpm, String songName, String sonSubName, String mapAuthor, String coverArt,
			Environment environment, String songPath, int previewStart, int previewDuration, int beatsPerBar,
			int noteJumpSpeed, int shuffle, int shufflePeriod, String outputPath)
	{
		super();
		this.bpm = bpm;
		this.songName = songName;
		this.mapAuthor = mapAuthor;
		this.songSubName = sonSubName;
		this.coverArt = coverArt;
		this.coverArtFile = new File(coverArt);
		this.environment = environment;
		this.songPath = songPath;
		this.songFile = new File(songPath);
		this.previewStart = previewStart;
		this.previewDuration = previewDuration;
		this.beatsPerBar = beatsPerBar;
		this.noteJumpSpeed = noteJumpSpeed;
		this.shuffle = shuffle;
		this.shufflePeriod = shufflePeriod;
		this.outputPath = outputPath;
		writer = new ContentWriter();
		info = new Info(songName, songSubName, mapAuthor, bpm, previewStart, previewDuration,
				coverArtFile.getName() + ".jpg", environment);

	}

	public GenerationOptions getOptions()
	{
		return options;
	}

	public void setOptions(GenerationOptions options)
	{
		this.options = options;
	}

	private void saveAudioFile()
	{
		AudioConverter converter = new AudioConverter();
		if (this.songFile != null)
			if (this.songFile.exists())
				converter.mp3ToOgg(songFile,
						new File(outputPath + "/" + songFile.getName().replaceAll(".mp3", "") + ".ogg"));
	}

	private void saveLevelData()
	{
		for (Level l : levelMap.values())
			writer.writeTrack(l, outputPath);
	}

	private void saveInfoData()
	{
		writer.writeInfo(info, outputPath);
	}

	private void saveCoverArtData()
	{
		if (this.coverArtFile != null)
		{
			if (this.coverArtFile.exists())
			{
				try
				{
					BufferedImage image = ImageIO.read(this.coverArtFile);
					ImageIO.write(image, "JPG", new File(outputPath + "/" + this.coverArtFile.getName() + ".jpg"));
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
			{
				info.setCoverImagePath("coverArt.jpg");
				BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
				try
				{
					ImageIO.write(image, "jpg", new File(outputPath + "/" + "coverArt.jpg"));
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void saveMaps()
	{
		saveAudioFile();
		saveCoverArtData();
		saveLevelData();
		saveInfoData();

	}

	public void populateLevel(Level level)
	{
		Track track = level.getTrack();
		Beat[] beats = getBeatArray();
		if (beats == null)
			return;

		long oldTime = 0;

		for (Beat beat : beats)
		{
			if (beat.timeMs - oldTime > options.getTimeBetweenNotesInMs())
				if (beat.energy > 0.05d)
				{
					Note note = new Note(Note.ms2beats(beat.timeMs, getBpm()), HorizontalPosition.getRandom(),
							VerticalPosition.getRandom(), NoteType.getRandom(), CutDirection.getRandom());
					track.addNote(note);
				}
			oldTime = beat.timeMs;
		}
	}

	private Beat[] getBeatArray()
	{
		try
		{
			
			return BeatDetector.detectBeats(this.songFile, AudioType.MP3, DetectorSensitivity.MIDDLING);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Level createLevel(Difficulty difficulty)
	{
		Track track = new Track(bpm, beatsPerBar, noteJumpSpeed, shuffle, shufflePeriod);
		Level level = new Level(track, difficulty);
		info.addDifficulty(new DifficultyLevel(difficulty, songFile.getName().replaceAll(".mp3", "") + ".ogg"));
		levelMap.put(difficulty, level);
		return level;
	}

	public double getBpm()
	{

		return bpm;
	}

	public void setBpm(double bpm)
	{
		this.bpm = bpm;
	}

	public String getSongName()
	{
		return songName;
	}

	public void setSongName(String songName)
	{
		this.songName = songName;
	}

	public String getMapAuthor()
	{
		return mapAuthor;
	}

	public void setMapAuthor(String mapAuthor)
	{
		this.mapAuthor = mapAuthor;
	}

	public String getSongSubName()
	{
		return songSubName;
	}

	public void setSongSubName(String songSubName)
	{
		this.songSubName = songSubName;
	}

	public String getCoverArt()
	{
		return coverArt;
	}

	public void setCoverArt(String coverArt)
	{
		this.coverArt = coverArt;
	}

	public Environment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}

	public String getSongPath()
	{
		return songPath;
	}

	public void setSongPath(String songPath)
	{
		this.songPath = songPath;
	}

	public int getPreviewStart()
	{
		return previewStart;
	}

	public void setPreviewStart(int previewStart)
	{
		this.previewStart = previewStart;
	}

	public int getPreviewDuration()
	{
		return previewDuration;
	}

	public void setPreviewDuration(int previewDuration)
	{
		this.previewDuration = previewDuration;
	}

	public ContentWriter getWriter()
	{
		return writer;
	}

	public void setWriter(ContentWriter writer)
	{
		this.writer = writer;
	}

}
