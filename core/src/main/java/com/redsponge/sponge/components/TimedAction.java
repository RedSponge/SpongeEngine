package com.redsponge.sponge.components;

import com.redsponge.sponge.entity.Component;

public class TimedAction extends Component {

    private float value;
    private Runnable onComplete;
    private boolean removeOnComplete;

    public TimedAction() {
        super(false, false);
    }

    public TimedAction(Runnable onComplete, boolean removeOnComplete) {
        this();
        this.onComplete = onComplete;
        this.removeOnComplete = removeOnComplete;
    }

    public TimedAction(float value, Runnable onComplete, boolean removeOnComplete) {
        this(onComplete, removeOnComplete);
        setValue(value);
    }

    @Override
    public void begin() {}

    @Override
    public void update(float delta) {
        if(value > 0) {
            setValue(value - delta);
            if(value <= 0) {
                setValue(0);
                setActive(false);

                if(onComplete != null) onComplete.run();
                if(removeOnComplete) {
                    removeSelf();
                }
            }
        }
    }

    @Override
    public void render() {

    }

    public void clear() {
        value = 0;
        setActive(false);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.max(0, value);
        setActive(value > 0);
    }

    public boolean isRunning() {
        return value > 0;
    }
}
