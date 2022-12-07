package fr.shykuzo.punishment.utilities.enumerations;

import java.util.HashMap;

import fr.shykuzo.punishment.Main;

public enum TimeUnit {

	SECOND(
			Main.getInstance().getConfigManager().getSecondLongName(),
			Main.getInstance().getConfigManager().getSecondShortName(),
			1L
	),
	
	MINUTE(
			Main.getInstance().getConfigManager().getMinuteLongName(),
			Main.getInstance().getConfigManager().getMinuteShortName(),
			60L
	),
	
	HOUR(
			Main.getInstance().getConfigManager().getHourLongName(),
			Main.getInstance().getConfigManager().getHourShortName(),
			3600L
	),
	
	DAY(
			Main.getInstance().getConfigManager().getDayLongName(),
			Main.getInstance().getConfigManager().getDayShortName(),
			86400L
	);
	
		// -------------------- \\
	
	private String longName;
	private String shortName;
	private long toSecond;
	
	private static HashMap<String, TimeUnit> shortNameID;
	
		// -------------------- \\
	
	static {
		shortNameID = new HashMap<String, TimeUnit>();
		for(TimeUnit unit : values()) {
			shortNameID.put(unit.shortName, unit);
		}
	}
	
	TimeUnit(String longName, String shortName, long toSecond) {
		this.longName = longName;
		this.shortName = shortName;
		this.toSecond = toSecond;
	}
	
		// -------------------- \\
	
	public String getLongName() {
		return longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static TimeUnit getFromShortName(String shortName) {
		return shortNameID.get(shortName);
	}
	
	public long getToSecond() {
		return toSecond;
	}
	
	public static boolean existFromShortName(String shortName) {
		return shortNameID.containsKey(shortName);
	}
	
}
