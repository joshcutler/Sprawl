package sprawl.entities;

public enum KeyCommand {

    MOVE_LEFT(10), MOVE_RIGHT(10), JUMP(30), DIG(100), CHOP(100);
    private final int repeat;
    private int countDown;
    private boolean armed;

    KeyCommand(int repeat) {
        this.repeat = repeat;
        this.countDown = repeat;
        this.armed = true;
    }

    public void updateTime(int delta) {
        countDown -= delta;
         
        if (countDown <= 0) {
            armed = true;
            countDown = repeat;
        }
    }

    public boolean isArmed() {
        return armed;
    }

    public void resetArmed() {
        armed = false;
    }
}