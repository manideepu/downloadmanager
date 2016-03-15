package com.deepu.downloadmanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Download and save the files based on the input file provided
 * 
 * @author deepu.mani
 *
 */
public class MultiThreadedApp {

	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final static String INPUT_FILE_PATH = "/src/main/resources/urls.txt";
	private final static String OUTPUT_FILE_PATH = "/output/";
	private final static int THREAD_COUNT = 6;
	private static final Log LOG = LogFactory.getLog(MultiThreadedApp.class);

	public static void main(String[] args) {

		MultiThreadedApp app = new MultiThreadedApp();
		try {
			List<String> inputList = app.readInputTextFile();
			app.fetchFiles(inputList);
		} catch (IOException e) {
			LOG.error("IOException occured ");
		} catch (Exception e) {
			LOG.error("Exception occured ");
		}

	}

	/**
	 * @param inputList
	 * @throws IOException
	 *             Fetch the files based on the inputList provided
	 */
	public void fetchFiles(List<String> inputList) throws IOException {
		StringBuilder outputDirectory = new StringBuilder(
				System.getProperty("user.dir")).append(OUTPUT_FILE_PATH);
		ExecutorService executerService = Executors
				.newFixedThreadPool(THREAD_COUNT);
		for (final String string : inputList) {
			if (string != null) {
				final StringBuilder fileName = new StringBuilder(
						outputDirectory).append(string.substring(
						string.lastIndexOf("/") + 1, string.length()));
				executerService.execute(new Runnable() {
					public void run() {
						try {
							saveUrl(string, fileName.toString());
						} catch (IOException e) {
							LOG.error("IOException occured while processing "
									+ string);
						}
					}
				});
			}
		}
		executerService.shutdown();
	}

	/**
	 * @return
	 * @throws IOException
	 *             Read the input text file from the file path and return the
	 *             list
	 */
	public List<String> readInputTextFile() throws IOException {
		List<String> inputList = new ArrayList<String>();
		Path path = Paths.get(System.getProperty("user.dir"), INPUT_FILE_PATH);
		Scanner scanner = null;
		try {
			scanner = new Scanner(path, ENCODING.name());
			while (scanner.hasNextLine()) {
				inputList.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
		return inputList;
	}

	/**
	 * @param fileUrl
	 * @param filePath
	 * @throws IOException
	 *             Download and save the fileUrl to the filePath provided
	 */
	public void saveUrl(String fileUrl, String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			URL url = new URL(fileUrl);
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
		}
	}
}