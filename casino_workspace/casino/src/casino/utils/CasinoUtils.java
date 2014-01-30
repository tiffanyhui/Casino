package casino.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import casino.logger.CasinoLogger.CasinoLoggerLevel;
import casino.player.ComputerPlayer;
import casino.player.Player;

public class CasinoUtils {

	/**
	 * Formats file name to be [gameName]_[Year][Month][Day]_[Hr][Min][Sec].txt
	 * @param gameName
	 * @return
	 */
	public static String getLoggerFileName(String gameName){
		StringBuffer buffer = new StringBuffer();
		buffer.append(gameName+"_");
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		buffer.append(timeStamp);
		return buffer.toString();
	}
	
	public static boolean isComputer(Player player){
		return player instanceof ComputerPlayer;
	}
	
	public static Level convertLogLevel(CasinoLoggerLevel casinoLogLevel){
		Level level = null;
		switch (casinoLogLevel) {
		case ERROR:
		case THROWABLE :
			level = Level.SEVERE;
			break;
		case WARNING :
			level = Level.WARNING;
			break;
		case INFO :
			level = Level.INFO;
			break;
		}
		return level;
	}
	
}
