package ao.vivalabs.iska_minhas_notas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.models.TableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;

public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNomeEstudante = view.findViewById(R.id.textViewNomeEstudante);
        TextView tvNumeroAluno = view.findViewById(R.id.textViewNumeroAluno);
        TextView tvCurso = view.findViewById(R.id.textViewCurso);
        TextView tvTesouraria = view.findViewById(R.id.textViewTesouraria);
        TextView tvAcademica = view.findViewById(R.id.textViewAcademica);
        TextView tvMatricula = view.findViewById(R.id.textViewMatricula);
        TextView tvLectivo = view.findViewById(R.id.textViewAnoLectivo);
        TextView tvTelefone = view.findViewById(R.id.textViewTelefone);
        TextView tvTelefone2 = view.findViewById(R.id.textViewTelefone2);
        TextView tvEmail = view.findViewById(R.id.textViewEmail);

        IskaWebScraping iska;

        iska = IskaWebScraping.getInstance();
        ViewGroup viewGroup = view.<CardView>findViewById(R.id.card_view_dispensadas);
        LinearLayout linearLayoutCadeirasDispensadas = (LinearLayout) viewGroup.getChildAt(0);

        for (TableModel tableModel : iska.findByResultado("D")) {
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• ".concat(tableModel.getDisciplina()));
            newTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            newTextView.setTextSize(18);
            linearLayoutCadeirasDispensadas.addView(newTextView);
        }

        viewGroup = view.<CardView>findViewById(R.id.card_view_exames);
        LinearLayout linearLayoutExames = (LinearLayout) viewGroup.getChildAt(0);

        for (TableModel tableModel : iska.findByResultado("E")) {
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• ".concat(tableModel.getDisciplina()));
            newTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            newTextView.setTextSize(18);
            linearLayoutExames.addView(newTextView);
        }

        viewGroup = view.<CardView>findViewById(R.id.card_view_recursos);
        LinearLayout linearLayoutRecursos = (LinearLayout) viewGroup.getChildAt(0);

        for (TableModel tableModel : iska.findByResultado("R")) {
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• ".concat(tableModel.getDisciplina()));
            newTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            newTextView.setTextSize(18);
            linearLayoutRecursos.addView(newTextView);
        }

        viewGroup = view.<CardView>findViewById(R.id.card_view_cadeiras_atraso);
        LinearLayout linearLayoutCadeirasAtraso = (LinearLayout) viewGroup.getChildAt(0);

        for (TableModel tableModel : iska.findCadeirasAtraso(iska.getTablesMapList())) {
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• ".concat(tableModel.getDisciplina()));
            newTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            newTextView.setTextSize(18);
            linearLayoutCadeirasAtraso.addView(newTextView);
        }

        String nome = iska.getHomeModel().getNomeEstudante();
        String numero = iska.getHomeModel().getNumeroAluno();
        String curso = "Curso: ".concat(iska.getHomeModel().getCurso());
        String tesouraria = "Tesouraria: ".concat(iska.getHomeModel().getTesouraria());
        String academica = "Estado: ".concat(iska.getHomeModel().getAcademica());
        String matricula = iska.getHomeModel().getMatricula();
        String lectivo = iska.getHomeModel().getLectivo();
        String telefone = "Telemóvel: ".concat(iska.getHomeModel().getTelefone());
        String telefone2 = "Telemóvel (alternativo): ".concat(iska.getHomeModel().getTelefone2());
        String email = "E-mail: ".concat(iska.getHomeModel().getEmail());


        tvNomeEstudante.setText(nome);
        tvNumeroAluno.setText(numero);
        tvCurso.setText(curso);
        tvTesouraria.setText(tesouraria);
        tvAcademica.setText(academica);
        tvMatricula.setText(matricula);
        tvLectivo.setText(lectivo);
        tvTelefone.setText(telefone);
        tvTelefone2.setText(telefone2);
        tvEmail.setText(email);
    }
}
