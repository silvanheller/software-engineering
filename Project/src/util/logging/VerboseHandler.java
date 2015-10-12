package util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class VerboseHandler extends ConsoleHandler {
    
    public VerboseHandler(){
	this(LevelX.ERROR);
    }

    public VerboseHandler(Level lvl) {
	setOutputStream(System.out);
	setFormatter(new VerboseFormatter() );
	setLevel(lvl);
    }
    
    private static class VerboseFormatter extends Formatter{
	private final DateFormat dateFormat = new SimpleDateFormat(
		    "dd-MM-yyyy HH:mm:ss");
	/**
	     * Formats the given {@link LogRecord}.
	     * 
	     * <p>
	     * The layout of a single formatted {@link LogRecord} is like so:<br>
	     * {@code dd-MM-yyyy HH:mm:ss [LEVEL] [name] msg NL}<br>
	     * The described time format is in {@link SimpleDateFormat}-notation.<br>
	     * The LEVEL stands for the record's level.<br>
	     * name stands for the name of the logger which created the record.<br>
	     * The placeholder msg will be replaced by the record's message and NL
	     * stands for a new line.
	     * </p>
	     * 
	     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	     */
	    @Override
	    public synchronized String format(LogRecord record) {
		String dateStr = dateFormat.format(new Date(record.getMillis() ));
		String nameStr = record.getLoggerName();
		String lvlStr = record.getLevel().getName();
		StringBuffer sb = new StringBuffer(256);
//		sb.append(String.format("[%s]", dateStr));
		sb.append(String.format("[%s]", nameStr));
		sb.append(String.format("[%s]", lvlStr));
		sb.append(" ");
		sb.append(record.getMessage());
		sb.append("\n");
		Throwable thrown = record.getThrown();
		if(thrown != null){
		    //record has throwable
		    StringWriter strWtr = new StringWriter();
		    PrintWriter pWtr = new PrintWriter(strWtr);
		    pWtr.println();
		    thrown.printStackTrace(pWtr);
		    pWtr.flush();
		    pWtr.close();
		    sb.append(strWtr.toString());
		}
		return sb.toString();
	    }
    }

}