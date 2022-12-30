package ao.vivalabs.iska_minhas_notas.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class MinMaxFilter implements InputFilter {

    private final double min;
    private final double max;

    public MinMaxFilter(Double minValue, Double maxValue) {
        this.min = minValue;
        this.max = maxValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            Double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(Double a, Double b, Double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
