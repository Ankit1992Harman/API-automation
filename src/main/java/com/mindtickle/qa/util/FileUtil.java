package com.mindtickle.qa.util;

import com.mindtickle.qa.util.LoggerUtil.LogLevel;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {

	static File file = null;

	public static boolean getFileSizeandExtension(String urlString, String extension) {
		boolean flag = false;
		try {
			URL webImage = new URL(urlString);
			if (urlString.contains(extension)) {

				String fileName = webImage.getFile();
				String destName = fileName.substring(fileName.lastIndexOf("/"));
				file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Pixie"
						+ System.getProperty("file.separator") + destName);
				String ext = FilenameUtils.getExtension(file.getAbsolutePath());
				// System.out.println("ext " + ext);
				ReadableByteChannel rbc;
				rbc = Channels.newChannel(webImage.openStream());
				FileOutputStream fos = new FileOutputStream(file);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();
				double kilobytes = (file.length() / 1024);
				// System.out.println(kilobytes + " KBs");
				if (kilobytes > 0) {
					flag = true;
					LoggerUtil.setlog(LogLevel.INFO,
							"<pre>" + "File Extension matches. File Size is " + kilobytes + " KBs" + "</pre>");
				} else {
					LoggerUtil.setlog(LogLevel.INFO, "<pre>" + "File Extension matches. File Size is 0 KBs" + "</pre>");
				}
				file.delete();
			} else {
				LoggerUtil.setlog(LogLevel.INFO, "<pre>" + "File Extension doesnot match");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
