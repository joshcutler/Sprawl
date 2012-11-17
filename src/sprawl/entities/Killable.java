package sprawl.entities;

public abstract class Killable extends Entity {
	int health = 0;
	int maxHealth = 0;
	
	public int getHealth() {
		return health;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int h) {
		this.maxHealth = h;
	}
}
