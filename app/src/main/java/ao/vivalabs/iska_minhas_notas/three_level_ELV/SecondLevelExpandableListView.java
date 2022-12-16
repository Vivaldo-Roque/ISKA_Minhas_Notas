package ao.vivalabs.iska_minhas_notas.three_level_ELV;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

public class SecondLevelExpandableListView extends ExpandableListView {

    public SecondLevelExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(999999, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
