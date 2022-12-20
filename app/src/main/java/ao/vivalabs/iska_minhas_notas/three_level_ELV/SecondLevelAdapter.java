package ao.vivalabs.iska_minhas_notas.three_level_ELV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentNotes;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.utils.Methods;


public class SecondLevelAdapter extends BaseExpandableListAdapter {
    List<String[]> data;
    String[] headers;
    private final Context context;


    public SecondLevelAdapter(Context context, String[] headers, List<String[]> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers[groupPosition];
    }

    @Override
    public int getGroupCount() {

        return headers.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.second_row, parent, false);
        TextView text = convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        String[] childData;

        childData = data.get(groupPosition);

        return childData[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.third_row, parent, false);

        LinearLayout linearLayout = convertView.findViewById(R.id.rowThirdTextList);

        String[] childArray = data.get(groupPosition);

        String text = childArray[childPosition];

        TextView newTextView = new TextView(context);
        newTextView.setText(text);
        newTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        newTextView.setPadding(Methods.dpToPx(context, 20f), 0, 0, 0);
        newTextView.setTextSize(20);
        newTextView.setOnClickListener(view -> {
            IskaWebScraping iska = IskaWebScraping.getInstance();
            IskaWebScraping.getInstance().setDisciplina(iska.findByDiscipline(((TextView) view).getText().toString(), iska.getTablesMapList()));
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentNotes(), "FRAG_NOTES").addToBackStack(null).commit();
        });
        linearLayout.addView(newTextView);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition > data.size() - 1){
            return  0;
        }

        String[] children = data.get(groupPosition);
        return children.length;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}