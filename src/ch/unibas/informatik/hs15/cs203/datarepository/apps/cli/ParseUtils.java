package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.common.PrintUtils;

/**
 * Class which contains utility methods for parsing.
 * @author Loris
 *
 */
class ParseUtils {
	
	private ParseUtils(){/* no instances*/}
	
	/**
	 * Parses a given String to an appropriate date. <br />
	 * Based on the length of the string, either
	 * <code>yyyy-MM-dd HH:mm:ss</code> or <code>yyyy-MM-dd</code> is used as
	 * {@link DateFormat} to parse the string. Therefore <tt>null</tt> is
	 * returned, if the chosen {@link DateFormat} cannot parse the given string.
	 *
	 * @param str
	 *            The string to parse.
	 * @return The parsed Date OR <tt>null</tt> if the given string was not
	 *         parseable, or the given string was <tt>null</tt>
	 * @see DateFormat#parse(String)
	 */
	public static Date parseDate(final String str) {
		try {
			if (str.length() > 10) {
				return PrintUtils.DATE_TIME_FORMAT.parse(str);
			} else {
				return PrintUtils.DATE_FORMAT.parse(str);
			}
		} catch (final ParseException | NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Formats a given Date to the application's {@link PrintUtils#DATE_TIME_FORMAT}.
	 * @param date The date to parse.
	 * @return A string containing the parsed date.
	 * @see DateFormat#format(Date)
	 */
	public static String formatDate(final Date date) {
		final DateFormat precise = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return precise.format(date);
	}
}
