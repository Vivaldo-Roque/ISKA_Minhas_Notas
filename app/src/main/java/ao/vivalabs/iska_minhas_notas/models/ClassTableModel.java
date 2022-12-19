package ao.vivalabs.iska_minhas_notas.models;

import androidx.annotation.Nullable;

import java.text.Collator;
import java.util.Locale;
import java.util.Objects;

public class ClassTableModel implements Comparable<ClassTableModel> {
    public ClassTableModel(String disciplina, String abreviatura, String ano, String turma, String tipo, String notaFinal, String avaliacaoContinua, String parcelar1, String parcelar2, String finalContinua, String resultado, String exame, String recurso, String epocaEspecial, String melhoria) {
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
    }

    private final String disciplina;
    private final String abreviatura;
    private final String ano;
    private final String turma;
    private final String tipo;
    private final String notaFinal;
    private final String avaliacaoContinua;
    private final String parcelar1;
    private final String parcelar2;
    private final String finalContinua;
    private final String resultado;
    private final String exame;
    private final String recurso;
    private final String epocaEspecial;
    private final String melhoria;

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

    public String[] toArray() {
        return new String[]{
                this.disciplina,
                this.abreviatura,
                this.ano,
                this.turma,
                this.tipo,
                this.notaFinal,
                this.avaliacaoContinua,
                this.parcelar1,
                this.parcelar2,
                this.finalContinua,
                this.resultado,
                this.exame,
                this.recurso,
                this.epocaEspecial,
                this.melhoria
        };
    }

    @Override
    public int compareTo(ClassTableModel otherClassTableModel) {

        Locale locale = new Locale("pt");
        Collator collator = Collator.getInstance(locale);

        return collator.compare(getDisciplina(), otherClassTableModel.disciplina);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(this == other)
            return true;
        if(!(other instanceof ClassTableModel))
            return false;
        ClassTableModel otherPoint= (ClassTableModel) other;
        return Objects.equals(this.disciplina, otherPoint.getDisciplina());
    }
}
