package bowling.domain;

import bowling.domain.state.Final;
import bowling.domain.state.Strike;

import java.util.List;

public interface State {
    static State last(State current, int count) {
        Pin pin = Pin.of(count);

        if (pin.isStrike()) {
            return new Strike(pin);
        }

        return Final.from(current, count);
    }

    State roll(int count);

    List<Pin> toPins();

    Score getScore();

    default boolean isFinish() {
        return false;
    }

    default Score sumScore(Score before) {
        if (before.canNextSum()) {
            getScore().sum(before);
        }
        return before;
    }
}