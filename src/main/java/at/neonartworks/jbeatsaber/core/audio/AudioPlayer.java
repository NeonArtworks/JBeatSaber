package at.neonartworks.jbeatsaber.core.audio;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer
{

	public double calculateDuration(final File oggFile) throws IOException
	{
		int rate = -1;
		int length = -1;

		int size = (int) oggFile.length();
		byte[] t = new byte[size];

		FileInputStream stream = new FileInputStream(oggFile);
		stream.read(t);

		for (int i = size - 1 - 8 - 2 - 4; i >= 0 && length < 0; i--)
		{ // 4 bytes for "OggS", 2 unused bytes, 8 bytes for length
			// Looking for length (value after last "OggS")
			if (t[i] == (byte) 'O' && t[i + 1] == (byte) 'g' && t[i + 2] == (byte) 'g' && t[i + 3] == (byte) 'S')
			{
				byte[] byteArray = new byte[] { t[i + 6], t[i + 7], t[i + 8], t[i + 9], t[i + 10], t[i + 11], t[i + 12],
						t[i + 13] };
				ByteBuffer bb = ByteBuffer.wrap(byteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				length = bb.getInt(0);
			}
		}
		for (int i = 0; i < size - 8 - 2 - 4 && rate < 0; i++)
		{
			// Looking for rate (first value after "vorbis")
			if (t[i] == (byte) 'v' && t[i + 1] == (byte) 'o' && t[i + 2] == (byte) 'r' && t[i + 3] == (byte) 'b'
					&& t[i + 4] == (byte) 'i' && t[i + 5] == (byte) 's')
			{
				byte[] byteArray = new byte[] { t[i + 11], t[i + 12], t[i + 13], t[i + 14] };
				ByteBuffer bb = ByteBuffer.wrap(byteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				rate = bb.getInt(0);
			}

		}
		stream.close();

		double duration = (double) (length * 1000) / (double) rate;
		return duration;
	}

	public void play(String filePath)
	{
		final File file = new File(filePath);

		try (final AudioInputStream in = getAudioInputStream(file))
		{

			final AudioFormat outFormat = getOutFormat(in.getFormat());
			final Info info = new Info(SourceDataLine.class, outFormat);

			try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info))
			{

				if (line != null)
				{
					line.open(outFormat);
					line.start();
					stream(getAudioInputStream(outFormat, in), line);
					line.drain();
					line.stop();
				}
			}

		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	private AudioFormat getOutFormat(AudioFormat inFormat)
	{
		final int ch = inFormat.getChannels();
		final float rate = inFormat.getSampleRate();
		return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

	private void stream(AudioInputStream in, SourceDataLine line) throws IOException
	{
		final byte[] buffer = new byte[65536];
		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length))
		{
			line.write(buffer, 0, n);
		}
	}
}
