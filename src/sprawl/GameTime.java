package sprawl;

public class GameTime {
	private static int days = 0;
	private static int hours = 0;
	private static float minutes = 0;
	
	public static void update(int delta) {
		GameTime.minutes += 1 / 1000f * (float) delta / Constants.GAME_SPEED;
		
		//Rollover things
		if (GameTime.minutes > 60) {
			GameTime.hours += Math.floor(GameTime.minutes / 60);
			GameTime.minutes = GameTime.minutes % 60;
		}
		if (GameTime.hours > 24) {
			GameTime.days += GameTime.hours / 24;
			GameTime.hours = GameTime.hours % 24;
		}
	}
	
	public static void reset() {
		GameTime.days = 0;
		GameTime.hours = 0;
		GameTime.minutes = 0;
	}

	public static int getDays() {
		return days;
	}

	public static int getHours() {
		return hours;
	}

	public static int getMinutes() {
		return (int) minutes;
	}
}
