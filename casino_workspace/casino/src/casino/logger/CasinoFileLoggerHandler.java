package casino.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import casino.logger.CasinoLogger.CasinoLoggerLevel;
import casino.utils.CasinoUtils;

public class CasinoFileLoggerHandler extends FileHandler implements CasinoLoggerHandler{

	protected Logger logger;
	
	public CasinoFileLoggerHandler(String gameName) throws IOException{
		super(getFileStorageLocation(gameName));
		logger = Logger.getLogger(getLoggerName());
		logger.addHandler(this);
		logger.setUseParentHandlers(false);
        SimpleFormatter formatter = new SimpleFormatter();  
        setFormatter(formatter);
	}
	
	private static String getFileStorageLocation(String gameName){
		String location = "C:/Games/";
		return location + CasinoUtils.getLoggerFileName(gameName) + ".log";
	}
	
	private static String getLoggerName(){
		return CasinoFileLoggerHandler.class.getName();
	}
	
	@Override
	public void log(CasinoLoggerLevel casinoLevel, String message) {
		Level level = CasinoUtils.convertLogLevel(casinoLevel);
		logger.log(level, message);
	}
}
