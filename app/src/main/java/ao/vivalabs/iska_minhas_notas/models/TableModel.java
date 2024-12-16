package ao.vivalabs.iska_minhas_notas.models;

import androidx.annotation.Nullable;

import java.text.Collator;
import java.util.Locale;
import java.util.Objects;

public class TableModel implements Comparable<TableModel> {
    private final int id;
    private String abreviatura;
    private String ano;
    private final String disciplina;
    private String turma;
    private String tipo;
    private String notaFinal;
    private String avaliacaoContinua;
    private String parcelar1;
    private String parcelar2;
    private String finalContinua;
    private String resultado;
    private String exame;
    private String recurso;
    private String epocaEspecial;
    private String melhoria;
    private int tableId;

    public TableModel(int id, String disciplina, String abreviatura, String ano, String turma, String tipo, String notaFinal, String avaliacaoContinua, String parcelar1, String parcelar2, String finalContinua, String resultado, String exame, String recurso, String epocaEspecial, String melhoria, int tableId) {
        this.id = id;
        this.disciplina = disciplina;
        this.abreviatura = abreviatura;
        this.ano = ano;
        this.turma = turma;
        this.tipo = tipo;
        this.notaFinal = notaFinal;
        this.avaliacaoContinua = avaliacaoContinua;
        this.parcelar1 = parcelar1;
        this.parcelar2 = parcelar2;
        this.finalContinua = finalContinua;
        this.resultado = resultado;
        this.exame = exame;
        this.recurso = recurso;
        this.epocaEspecial = epocaEspecial;
        this.melhoria = melhoria;
        this.tableId = tableId;
    }

    public TableModel(int id, String disciplina) {
        this.id = id;
        this.disciplina = disciplina;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getAno() {
        return ano;
    }

    public String getTurma() {
        return turma;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNotaFinal() {
        return notaFinal;
    }

    public String getAvaliacaoContinua() {
        return avaliacaoContinua;
    }

    public String getParcelar1() {
        return parcelar1;
    }

    public String getParcelar2() {
        return parcelar2;
    }

    public String getFinalContinua() {
        return finalContinua;
    }

    public String getResultado() {
        return resultado;
    }

    public String getExame() {
        return exame;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getEpocaEspecial() {
        return epocaEspecial;
    }

    public String getMelhoria() {
        return melhoria;
    }

    public int getTableId() {
        return tableId;
    }

    public int getId() {
        return id;
    }

    public boolean todosCamposVazios() {
        String vazio = "-";
        return (getNotaFinal().equals(vazio) && getAvaliacaoContinua().equals(vazio) && getParcelar1().equals(vazio) && getParcelar2().equals(vazio) && getFinalContinua().equals(vazio) && getResultado().equals(vazio) && getExame().equals(vazio) && getRecurso().equals(vazio) && getEpocaEspecial().equals(vazio) && getMelhoria().equals(vazio));
    }

    @Override
    public int compareTo(TableModel otherTableModel) {

        Locale locale = new Locale("pt");
        Collator collator = Collator.getInstance(locale);

        return collator.compare(getDisciplina(), otherTableModel.disciplina);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(this == other)
            return true;
        if (!(other instanceof TableModel))
            return false;
        TableModel otherPoint = (TableModel) other;
        return Objects.equals(this.disciplina, otherPoint.getDisciplina());
    }
}
