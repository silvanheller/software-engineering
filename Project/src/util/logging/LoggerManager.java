package util.logging;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class LoggerManager {

	private static LoggerManager instance = null;
	private static final Logger LOGGER;

	private static Level internalLevel;
	/**
	 * Contains the system property key to specify the log level of the logging
	 * api itself.
	 */
	public final static String VERBOSE_LEVEL_KEY = "lsjl.verbose.level";
	/**
	 * Contains the system property key to specify if the logging api logs its
	 * actions.
	 */
	public final static String VERBOSE_ENABLED_KEY = "lsjl.verbose.enabled";
	/**
	 * Contains the system property key to specify the path of the config file.
	 */
	public final static String CONFIG_PATH_KEY = "lsjl.logging.config.path";
	/**
	 * Contains the system property key to specify if the logging api is
	 * completely disabled.
	 */
	public final static String LOGGING_DISABLED_KEY = "lsjl.logging.disabled";

	private final static String[] configFileNames = { "lsjl", "logging",
			"logging-config" };
	private final static String[] configFileSuffixes = { ".json", ".cfg" };

	public static LoggerManager getManager() {
		if (instance == null) {
			instance = new LoggerManager();
		}
		return instance;
	}

	static Logger getLoggingLogger(final Class<?> clazz) {
		final Logger l = new Logger(java.util.logging.Logger.getLogger(clazz
				.getSimpleName()));
		l.resetHandlers();
		l.addHandler(new VerboseHandler(internalLevel));
		l.setLevel(LevelX.DEBUG);
		return l;
	}

	private final HashMap<String, Logger> nameLoggerMap;

	private final ConfigurationManager configManager = ConfigurationManager
			.getConfigManager();

	static {
		// verbose preparation
		if (Boolean.parseBoolean(System.getProperty(VERBOSE_ENABLED_KEY))) {
			try {
				internalLevel = LevelX.parse(System
						.getProperty(VERBOSE_LEVEL_KEY));
			} catch (final IllegalArgumentException ex) {
				internalLevel = Level.INFO;
			}
		} else {
			internalLevel = Level.OFF;
		}
		LOGGER = getLoggingLogger(LoggerManager.class);
	}

	private LoggerManager() {
		// TODO fix capacity
		nameLoggerMap = new HashMap<>(10);
		try {
			// TODO change to pre-defined chain of places to look for.
			final URL uri = findConfigFile();
			if (uri != null) {
				configManager.loadConfigFile(uri.getPath());
			} else {
				LOGGER.warn("Did not find a configuration file.");
			}
		} catch (final IOException e) {
			LOGGER.warn("Reading config failed: ", e);
			LOGGER.error("Could not read config file");
		}
	}

	/**
	 * Returns the names of all registered Loggers.
	 * 
	 * @return Returns the names of all registered and Loggers. If this method
	 *         gets invoked before a single logger has been registered, it
	 *         returns a zero-lengthed string array.
	 */
	public String[] getLoggerNames() {
		if (nameLoggerMap.size() == 0) {
			return new String[0];
		}
		final String[] names = new String[nameLoggerMap.size()];
		final Iterator<String> keysIt = nameLoggerMap.keySet().iterator();
		int i = 0;
		while (keysIt.hasNext()) {
			names[i++] = keysIt.next();
		}
		return names;
	}

	public void resetHandlers(final String loggerName) {
		final Logger log = nameLoggerMap.get(loggerName);
		if (log != null) {
			log.resetHandlers();
		}
	}

	Logger registerLogger(final Logger logger) {
		if (logger == null) {
			throw new NullPointerException(
					"Registering null loggers not allowed");
		}
		if (logger.getName() == null) {
			throw new IllegalArgumentException(
					"Registering anonymous loggers not allowed");
		}
		// logger is valid
		if (nameLoggerMap.containsKey(logger.getName())) {
			// logger is already registered
			return nameLoggerMap.get(logger.getName());
		} else {
			// register the new Logger
			// look if there is a config for the logger
			// if none, go with default
			// else go with created
			configManager.applyConfig(logger);
			nameLoggerMap.put(logger.getName(), logger);
			return logger;
		}
	}

	private URL findConfigFile() {
		final String pathPerProperty = System.getProperty(CONFIG_PATH_KEY);
		URL out = null;
		if (pathPerProperty == null) {
			for (final String suffix : configFileSuffixes) {
				for (final String name : configFileNames) {
					LOGGER.debug("Currently trying to load config file: "
							+ name + suffix);
					out = getClass().getClassLoader()
							.getResource(name + suffix);
					if (out == null) {
						continue;
					} else {
						break;
					}
				}
				if (out == null) {
					continue;
				} else {
					break;
				}
			}
		} else {
			LOGGER.debug("Config file path read from system property is: "
					+ pathPerProperty);
			try {
				out = getClass().getClassLoader().getResource(pathPerProperty);
			} catch (final SecurityException ex) {
				LOGGER.warn("SecurityManager denied access to classloader.");
			}
		}
		LOGGER.debug("Config file path: " + out);
		return out;
	}
}
