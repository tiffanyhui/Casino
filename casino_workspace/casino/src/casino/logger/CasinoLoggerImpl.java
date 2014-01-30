package casino.logger;

import java.util.HashSet;
import java.util.Set;

public class CasinoLoggerImpl implements CasinoLogger {

	Set<CasinoLoggerHandler> handlers = new HashSet<CasinoLoggerHandler>();
	
	@Override
	public void logError(String message) {
		log(CasinoLoggerLevel.ERROR, message, null);
	}

	@Override
	public void logInfo(String message) {
		log(CasinoLoggerLevel.INFO, message, null);
	}

	@Override
	public void logWarning(String message) {
		log(CasinoLoggerLevel.WARNING, message, null);
	}

	@Override
	public void logThrowable(Exception ex, String message) {
		log(CasinoLoggerLevel.ERROR, message, ex);
	}

	@Override
	public void log(CasinoLoggerLevel logLevel, String message) {
		for (CasinoLoggerHandler handler : handlers){
			handler.log(logLevel, message);
		}
	}
	
	@Override
	public void log(CasinoLoggerLevel logLevel, String message, Throwable cause) {
		for (CasinoLoggerHandler handler : handlers){
			handler.log(logLevel, message);
		}
	}

	@Override
	public void addLogHandler(CasinoLoggerHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void removeLogHandler(CasinoLoggerHandler handler) {
		handlers.remove(handler);
	}

}
