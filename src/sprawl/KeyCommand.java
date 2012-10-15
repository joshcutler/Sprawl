package sprawl;

public enum KeyCommand {

    MOVE_LEFT(10), MOVE_RIGHT(10), JUMP(30), DRAW_PHYSICS(10);
    private final int repeat;
    private int countDown;
    private boolean armed;

    KeyCommand(int repeat) {
        this.repeat = repeat;
    }

    public void updatePressed(boolean pressed) {
        countDown--;
        if (pressed) {
            if (countDown < 0) {
                armed = true;
                countDown = repeat;
            }
        }
    }

    public boolean isArmed() {
        return armed;
    }

    public void resetArmed() {
        armed = false;
    }
}