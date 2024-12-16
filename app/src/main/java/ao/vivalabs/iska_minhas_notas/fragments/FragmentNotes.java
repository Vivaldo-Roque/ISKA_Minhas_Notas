package ao.vivalabs.iska_minhas_notas.fragments;

import static ao.vivalabs.iska_minhas_notas.utils.Methods.isNumeric;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.models.TableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.utils.MinMaxFilter;

public class FragmentNotes extends Fragment {

    EditText valor1;

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
    EditText valor2;
    EditText valor3;
    EditText valor4;
    double ac, parcelar1, parcelar2, resultado;
    boolean editText1Empty;
    boolean editText2Empty;
    boolean editText3Empty;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TableModel discipline = IskaWebScraping.getInstance().getDisciplina();

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

        valor1 = view.findViewById(R.id.valor_1);
        valor2 = view.findViewById(R.id.valor_2);
        valor3 = view.findViewById(R.id.valor_3);
        valor4 = view.findViewById(R.id.valor_4);

        valor1.setFilters(new InputFilter[]{new MinMaxFilter(0.0, 20.0)});
        valor2.setFilters(new InputFilter[]{new MinMaxFilter(0.0, 20.0)});
        valor3.setFilters(new InputFilter[]{new MinMaxFilter(0.0, 20.0)});
        valor4.setFilters(new InputFilter[]{new MinMaxFilter(0.0, 20.0)});

        String disciplina = "Nome: ".concat(discipline.getDisciplina());
        String abrev = "Abreviatura: ".concat(discipline.getAbreviatura());
        String ano = "Ano acadêmico: ".concat(discipline.getAno());
        String turma = "Turma: ".concat(discipline.getTurma());
        String tipo = "Período: ".concat(discipline.getTipo());

        tvClassDisciplina.setText(disciplina);
        tvClassAbreviatura.setText(abrev);
        tvClassAno.setText(ano);
        tvClassTurma.setText(turma);
        tvClassTipo.setText(tipo);
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

        try {
            ac = Double.parseDouble(tvClassAvaliacaoContinua.getText().toString());
        } catch (Exception e) {
            ac = 0.0;
            editText1Empty = true;
        }

        try {
            parcelar1 = Double.parseDouble(tvClassParcelar1.getText().toString());
        } catch (Exception e) {
            parcelar1 = 0.0;
            editText2Empty = true;
        }

        try {
            parcelar2 = Double.parseDouble(tvClassParcelar2.getText().toString());
        } catch (Exception e) {
            parcelar2 = 0.0;
            editText3Empty = true;
        }

        setupEditTexts();

        valor1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    calc();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        valor2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    calc();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        valor3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    calc();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        valor4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    double value = Double.parseDouble(charSequence.toString());

                    if (value < 10) {
                        valor4.setTextColor(ActivityCompat.getColor(mContext, R.color.red));
                    } else {
                        valor4.setTextColor(ActivityCompat.getColor(mContext, R.color.black));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void calc() {

        if (isNumeric(valor1.getText().toString()) && isNumeric(valor2.getText().toString()) && isNumeric(valor3.getText().toString())) {
            ac = Double.parseDouble(valor1.getText().toString());
            parcelar1 = Double.parseDouble(valor2.getText().toString());
            parcelar2 = Double.parseDouble(valor3.getText().toString());

            resultado = Math.round((ac + parcelar1 + parcelar2) / 3);

            valor4.setText(String.format("%.2f", resultado));
        }
    }

    public void setupEditTexts() {

        valor1.setText(String.format("%.2f", ac));
        valor2.setText(String.format("%.2f", parcelar1));
        valor3.setText(String.format("%.2f", parcelar2));

        resultado = Math.round((ac + parcelar1 + parcelar2) / 3);

        valor4.setText(String.format("%.2f", resultado));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
