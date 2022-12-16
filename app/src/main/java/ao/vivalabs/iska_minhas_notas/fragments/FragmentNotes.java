package ao.vivalabs.iska_minhas_notas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.R;

public class FragmentNotes extends Fragment {

    TextView tvClassDisciplina;
    TextView tvClassAbreviatura;
    TextView tvClassAno;
    TextView tvClassTurma;
    TextView tvClassTipo;
    TextView tvClassNotaFinal;
    TextView tvClassAvaliacaoContinua;
    TextView tvClassParcelar1;
    TextView tvClassParcelar2;
    TextView tvClassFinalContinua;
    TextView tvClassResultado;
    TextView tvClassExame;
    TextView tvClassRecurso;
    TextView tvClassEpocaEspecial;
    TextView tvClassMelhoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ClassTableModel discipline = IskaWebScraping.getInstance().getDisciplina();

         tvClassDisciplina = view.findViewById(R.id.tvClassNome);
         tvClassAbreviatura = view.findViewById(R.id.tvClassAbreviatura);
         tvClassAno = view.findViewById(R.id.tvClassAno);
         tvClassTurma = view.findViewById(R.id.tvClassTurma);
         tvClassTipo = view.findViewById(R.id.tvClassPeriodo);
         tvClassNotaFinal = view.findViewById(R.id.tvClassNotaFinal);
         tvClassAvaliacaoContinua = view.findViewById(R.id.tvClassAvaliacaoContinua);
         tvClassParcelar1 = view.findViewById(R.id.tvClassParcelar1);
         tvClassParcelar2 = view.findViewById(R.id.tvClassParcelar2);
         tvClassFinalContinua = view.findViewById(R.id.tvClassFinalContinua);
         tvClassResultado = view.findViewById(R.id.tvClassResultado);
         tvClassExame = view.findViewById(R.id.tvClassExame);
         tvClassRecurso = view.findViewById(R.id.tvClassRecurso);
         tvClassEpocaEspecial = view.findViewById(R.id.tvClassEpocaEspecial);
         tvClassMelhoria = view.findViewById(R.id.tvClassMelhoria);

        tvClassDisciplina.setText("Nome: "+discipline.getDisciplina());
        tvClassAbreviatura.setText("Abreviatura: "+discipline.getAbreviatura());
        tvClassAno.setText("Ano acadêmico: "+discipline.getAno());
        tvClassTurma.setText("Turma: "+discipline.getTurma());
        tvClassTipo.setText("Período: "+discipline.getTipo());
        tvClassNotaFinal.setText(discipline.getNotaFinal());
        tvClassAvaliacaoContinua.setText(discipline.getAvaliacaoContinua());
        tvClassParcelar1.setText(discipline.getParcelar1());
        tvClassParcelar2.setText(discipline.getParcelar2());
        tvClassFinalContinua.setText(discipline.getFinalContinua());
        tvClassResultado.setText(discipline.getResultado());
        tvClassExame.setText(discipline.getExame());
        tvClassRecurso.setText(discipline.getRecurso());
        tvClassEpocaEspecial.setText(discipline.getEpocaEspecial());
        tvClassMelhoria.setText(discipline.getMelhoria());
    }
}
