package ao.vivalabs.iska_minhas_notas.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.three_level_ELV.ThreeLevelListAdapter;
import ao.vivalabs.iska_minhas_notas.utils.Methods;

public class FragmentChooseYearPeriodDiscipline extends Fragment {

    private Context mContext;

    String[] parent = new String[]{};

    String[] periodos = new String[]{"1º Simestre", "2º Simestre", "Anual"};

    /**
     * Second level array list
     */
    List<String[]> secondLevel = new ArrayList<>();
    /**
     * Inner level data
     */
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
    private ExpandableListView expandableListView;

    IskaWebScraping iska;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_period_discipline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iska = IskaWebScraping.getInstance();
        iska.findAllYears(iska.getTablesMapList());
        setUpAdapter(view);
    }

    private void setUpAdapter(@NonNull View view) {

        // Adicionar anos ao primeiro nivel da lista
        parent = iska.findAllYears(iska.getTablesMapList());

        // Adicionar periodos ao segundo nivel da lista
        secondLevel.add(periodos);
        secondLevel.add(periodos);
        secondLevel.add(periodos);
        secondLevel.add(periodos);

        for (String year : parent) {
            LinkedHashMap<String, String[]> thirdLevelPeriodo = new LinkedHashMap<>();
            List<ClassTableModel> currentYear = iska.findAllByYear(year.replace(" Ano", ""), iska.getTablesMapList());

            if (!currentYear.isEmpty()) {
                List<ClassTableModel> simeste1 = iska.findAllByPeriod("1º Semestre", currentYear);
                List<ClassTableModel> simeste2 = iska.findAllByPeriod("2º Semestre", currentYear);
                List<ClassTableModel> anual = iska.findAllByPeriod("Anual", currentYear);

                List<String> cadeiras = new ArrayList<>();

                if (!simeste1.isEmpty()) {

                    for (ClassTableModel cadeira : simeste1) {
                        cadeiras.add(cadeira.getDisciplina());
                    }
                    thirdLevelPeriodo.put(periodos[0], cadeiras.toArray(new String[0]));
                    cadeiras = new ArrayList<>();
                }

                if(!simeste2.isEmpty()){

                    for(ClassTableModel cadeira: simeste2){
                        cadeiras.add(cadeira.getDisciplina());
                    }
                    thirdLevelPeriodo.put(periodos[1], cadeiras.toArray(new String[0]));
                    cadeiras = new ArrayList<>();
                }

                if(!anual.isEmpty()){

                    for(ClassTableModel cadeira: anual){
                        cadeiras.add(cadeira.getDisciplina());
                    }
                    thirdLevelPeriodo.put(periodos[2], cadeiras.toArray(new String[0]));
                }

            }

            data.add(thirdLevelPeriodo);
        }

        expandableListView = view.findViewById(R.id.exp_list_view_year);
        //passing three level of information to constructor
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this.getContext(), parent, secondLevel, data);
        expandableListView.setAdapter(threeLevelListAdapterAdapter);
        expandableListView.setDividerHeight(Methods.dpToPx(mContext, 1f));
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
