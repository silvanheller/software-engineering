package util.logging;

import java.util.logging.Level;

/**
 * The {@link LoggerConfiguration} class represents the <tt>logger</tt> entry in
 * a <tt>LSJL</tt> configuration file.
 *
 * @author Loris
 *
 */
class LoggerConfiguration {

	private String name;
	private final String handlerRef;
	private final Level level;

	public LoggerConfiguration(final String handlerRef, final Level level) {
		this.handlerRef = handlerRef;
		this.level = level;
	}

	/**
	 * @return the handlerRef
	 */
	public String getHandlerRef() {
		return handlerRef;
	}

	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
