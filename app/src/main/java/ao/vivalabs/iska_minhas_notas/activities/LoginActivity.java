package ao.vivalabs.iska_minhas_notas.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScrapingTask;

public class LoginActivity extends AppCompatActivity {

    boolean isAllFieldsChecked;
    Dialog dialog;
    EditText etNumeroEstudante;
    EditText etSenha;
    Button btnEntrar;
    TextView tvAlert;
    final ExecutorService iskaWebScraping = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        tvAlert = findViewById(R.id.tvAlert);
        etNumeroEstudante = findViewById(R.id.editTextEstudante);
        etSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.buttonEntrar);

        ScrollView scrollView = findViewById(R.id.scrollView1);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));

        
        etNumeroEstudante.clearFocus();

        btnEntrar.setOnClickListener(view -> {

            isAllFieldsChecked = CheckAllFields();
            if (isAllFieldsChecked) {
                btnEntrar.setEnabled(false);
                btnEntrar.setClickable(false);

                // BACKGROUND TASK START
                preExe();
                iskaWebScraping.execute(() -> {
                    // Background
                    new IskaWebScrapingTask(
                            etNumeroEstudante.getText().toString(),
                            etSenha.getText().toString(),
                            (loggedIn, hasError) -> {
                                // Post execute
                                handler.post(() -> postExe(loggedIn, hasError));
                            }
                    );
                });
                // BACKGROUND TASK END
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sobre) {
            Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);

        super.attachBaseContext(newBase);
    }

    private boolean CheckAllFields() {

        if (etNumeroEstudante.length() == 0 && etSenha.length() == 0) {
            etNumeroEstudante.setError("Campo obrigatório");
            etSenha.setError("campo obrigatório");
            return false;
        }

        if (etNumeroEstudante.length() == 0) {
            etNumeroEstudante.setError("Campo obrigatório");
            return false;
        }

        if (etSenha.length() == 0) {
            etSenha.setError("campo obrigatório");
            return false;
        }

        // after all validation return true.
        return true;
    }

    // Implementation of the callback interface
    public void preExe() {
        tvAlert.setVisibility(View.GONE);
        dialog = new Dialog(LoginActivity.this);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void postExe(boolean loggedIn, boolean hasError) {
        if (loggedIn && !hasError) {
            dialog.dismiss();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (!loggedIn && !hasError) {
            String loginErrorMsg = "Não foi possível autenticar, por favor, tente novamente!";
            tvAlert.setText(loginErrorMsg);
            tvAlert.setVisibility(View.VISIBLE);
            dialog.dismiss();
        } else {
            String netErrorMsg = "Erro de conexão";
            tvAlert.setText(netErrorMsg);
            tvAlert.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
        btnEntrar.setEnabled(true);
        btnEntrar.setClickable(true);
    }
}
