package com.mytrainstation;

import android.app.Application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Custom application that will configure log4j to enable logging for whole application.
 *
 * @author JSCHENK
 */
public class TrainStationApplication extends Application {

	private static final Logger LOG = Logger.getLogger(TrainStationApplication.class);

	private GlobalUncaughtExceptionHandler _globalExceptionHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		configureLog4j();
	}

	private String getInternalLogFilePath() {
		return getFilesDir() + "com.mytrainstation.log4j.txt";
	}

	private String getFilePatternForLog4j() {
		return "%p  %d  %t  %c  %m%n";
	}

	/**
	 * Configures log4j to enable logging in application.
	 */
	private void configureLog4j() {
		int maxBackupSize = 0;
		long maxFileSize = 10 * 1024 * 1024; // 10MB
		Log4jHelper.configure(getInternalLogFilePath(), getFilePatternForLog4j(), maxBackupSize,
				maxFileSize, Level.DEBUG);
		registerGlobalUncaughtExceptionHandler();
	}

	private void registerGlobalUncaughtExceptionHandler() {
		if (_globalExceptionHandler == null) {
			_globalExceptionHandler = new GlobalUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(_globalExceptionHandler);
		}
	}

	private class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

		Thread.UncaughtExceptionHandler _defaultExceptionHandler;

		GlobalUncaughtExceptionHandler() {
			_defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			int level = Log4jHelper.getRootLogLevel();
			if (level == Level.OFF_INT) {
				Log4jHelper.setRootLevel(Level.ERROR_INT);
			}
			LOG.error("UncaughtException!", ex);
			_defaultExceptionHandler.uncaughtException(thread, ex);
		}

	}
}
