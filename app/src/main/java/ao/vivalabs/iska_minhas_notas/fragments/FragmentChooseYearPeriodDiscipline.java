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
import ao.vivalabs.iska_minhas_notas.models.TableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.three_level_ELV.ThreeLevelListAdapter;
import ao.vivalabs.iska_minhas_notas.utils.Methods;

public class FragmentChooseYearPeriodDiscipline extends Fragment {

    private Context mContext;

    String[] parent = new String[]{};

    final String[] periodos = new String[]{"1º Simestre", "2º Simestre", "Anual"};

    /**
     * Second level array list
     */
    final List<String[]> secondLevel = new ArrayList<>();
    /**
     * Inner level data
     */
    final List<LinkedHashMap<String, TableModel[]>> data = new ArrayList<>();
    private ExpandableListView expandableListView;

    private ThreeLevelListAdapter threeLevelListAdapterAdapter;

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

        List<TableModel> todasCadeiras = iska.getTablesMapList();

        for (String year : parent) {
            LinkedHashMap<String, TableModel[]> thirdLevelPeriodo = new LinkedHashMap<>();
            List<TableModel> currentYear = iska.findAllByYear(year.replace(" Ano", ""), todasCadeiras);

            if (!currentYear.isEmpty()) {
                List<TableModel> simeste1 = iska.findAllByPeriod("1º Semestre", currentYear);
                List<TableModel> simeste2 = iska.findAllByPeriod("2º Semestre", currentYear);
                List<TableModel> anual = iska.findAllByPeriod("Anual", currentYear);

                List<TableModel> cadeiras = new ArrayList<>();

                if (!simeste1.isEmpty()) {

                    for (TableModel cadeira : simeste1) {
                        int tempCurrentYear = cadeira.getTableId();
                        int tempYear = Integer.parseInt(cadeira.getAno().replaceAll("\\D", ""));

                        if (tempYear != tempCurrentYear) {
                            if (cadeira.todosCamposVazios()) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else if (!cadeira.getNotaFinal().equals("-")) {
                            int nota = Integer.parseInt(cadeira.getNotaFinal());
                            if (nota >= 10) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                            } else {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else {
                            cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                        }
                    }
                    thirdLevelPeriodo.put(periodos[0], cadeiras.toArray(new TableModel[0]));
                    cadeiras = new ArrayList<>();
                }

                if(!simeste2.isEmpty()) {

                    for (TableModel cadeira : simeste2) {

                        int tempCurrentYear = cadeira.getTableId();
                        int tempYear = Integer.parseInt(cadeira.getAno().replaceAll("\\D", ""));

                        if (tempYear != tempCurrentYear) {
                            if (cadeira.todosCamposVazios()) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else if (!cadeira.getNotaFinal().equals("-")) {
                            int nota = Integer.parseInt(cadeira.getNotaFinal());
                            if (nota >= 10) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                            } else {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else {
                            cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                        }
                    }
                    thirdLevelPeriodo.put(periodos[1], cadeiras.toArray(new TableModel[0]));
                    cadeiras = new ArrayList<>();
                }

                if(!anual.isEmpty()) {

                    for (TableModel cadeira : anual) {
                        int tempCurrentYear = cadeira.getTableId();
                        int tempYear = Integer.parseInt(cadeira.getAno().replaceAll("\\D", ""));

                        if (tempYear != tempCurrentYear) {
                            if (cadeira.todosCamposVazios()) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else if (!cadeira.getNotaFinal().equals("-")) {
                            int nota = Integer.parseInt(cadeira.getNotaFinal());
                            if (nota >= 10) {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                            } else {
                                cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina() + " - Atraso"));
                            }
                        } else {
                            cadeiras.add(new TableModel(cadeira.getId(), cadeira.getDisciplina()));
                        }
                    }
                    thirdLevelPeriodo.put(periodos[2], cadeiras.toArray(new TableModel[0]));
                }

            }

            data.add(thirdLevelPeriodo);
        }

        expandableListView = view.findViewById(R.id.exp_list_view_year);
        //passing three level of information to constructor
        threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this.getContext(), parent, secondLevel, data);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        expandableListView = null;
        threeLevelListAdapterAdapter = null;
    }
}
