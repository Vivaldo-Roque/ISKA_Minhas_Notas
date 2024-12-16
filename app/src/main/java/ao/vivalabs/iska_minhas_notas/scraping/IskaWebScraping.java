package ao.vivalabs.iska_minhas_notas.scraping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ao.vivalabs.iska_minhas_notas.models.HomeModel;
import ao.vivalabs.iska_minhas_notas.models.TableModel;

public class IskaWebScraping {

    private static IskaWebScraping instance = null;
    private List<TableModel> tablesMapList = new ArrayList<>();
    private HomeModel homeModel;
    private TableModel disciplina = null;

    private IskaWebScraping() {

    }

    public static IskaWebScraping getInstance() {
        if (instance == null) {
            instance = new IskaWebScraping();
        }
        return instance;
    }

    public TableModel getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(TableModel disciplina) {
        this.disciplina = disciplina;
    }

    public HomeModel getHomeModel() {
        return this.homeModel;
    }

    public List<TableModel> getTablesMapList() {

        List<TableModel> tempPeriod;
        List<TableModel> res = new ArrayList<>();

        String[] periods = {"1º Semestre", "2º Semestre", "Anual"};

        for (String year : findAllYears(tablesMapList)) {
            for (String period : periods) {
                tempPeriod = new ArrayList<>(findAllByPeriod(period, findAllByYear(year, tablesMapList)));
                Collections.sort(tempPeriod);
                res.addAll(tempPeriod);
            }
        }

        return res;
    }

    public void SetIskaWebScraping(HomeModel homeModel, List<TableModel> tablesMapList) {
        this.homeModel = homeModel;
        this.tablesMapList = tablesMapList;
    }

    public List<TableModel> findByResultado(String resultado) {
        List<TableModel> res = new ArrayList<>();
        String year = getHomeModel().getMatricula().replaceAll("\\D", "");
        List<TableModel> cadeirasAnoAtual = findAllByYear(year, getTablesMapList());
        for (TableModel tableModel : cadeirasAnoAtual) {
            if (resultado.equals("R")) {
                if (!tableModel.getFinalContinua().equals("-")) {
                    int fCont = Integer.parseInt(tableModel.getFinalContinua());
                    if (fCont < 7) {
                        res.add(tableModel);
                    }
                }
                if (!tableModel.getExame().equals("-")) {
                    int exame = Integer.parseInt(tableModel.getExame());
                    if (exame < 10) {
                        res.add(tableModel);
                    }
                }
            }
            if (resultado.equals("D")) {
                if (tableModel.getResultado().equals("Disp.")) {
                    res.add(tableModel);
                    System.out.println(tableModel.getDisciplina());
                }
            }
            if (resultado.equals("E")) {
                if (tableModel.getResultado().equals("Exame")) {
                    res.add(tableModel);
                }
            }
        }

        return res;
    }

    public List<TableModel> findCadeirasAtraso(List<TableModel> tableModelList) {
        List<TableModel> res = new ArrayList<>();
        // int currentYear = Integer.parseInt(getHomeModel().getMatricula().replaceAll("[^0-9]", ""));
        for (TableModel tableModel : tableModelList) {
            int year = tableModel.getTableId();
            int tempYear = Integer.parseInt(tableModel.getAno().replaceAll("\\D", ""));

            if (tempYear != year) {
                if (tableModel.todosCamposVazios()) {
                    res.add(tableModel);
                } else if (!tableModel.getNotaFinal().equals("-")) {
                    int nota = Integer.parseInt(tableModel.getNotaFinal());
                    if (nota < 10) {
                        res.add(tableModel);
                    }
                }
            }
        }

        return res;
    }

    public String[] findAllYears(List<TableModel> tableModels) {

        HashSet<String> temp = new HashSet<>();

        for (TableModel tableModel : tableModels) {
            temp.add(tableModel.getAno());
        }

        ArrayList<String> res = new ArrayList<>(temp);

        Collections.sort(res);

        return res.toArray(new String[0]);
    }

    public List<TableModel> findAllByYear(String year, List<TableModel> tableModels) {

        List<TableModel> res = new ArrayList<>();

        for (TableModel tableModel : tableModels) {
            try {
                int ano0 = Integer.parseInt(tableModel.getAno().replaceAll("\\D", ""));
                int ano1 = Integer.parseInt(year.replaceAll("\\D", ""));
                if (ano0 == ano1) {
                    res.add(tableModel);
                }
            } catch (NumberFormatException ex) { // handle your exception
            }
        }

        Collections.sort(res);
        return res;
    }

    public List<TableModel> findAllByPeriod(String period, List<TableModel> tableModels) {

        List<TableModel> res = new ArrayList<>();

        for (TableModel tableModel : tableModels) {
            if (tableModel.getTipo().equals(period)) {
                res.add(tableModel);
            }
        }

        return res;
    }

    public TableModel findByDiscipline(String discipline, List<TableModel> tableModels) {

        for (TableModel tableModel : tableModels) {
            if (tableModel.getDisciplina().equals(discipline)) {
                return tableModel;
            }
        }

        return null;
    }

    public TableModel findById(int id, List<TableModel> tableModels) {

        for (TableModel tableModel : tableModels) {
            if (tableModel.getId() == id) {
                return tableModel;
            }
        }

        return null;
    }
}
