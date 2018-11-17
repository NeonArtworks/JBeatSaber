package at.neonartworks.jbeatsaber.core.audio;

import java.io.File;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

public class AudioConverter
{
	private static final Integer bitrate = 256000;// Minimal bitrate only
	private static final Integer channels = 2; // 2 for stereo, 1 for mono
	private static final Integer samplingRate = 44100;// For good quality.
	private AudioAttributes audioAttr = new AudioAttributes();
	private EncodingAttributes encoAttrs = new EncodingAttributes();
	private Encoder encoder = new Encoder();
	private String oggFormat = "ogg";
	private String oggCodec = "libvorbis";

	public AudioConverter()
	{
		audioAttr.setBitRate(bitrate);
		audioAttr.setChannels(channels);
		audioAttr.setSamplingRate(samplingRate);
	}

	public void mp3ToOgg(File source, File target)
	{
		// ADD CODE FOR CHANGING THE EXTENSION OF THE FILE
		encoAttrs.setFormat(oggFormat);
		audioAttr.setCodec(oggCodec);
		encoAttrs.setAudioAttributes(audioAttr);
		MultimediaObject sourceObject = new MultimediaObject(source);
		try
		{
			encoder.encode(sourceObject, target, encoAttrs);
		} catch (Exception e)
		{
			System.out.println("Encoding Failed");
		}
	}

}
