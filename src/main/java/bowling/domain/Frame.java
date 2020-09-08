package bowling.domain;

public class Frame {

    public static final int BEGIN_STAGE = 1;
    public static final int END_STAGE = 10;
    public static final int BONUS_STAGE = END_STAGE;

    private static final int FINAL_STEP = 2;
    private static final int BONUS_STEP = FINAL_STEP + 1;

    private int stage;
    private int step;
    private Pins pins;
    private Pins bonusPins;
    private GameResult result;

    private Frame(int stage) {
        if (stage < BEGIN_STAGE) {
            throw new IllegalArgumentException(String.format("%s 보다 작은 값은 설정될수 없습니다. [%s]", BEGIN_STAGE, stage));
        }

        if (stage > END_STAGE) {
            throw new IllegalArgumentException(String.format("%s 보다 큰 값은 설정될수 없습니다. [%s]", END_STAGE, stage));
        }

        this.stage = stage;
        this.step = 1;
        this.pins = Pins.newPins();
        this.bonusPins = Pins.newPins();
        this.result = GameResult.ofGutter();
    }

    public static Frame from(int stage) {
        return new Frame(stage);
    }

    public boolean hasNextTurn() {
        if (hasBonusStep()) {
            return true;
        }

        if (step > FINAL_STEP) {
            return false;
        }

        return !pins.isClear();
    }

    private boolean hasBonusStep() {
        return stage == BONUS_STAGE && pins.isClear() && step <= BONUS_STEP;
    }

    public Frame record(int hitCount) {
        if (hasBonusStep()) {
            return bonusRecord(hitCount);
        }

        return hitting(pins, hitCount);
    }

    private Frame bonusRecord(int hitCount) {
        step = BONUS_STEP;
        return hitting(bonusPins, hitCount);
    }

    private Frame hitting(Pins pins, int hitCount) {
        pins.hitting(hitCount);
        result = toResult(step, pins);
        step++;

        return this;
    }

    private GameResult toResult(int step, Pins pins) {
        if (pins.checkCount(Pins.DEFAULT_PIN_COUNT)) {
            return GameResult.ofGutter();
        }

        if (step == 1 && pins.isClear()) {
            return GameResult.ofStrike();
        }

        if (pins.isClear()) {
            return GameResult.ofSpare();
        }

        return GameResult.ofMiss(pins.getDownPins());
    }

    public boolean match(GameResult result) {
        return this.result.equals(result);
    }

    public int getStage(){
        return stage;
    }
}
