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
import androidx.fragment.app.Fragment;

import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.utils.Methods;

public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        CardView cardViewCadeirasDispensadas = view.findViewById(R.id.card_view_cadeiras);
        ViewGroup viewGroup = (ViewGroup) cardViewCadeirasDispensadas;
        LinearLayout linearLayoutCadeirasDispensadas = (LinearLayout) viewGroup.getChildAt(0);

        for(ClassTableModel classTableModel : iska.findByResultado("Disp.")){
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• "+classTableModel.getDisciplina());
            newTextView.setTextColor(getContext().getResources().getColor(R.color.black));
            newTextView.setTextSize(18);
            linearLayoutCadeirasDispensadas.addView(newTextView);
        }

        CardView cardViewExames = view.findViewById(R.id.card_view_exames);
        viewGroup = (ViewGroup) cardViewExames;
        LinearLayout linearLayoutExames = (LinearLayout) viewGroup.getChildAt(0);

        for(ClassTableModel classTableModel : iska.findByResultado("Exame")){
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• "+classTableModel.getDisciplina());
            newTextView.setTextColor(getContext().getResources().getColor(R.color.black));
            newTextView.setTextSize(18);
            linearLayoutExames.addView(newTextView);
        }

        CardView cardViewRecursos = view.findViewById(R.id.card_view_recursos);
        viewGroup = (ViewGroup) cardViewRecursos;
        LinearLayout linearLayoutRecursos = (LinearLayout) viewGroup.getChildAt(0);

        for(ClassTableModel classTableModel : iska.findByResultado("Recurso")){
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• "+classTableModel.getDisciplina());
            newTextView.setTextColor(getContext().getResources().getColor(R.color.black));
            newTextView.setTextSize(18);
            linearLayoutRecursos.addView(newTextView);
        }

        CardView cardViewCadeirasAtraso = view.findViewById(R.id.card_view_cadeiras);
        viewGroup = (ViewGroup) cardViewCadeirasAtraso;
        LinearLayout linearLayoutCadeirasAtraso = (LinearLayout) viewGroup.getChildAt(0);

        for(ClassTableModel classTableModel : iska.findCadeirasAtraso()){
            TextView newTextView = new TextView(getContext());
            newTextView.setText("• "+classTableModel.getDisciplina());
            newTextView.setTextColor(getContext().getResources().getColor(R.color.black));
            newTextView.setTextSize(18);
            linearLayoutCadeirasAtraso.addView(newTextView);
        }

        tvNomeEstudante.setText(iska.getHomeModel().getNomeEstudante());
        tvNumeroAluno.setText(iska.getHomeModel().getNumeroAluno());
        tvCurso.setText("Curso: "+iska.getHomeModel().getCurso());
        tvTesouraria.setText("Tesouraria: "+iska.getHomeModel().getTesouraria());
        tvAcademica.setText("Estado: "+iska.getHomeModel().getAcademica());
        tvMatricula.setText(iska.getHomeModel().getMatricula());
        tvLectivo.setText(iska.getHomeModel().getLectivo());
        tvTelefone.setText("Telemóvel: "+iska.getHomeModel().getTelefone());
        tvTelefone2.setText("Telemóvel (alternativo): "+iska.getHomeModel().getTelefone2());
        tvEmail.setText("E-mail: "+iska.getHomeModel().getEmail());
        super.onViewCreated(view, savedInstanceState);
    }
}
