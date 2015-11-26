package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.tools.StandardLocation;

class DatasetPortLogger {

	private Path path;

	public static final String TEMPLATE = "%1$s %2$s %3$s";

	/**
	 * The application's date-time-format. <br />
	 * It's format string is as followed: <tt>yyyy-MM-dd HH:mm:ss</tt>.
	 */
	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final String INFO_LVL = "[INFO]";
	public static final String ERROR_LVL = "[ERROR]";

	public static final String DEFAULT_FILE_NAME = "server.log";

	public DatasetPortLogger(Path repo, Path path) {
		path = confimPath(repo, path);
	}

	private Path confimPath(Path repo, Path path) {
		boolean exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
		boolean notExists = Files.notExists(path, LinkOption.NOFOLLOW_LINKS);
		if (notExists) {
			return createDefaultLogFile(repo);
		} else if (exists) {
			boolean isDir = Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
			boolean isFile = Files.isRegularFile(path,
					LinkOption.NOFOLLOW_LINKS);
			if (isFile) {
				return path;
			} else if (isDir) {
				return createDefaultLogFile(path);
			} else {
				throw new IllegalArgumentException(
						"Path is neither a file nor a directory: "
								+ path.toString());
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot acces given path: " + path.toString());
		}
	}

	private Path createDefaultLogFile(Path parent) {
		return parent.resolve(DEFAULT_FILE_NAME);
	}

	public void info(String msg) {
		log(INFO_LVL, msg);
	}

	public void error(String msg) {
		log(ERROR_LVL, msg);
	}
	
	public void error(String msg, Throwable t){
		StringBuilder sb = new StringBuilder(msg+"\n");
		if (t != null) {
			// record has throwable
			final StringWriter strWtr = new StringWriter();
			final PrintWriter pWtr = new PrintWriter(strWtr);
			pWtr.println();
			t.printStackTrace(pWtr);
			pWtr.flush();
			pWtr.close();
			sb.append(strWtr.toString());
		}
		error(sb.toString());
	}

	private void log(String lvl, String msg) {
		String log = createLog(lvl, msg);
		writeLog(log);
	}

	private void writeLog(String log) {
		BufferedWriter bw = null;
		try {
			bw = Files.newBufferedWriter(path, StandardOpenOption.WRITE,
					StandardOpenOption.APPEND);
			bw.append(log);
			bw.newLine();
			bw.flush();
		} catch (IOException ex) {
			throw new RuntimeException("Oh! Problem: ", ex);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException("No! Serious problem: ", e);
			}
		}

	}

	private String createLog(String lvl, String msg) {
		return String.format(TEMPLATE, DATE_TIME_FORMAT.format(new Date()), lvl,
				msg);
	}
}