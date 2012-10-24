package sprawl;

public class GameTime {
	private static int days = 0;
	private static int hours = 0;
	private static float minutes = 0;
	private static float dawn = 5*60;
	private static float dawn_length = 2*60;
	private static float dusk = 19*60;
	private static float dusk_length = 2*60;
	private static float nighttime_brightness = 0.10f;
	
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
	
	public static void reset(int days, int hours, int minutes) {
		GameTime.days = days;
		GameTime.hours = hours;
		GameTime.minutes = minutes;
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
	
	public static float daylight() {
		float current_minutes = GameTime.hours * 60 + GameTime.minutes;
		if (current_minutes < dawn || current_minutes > (dusk + dusk_length)) {
			//NightTime
			return nighttime_brightness;
		} else if (current_minutes > dawn && current_minutes < (dawn + dawn_length)) {
			//Dawn
			return 1 - (1 - nighttime_brightness) * (dawn + dawn_length - current_minutes) / dawn_length;
		} else if (current_minutes > (dawn + dawn_length) && current_minutes < dusk) {
			//Daytime
			return 1;
		}  else if (current_minutes > dusk && current_minutes < (dusk + dusk_length)) {
			//Dusk
			return nighttime_brightness + (1 - nighttime_brightness) * (dusk + dusk_length - current_minutes) / dusk_length;
		}

		return 1f;
	}
}
