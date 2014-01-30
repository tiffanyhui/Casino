package casino.logger;

public interface CasinoLogger {

	public CasinoLogger INSTANCE = new CasinoLoggerImpl();
	
	public static enum CasinoLoggerLevel {
		INFO, WARNING, ERROR, THROWABLE;
	}
	
	public void logError(String message);
	
	public void logInfo(String message);
	
	public void logWarning(String message);
	
	public void logThrowable(Exception ex, String message);

	public void log(CasinoLoggerLevel logLevel, String message);
	
	/**
	 * @param logLevel
	 * @param message
	 * @param cause optional 
	 */
	public void log(CasinoLoggerLevel logLevel, String message, Throwable cause);
	
	void addLogHandler(CasinoLoggerHandler handler);
	
	void removeLogHandler(CasinoLoggerHandler handler);
}
