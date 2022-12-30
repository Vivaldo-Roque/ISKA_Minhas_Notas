package ao.vivalabs.iska_minhas_notas.utils;

/*
 * Created on 20/12/2022.
 * Written by Vivaldo Roque
 */

import android.content.Context;

/**
 * Converts Dp to Px.
 * <p>
 * Check if string is number
 */

public class Methods {
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round(dp * density);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}