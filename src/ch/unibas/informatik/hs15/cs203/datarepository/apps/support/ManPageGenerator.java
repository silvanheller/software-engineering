package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;
import util.logging.Logger;

/**
 * Generates ManPages. <br />
 * A ManPage is in particular a formatted string containing information about a
 * certain command. It contains a paragraph with the synopsis, explains the
 * options and parameters as well as a brief description upon the behavior based
 * on the parameters and its output.<br />
 * This class formats information gathered with the two parser classes {@link HelpParser} and {@link DescriptionParser}.
 * They do read and parse the appropriate help files. 
 * @author Loris
 *
 */
public class ManPageGenerator {
	private final static Logger LOG = Logger.getLogger(ManPageGenerator.class);
	private static final String TITLE_FORMAT = "Help to command %s";
	private static final String SYNOPSIS_TITLE = "SYNOPSIS";
	private static final String PARAMS_TITLE = "PARAMETERS and OPTIONS";
	private static final String DESC_TITLE = "DESCRIPTION";

	private final String command;

	private HelpParser helpParser = null;

	private DescriptionParser descParser = null;
	
	/**
	 * Creates a new ManPageGenerator for the given command.
	 * If <tt>null</tt> is passed to this method, it generates a general help message
	 * about the application.
	 * @param command
	 */
	public ManPageGenerator(final String command) {
		this.command = command;
		LOG.config("Set up for command: " + command);
		if (command != null) {
			if(!command.equalsIgnoreCase("help")){
				init();
			}
		}
	}

	public String getManPage() throws IOException {
		return getManPage(false);
	}

	/**
	 * currently no experimental code!
	 */
	public String getManPage(boolean experimental) throws IOException {
		LOG.debug(
				"Request manpage for: " + (command == null ? "null" : command));
		String out;
		if (command == null) {
			out = createDefaultHelpPage();
		} else if (command.equalsIgnoreCase("help")) {
			out = createHelpPage();
		} else {
			out = buildManPage();
		}
		experimental = true;
		if (experimental) {
			return Utilities.wrapLinesSensitive(out, 80, null);
		} else {
			return Utilities.wrapLine(out, 80);
		}
	}

	protected void init() {
		LOG.debug("Initialization");
		readHelpFile();
		readDescFile();
	}

	private String buildManPage() throws IOException {
		LOG.debug("Building man page");
		final boolean descReady = checkDescParserReady();
		final boolean helpReady = checkHelpParserReady();
		if (!(descReady && helpReady)) {
			throw new IllegalStateException(String.format(
					"Not ready. DescriptionParser ready: %b, HelpParser ready: %b",
					descReady, helpReady));
		}
		final StringBuilder sb = new StringBuilder();
		// sb.append(String.format(TITLE_FORMAT, helpParser.getName() ));
		// newParagraph(sb);
		final String cap = helpParser.getShort();
		if (cap != null) {
			sb.append(cap);
			newParagraph(sb);
		} else {
			sb.append(String.format(TITLE_FORMAT, command));
			newParagraph(sb);
		}
		createSynopsisParagrpah(sb);
		createParamsParagraph(sb);
		createDescParagraph(sb);
		return sb.toString();
	}

	private String buildParams() {
		LOG.debug("Building params");
		final StringBuilder sb = new StringBuilder();
		final String[] lines = helpParser.getParamsLines();
		for (final String l : lines) {
			sb.append(l);
			newLine(sb);
		}
		return sb.toString();
	}

	private boolean checkDescParserReady() {
		return descParser != null && descParser.isReady();
	}

	private boolean checkHelpParserReady() {
		return helpParser != null && helpParser.hasContents();
	}

	private String createDefaultHelpPage() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Version: " + Version.VERSION);
		newParagraph(sb);
		sb.append("\t");
		sb.append("add");
		sb.append(": ");
		sb.append("Adds files to the repository.");
		newLine(sb);
		sb.append("\t");
		sb.append("delete");
		sb.append(": ");
		sb.append("Deletes data set(s) in the repository.");
		newLine(sb);
		sb.append("\t");
		sb.append("export");
		sb.append(": ");
		sb.append("Exports data set(s) to the file system.");
		newLine(sb);
		sb.append("\t");
		sb.append("list");
		sb.append(": ");
		sb.append("Lists data sets in the repository.");
		newLine(sb);
		sb.append("\t");
		sb.append("replace");
		sb.append(": ");
		sb.append("Replaces data set(s) by new one(s).");
		newLine(sb);
		sb.append("\t");
		sb.append("server");
		sb.append(": ");
		sb.append("Runs in server mode: Moving data sets from an incoming directory into the repository.");
		newLine(sb);
		sb.append("\t");
		sb.append("help");
		sb.append(": ");
		sb.append(
				"Gives information about the application or specified command.");
		newLine(sb);
		newLine(sb);
		sb.append(
				"Use data-repository help [command name] to get more information about a command");
		return sb.toString();
	}

	private void createDescParagraph(final StringBuilder sb)
			throws IOException {
		sb.append(DESC_TITLE);
		newLine(sb);
		sb.append(descParser.parse());
		newLine(sb);
	}

	private String createHelpPage() {
		final StringBuilder sb = new StringBuilder();
		sb.append(
				"Provides information about data repository or the given command.");
		newParagraph(sb);
		// SYNOPSIS
		sb.append(SYNOPSIS_TITLE);
		newLine(sb);
		sb.append("data-repository [help] [command name]");
		newParagraph(sb);
		// DESC
		sb.append(DESC_TITLE);
		newLine(sb);
		sb.append(
				"Prints onto standard output the version of the software in the first line followed by a list of all"
						+ "commands if the parameter [command name] is missing. For each command the name and a short"
						+ "description (should fit in one line) is printed. The user is also informed how to get more information"
						+ "for a command.\n\n"
						+ "If the command name is specified a complete synopsis and brief description of the command is"
						+ "printed onto standard output.\n\n"
						+ "In case of no command, options, or parameters the short help information will also be printed.");
		newLine(sb);
		return sb.toString();
	}

	private void createParamsParagraph(final StringBuilder sb) {
		// PARAMS
		sb.append(PARAMS_TITLE);
		newLine(sb);
		sb.append(buildParams());
		newParagraph(sb);
	}

	private void createSynopsisParagrpah(final StringBuilder sb) {
		// SYNOPSIS
		sb.append(SYNOPSIS_TITLE);
		newLine(sb);
		sb.append(helpParser.getSynopsis());
		newParagraph(sb);
	}

	private void newLine(final StringBuilder sb) {
		sb.append("\n");
	}

	private void newParagraph(final StringBuilder sb) {
		sb.append("\n\n");
	}

	private void readDescFile() {
		if (!checkHelpParserReady()) {
			throw new IllegalStateException("HelpParser not ready");
		}
		descParser = new DescriptionParser(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(helpParser.getDescriptionFilePath())));
		LOG.info("Read desc file");
	}

	private void readHelpFile() {
		helpParser = new HelpParser(command);
		try {
			helpParser.readFile();
			LOG.info("Read help file");
		} catch (final IOException e) {
			LOG.error("Error while reading helpfile", e);
		}
	}
}
