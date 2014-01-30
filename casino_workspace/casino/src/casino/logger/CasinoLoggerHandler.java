package casino.logger;

import casino.logger.CasinoLogger.CasinoLoggerLevel;

public interface CasinoLoggerHandler {

	void log(CasinoLoggerLevel level, String message);
	
}
