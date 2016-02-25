package perfmon.agent.util;

import perfmon.agent.util.Config;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Log{
	public static final int DEBUG	= 0;
	public static final int INFO	= 1;
	public static final int WARN	= 2;
	public static final int ERROR	= 3;
	public static final int FATAL	= 4;

	public static void $(int severity, String message){
		if (severity == Log.DEBUG && !Config.AGENT_DEBUG){ return; }
		StringBuilder m = new StringBuilder(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date()));
		m.append(" | ");
		m.append(Log.parseSeverity(severity));
		m.append(": ");
		m.append(message);
		if (Config.AGENT_LOG_PRINT){ Log.putScreen(m.toString()); }
		if (Config.AGENT_LOG_FILE){ Log.putFile(m.toString()); }
		if (Config.AGENT_LOG_DB){ Log.putDatabase(m.toString()); }
	}

	private static String parseSeverity(int s){
		switch (s){
			case Log.DEBUG:
				return "[DBG]";
			case Log.INFO:
				return "[INF]";
			case Log.WARN:
				return "[WRN]";
			case Log.ERROR:
				return "[ERR]";
			case Log.FATAL:
				return "[FAT]";
		}
		return "[---]";
	}

	private static void putScreen(String m){
		System.out.println(m);
	}

	private static void putFile(String m){
		// TODO: Log to file
	}

	private static void putDatabase(String m){
		// TODO: Log to database
		// NOTE: Low priority function
	}
}
