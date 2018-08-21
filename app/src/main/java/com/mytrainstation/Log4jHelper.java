package com.mytrainstation;

import org.apache.log4j.Level;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Helper class to configure log4j for application.
 *
 * @author JSCHENK
 */
public class Log4jHelper {

	private final static LogConfigurator _logConfigurator = new LogConfigurator();

	/**
	 * @param fileName
	 * 		The name of the log file.
	 * @param filePattern
	 * 		The file pattern that will be used for each entry within log file.
	 * @param maxBackupSize
	 * 		The maximum size of backup.
	 * @param maxFileSize
	 * 		The maximum file size.
	 * @param level
	 * 		The log level that will be used to decide whether entry will be write within log file
	 * 		or not.
	 */
	public static void configure(String fileName, String filePattern, int maxBackupSize,
			long maxFileSize, Level level) {
		// set the name of the log file
		_logConfigurator.setFileName(fileName);
		// set output format of the log line
		_logConfigurator.setFilePattern(filePattern);
		// Maximum number of backed up log files
		_logConfigurator.setMaxBackupSize(maxBackupSize);
		// Maximum size of log file until rolling
		_logConfigurator.setMaxFileSize(maxFileSize);
		// Set the current level
		_logConfigurator.setRootLevel(level);
		// configure
		_logConfigurator.configure();
	}

	/**
	 * @return The current root log level.
	 */
	public static int getRootLogLevel() {
		return _logConfigurator.getRootLevel().toInt();
	}

	/**
	 * @param level
	 * 		The log level to be set as root level.
	 */
	public static void setRootLevel(int level) {
		try {
			_logConfigurator.setRootLevel(Level.toLevel(level));
			_logConfigurator.configure();
		} catch (Exception e) {
			// cannot determine if logging is possible.
			// _logConfigurator.configure() will throw exception if internal
			// storage is not mounted.
		}
	}
}
