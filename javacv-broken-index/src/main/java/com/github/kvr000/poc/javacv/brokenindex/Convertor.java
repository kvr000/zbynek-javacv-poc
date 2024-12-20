package com.github.kvr000.poc.javacv.brokenindex;

import lombok.extern.log4j.Log4j2;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


@Log4j2
public class Convertor
{
	public static void main(String[] args) throws Exception
	{
		if (args.length != 2 && args.length != 3) {
			log.warn("Hello");
			throw new IllegalArgumentException("Usage: javacv-broken-index source-video destination-video [time-cut-sec]");
		}

		String sourceName = args[0];
		String destinationName = args[1];
		long timeCutSec = args.length > 2 ? Long.parseLong(args[2]) : Long.MAX_VALUE;

		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourceName)) {
			grabber.start();
			try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
				destinationName,
				grabber.getImageWidth(),
				grabber.getImageHeight(),
				grabber.getAudioChannels()
			)) {
				recorder.setFormat("mp4");
				recorder.setFrameRate(grabber.getFrameRate());

				if (grabber.hasVideo()) {
					// Keep the rotation from original video, so all the other data including audio match the orientation
					recorder.setDisplayRotation(grabber.getDisplayRotation());
					recorder.setVideoMetadata(grabber.getVideoMetadata());
					recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
					recorder.setVideoBitrate(grabber.getVideoBitrate());
					recorder.setVideoOption("tune", "film");
				}
				if (grabber.hasAudio()) {
					recorder.setAudioChannels(grabber.getAudioChannels());
					recorder.setAudioMetadata(grabber.getAudioMetadata());
					recorder.setAudioCodec(grabber.getAudioCodec());
					recorder.setAudioBitrate(grabber.getAudioBitrate());
					recorder.setAudioOption("c", "copy");
				}
				recorder.start();

				long nextLog = 0;

				Frame frame;
				BufferedImage bufferedImage = null;
				var imageType = -1;

				while ((frame = grabber.grab()) != null) {
					if (frame.timestamp/1_000_000 >= timeCutSec) {
						break;
					}
					recorder.setTimestamp(frame.timestamp);
					if (frame.type == Frame.Type.VIDEO) {
						if (imageType != Java2DFrameConverter.getBufferedImageType(frame)) {
							imageType = Java2DFrameConverter.getBufferedImageType(frame);
							bufferedImage = new BufferedImage(grabber.getImageWidth(), grabber.getImageHeight(), imageType);
						}
						// Convert OpenCV frame to BufferedImage
						Java2DFrameConverter.copy(frame, bufferedImage);

						// Paint
						Graphics2D graphics = bufferedImage.createGraphics();
						graphics.setColor(Color.green);
						graphics.setStroke(new BasicStroke(15));
						graphics.drawRect(10, 10, 100, 100);

						// Convert BufferedImage back to OpenCV frame
						Frame modifiedFrame = frame.clone();
						Java2DFrameConverter.copy(bufferedImage, modifiedFrame);
						// Record the modified frame
						recorder.record(modifiedFrame);
					}
					else {
						recorder.record(frame);
					}
					if (frame.timestamp/1_000 >= nextLog) {
						log.error("Progress: time={} ms", frame.timestamp / 1_000);
						nextLog += 10_000;
					}
				}
				log.error("Done, closing recorder");
			}
		}
		log.error("Done, exiting");
	}
}
