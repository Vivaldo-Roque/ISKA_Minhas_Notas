package ao.vivalabs.iska_minhas_notas.utils;

/*
 * Created on 20/12/2022.
 * Written by Vivaldo Roque
 */

import android.content.Context;

/**
 * Converts Dp to Px.
 * <p>
 * Converts Sp to Px
 */

public class Methods {
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round(dp * density);
    }

    public static int spToPx(Context context, float sp) {
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(sp * scaleDensity);
    }
}