package ao.vivalabs.iska_minhas_notas.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScrapingCallback;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScrapingTask;

public class LoginActivity extends AppCompatActivity implements IskaWebScrapingCallback {

    boolean isAllFieldsChecked;
    ProgressDialog dialog;
    EditText etNumeroEstudante;
    EditText etSenha;
    Button btnEntrar;
    TextView tvAlert;
    IskaWebScrapingTask iskaTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        tvAlert = findViewById(R.id.tvAlert);
        etNumeroEstudante = findViewById(R.id.editTextEstudante);
        etSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.buttonEntrar);

        etNumeroEstudante.setText("190210");
        etSenha.setText("006272580LA048");

        etNumeroEstudante.setFocusableInTouchMode(true);
        etSenha.setFocusableInTouchMode(true);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    btnEntrar.setEnabled(false);
                    btnEntrar.setClickable(false);
                    iskaTask = new IskaWebScrapingTask(
                            etNumeroEstudante.getText().toString(),
                            etSenha.getText().toString(),
                            LoginActivity.this
                    );
                    iskaTask.execute();
                }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sobre) {
            Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etNumeroEstudante.clearFocus();
        etSenha.clearFocus();
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
    public void preExecute() {
        tvAlert.setVisibility(View.GONE);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Verificando");
        dialog.setMessage("Por favor aguarde...");
        dialog.show();
    }

    public void postExecute(boolean loggedIn, boolean hasError) {
        btnEntrar.setEnabled(true);
        btnEntrar.setClickable(true);
        if (loggedIn && !hasError) {
            dialog.dismiss();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (!loggedIn && !hasError) {
            tvAlert.setText("Não foi possível autenticar, por favor, tente novamente!");
            tvAlert.setVisibility(View.VISIBLE);
            dialog.dismiss();
        } else if (hasError) {
            tvAlert.setText("Erro de conexão");
            tvAlert.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }
}
