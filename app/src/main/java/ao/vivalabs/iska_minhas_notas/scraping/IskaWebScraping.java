package ao.vivalabs.iska_minhas_notas.scraping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.models.HomeModel;

public class IskaWebScraping {

    private static IskaWebScraping instance = null;
    private List<ClassTableModel> tablesMapList = new ArrayList<>();
    private HomeModel homeModel;
    private ClassTableModel disciplina = null;

    private IskaWebScraping() {

    }

    public static IskaWebScraping getInstance() {
        if (instance == null) {
            instance = new IskaWebScraping();
        }
        return instance;
    }

    public ClassTableModel getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(ClassTableModel disciplina) {
        this.disciplina = disciplina;
    }

    public HomeModel getHomeModel() {
        return this.homeModel;
    }

    public List<ClassTableModel> getTablesMapList() {

        List<ClassTableModel> tempPeriod;
        List<ClassTableModel> res = new ArrayList<>();

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

    public void SetIskaWebScraping(HomeModel homeModel, List<ClassTableModel> tablesMapList) {
        this.homeModel = homeModel;
        this.tablesMapList = tablesMapList;
    }

    public List<ClassTableModel> findByResultado(String resultado) {
        List<ClassTableModel> res = new ArrayList<>();
        String year = getHomeModel().getMatricula().replaceAll("[^0-9]", "");
        List<ClassTableModel> cadeirasAnoAtual = findAllByYear(year, getTablesMapList());
        for (ClassTableModel classTableModel : cadeirasAnoAtual) {
            if (resultado.equals("Recurso")) {
                if (!classTableModel.getFinalContinua().equals("-")) {
                    int fCont = Integer.parseInt(classTableModel.getFinalContinua());
                    if (fCont < 7) {
                        res.add(classTableModel);
                    }
                }
                if (!classTableModel.getExame().equals("-")) {
                    int exame = Integer.parseInt(classTableModel.getExame());
                    if (exame < 10) {
                        res.add(classTableModel);
                    }
                }
            }
            if (classTableModel.getResultado().equals(resultado)) {
                res.add(classTableModel);
            }
        }

        return res;
    }

    public List<ClassTableModel> findCadeirasAtraso(List<ClassTableModel> classTableModelList) {
        List<ClassTableModel> res = new ArrayList<>();
        // int currentYear = Integer.parseInt(getHomeModel().getMatricula().replaceAll("[^0-9]", ""));
        for (ClassTableModel classTableModel : classTableModelList) {
            int year = classTableModel.getTableId();
            int tempYear = Integer.parseInt(classTableModel.getAno().replaceAll("[^0-9]", ""));

            if (tempYear != year) {
                if (classTableModel.todosCamposVazios()) {
                    res.add(classTableModel);
                } else if (!classTableModel.getNotaFinal().equals("-")) {
                    int nota = Integer.parseInt(classTableModel.getNotaFinal());
                    if (nota < 10) {
                        res.add(classTableModel);
                    }
                }
            }
        }

        return res;
    }

    public String[] findAllYears(List<ClassTableModel> classTableModels) {

        HashSet<String> temp = new HashSet<>();

        for (ClassTableModel classTableModel : classTableModels) {
            temp.add(classTableModel.getAno());
        }

        ArrayList<String> res = new ArrayList<>(temp);

        Collections.sort(res);

        return res.toArray(new String[0]);
    }

    public List<ClassTableModel> findAllByYear(String year, List<ClassTableModel> classTableModels) {

        List<ClassTableModel> res = new ArrayList<>();

        for (ClassTableModel classTableModel : classTableModels) {
            if (classTableModel.getAno().equals(year)) {
                res.add(classTableModel);
            }
        }

        Collections.sort(res);

        return res;
    }

    public List<ClassTableModel> findAllByPeriod(String period, List<ClassTableModel> classTableModels) {

        List<ClassTableModel> res = new ArrayList<>();

        for (ClassTableModel classTableModel : classTableModels) {
            if (classTableModel.getTipo().equals(period)) {
                res.add(classTableModel);
            }
        }

        return res;
    }

    public ClassTableModel findByDiscipline(String discipline, List<ClassTableModel> classTableModels) {

        for (ClassTableModel classTableModel : classTableModels) {
            if (classTableModel.getDisciplina().equals(discipline)) {
                return classTableModel;
            }
        }

        return null;
    }

    public ClassTableModel findById(int id, List<ClassTableModel> classTableModels) {

        for (ClassTableModel classTableModel : classTableModels) {
            if (classTableModel.getId() == id) {
                return classTableModel;
            }
        }

        return null;
    }
}
