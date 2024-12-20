package ao.vivalabs.iska_minhas_notas.three_level_ELV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.models.TableModel;
import ao.vivalabs.iska_minhas_notas.utils.Methods;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    final String[] parentHeaders;
    final List<String[]> secondLevel;
    final List<LinkedHashMap<String, TableModel[]>> data;
    private final Context context;

    public ThreeLevelListAdapter(Context context, String[] parentHeader, List<String[]> secondLevel, List<LinkedHashMap<String, TableModel[]>> data) {
        this.context = context;

        this.parentHeaders = parentHeader;

        this.secondLevel = secondLevel;

        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return parentHeaders.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.first_row, parent, false);
        TextView text = convertView.findViewById(R.id.rowParentText);
        text.setText(this.parentHeaders[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        String[] headers = secondLevel.get(groupPosition);


        List<TableModel[]> childData = new ArrayList<>();
        HashMap<String, TableModel[]> secondLevelData = data.get(groupPosition);

        for (String key : secondLevelData.keySet()) {


            childData.add(secondLevelData.get(key));

        }


        secondLevelELV.setAdapter(new SecondLevelAdapter(context, headers, childData));

        secondLevelELV.setGroupIndicator(null);

        secondLevelELV.setDividerHeight(Methods.dpToPx(context, 1f));

        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}