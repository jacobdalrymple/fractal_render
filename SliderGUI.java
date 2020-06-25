import javax.swing.*;
import javax.swing.event.*;

interface SliderToTrueValFunc {
    float operation(int sliderValue);
}

interface TrueToSliderValFunc {
    int operation(float trueValue);
}

public class SliderGUI extends JSlider{

    SliderToTrueValFunc sliderToTrueValFunc;
    TrueToSliderValFunc trueToSliderValFunc;

    public SliderGUI(int orentation, int minSliderRange, int maxSliderRange) {
        super(orentation, minSliderRange, maxSliderRange);
        setConvertOperationsToDefault();
    }

    public SliderGUI(int orentation, int minSliderRange, int maxSliderRange, int initPosition) {
        super(orentation, minSliderRange, maxSliderRange, initPosition);
        setConvertOperationsToDefault();
    }

    private void setConvertOperationsToDefault() {
        sliderToTrueValFunc = (int sliderValue) -> (float) sliderValue;
        trueToSliderValFunc = (float trueValue) -> (int) trueValue;
    }

    public void setSliderToTrueValFunc(SliderToTrueValFunc sliderToTrueValFunc) {
        this.sliderToTrueValFunc = sliderToTrueValFunc;
    }

    public void setTrueToSliderValFunc(TrueToSliderValFunc trueToSliderValFunc) {
        this.trueToSliderValFunc = trueToSliderValFunc;
    }

    public void setSliderPosition(float trueValue) {
        int sliderPos = trueToSliderValFunc.operation(trueValue);
        this.setValue(sliderPos);
    }

    public float getTrueValue() {
        return sliderToTrueValFunc.operation(getValue());
    }

    public boolean inSliderRange(int sliderPos) {
        return sliderPos >= getMinimum() && sliderPos <= getMaximum();
    }
}