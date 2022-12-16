package ao.vivalabs.iska_minhas_notas.utils;

import android.content.Context;
import android.util.TypedValue;

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
