package ao.vivalabs.iska_minhas_notas.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ao.vivalabs.iska_minhas_notas.BuildConfig;
import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentAbout;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentChooseYearPeriodDiscipline;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentHome;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.utils.ConvertToTable;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean permissions_granted = false;
    private DrawerLayout drawer;
    NavigationView navigationView;
    private ConvertToTable convert;
    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    boolean doubleBackToExitPressedOnce = false;
    MenuItem nav_exportar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        permissions_granted = checkAndRequestPermissions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        nav_exportar = menuNav.findItem(R.id.nav_exportar);

        navigationView.setNavigationItemSelectedListener(this);

        MenuCompat.setGroupDividerEnabled(navigationView.getMenu(), true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome(), "FLAG_HOME").commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        convert = new ConvertToTable();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);

        super.attachBaseContext(newBase);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            drawer.closeDrawers();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome(), "FRAG_HOME").commit();
        } else if (itemId == R.id.nav_notes) {
            drawer.closeDrawers();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentChooseYearPeriodDiscipline(), "FRAG_SELECT").commit();
        } else if (itemId == R.id.nav_exportar) {
            drawer.closeDrawers();
            navigationView.setCheckedItem(R.id.nav_home);
            exportarDados();
        } else if (itemId == R.id.nav_sobre) {
            drawer.closeDrawers();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbout(), "FRAG_ABOUT").commit();
        }

        FragmentChooseYearPeriodDiscipline fragmentChooseYearPeriodDiscipline = (FragmentChooseYearPeriodDiscipline) getSupportFragmentManager().findFragmentByTag("FRAG_SELECT");

        if (fragmentChooseYearPeriodDiscipline != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentChooseYearPeriodDiscipline).commit();
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        FragmentHome fragmentHome = (FragmentHome) getSupportFragmentManager().findFragmentByTag("FRAG_HOME");
        FragmentChooseYearPeriodDiscipline fragmentChooseYearPeriodDiscipline = (FragmentChooseYearPeriodDiscipline) getSupportFragmentManager().findFragmentByTag("FRAG_SELECT");
        FragmentAbout fragmentAbout = (FragmentAbout) getSupportFragmentManager().findFragmentByTag("FRAG_ABOUT");
        // FragmentNotes fragmentNotes = (FragmentNotes) getSupportFragmentManager().findFragmentByTag("FRAG_NOTES");

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentChooseYearPeriodDiscipline != null && fragmentChooseYearPeriodDiscipline.isResumed()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome(), "FRAG_HOME").commit();
        } else if (fragmentAbout != null && fragmentAbout.isResumed()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome(), "FRAG_HOME").commit();
        } else if (fragmentHome != null && fragmentHome.isResumed()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Clique 2 vezes em VOLTAR para sair", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void exportarDados() {

        permissions_granted = checkAndRequestPermissions();

        ExecutorService convertTask = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        if (permissions_granted) {

            // Pre execute
            dialog = new Dialog(HomeActivity.this);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            dialog.setContentView(R.layout.loading_dialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            nav_exportar.setEnabled(false);
            convertTask.execute(() -> {
                // Background
                try {
                    convert.convertToExcel("ISKA_NOTAS.xlsx", IskaWebScraping.getInstance().getHomeModel(), IskaWebScraping.getInstance().getTablesMapList());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    convert.convertToPdf("ISKA_NOTAS.pdf", HomeActivity.this, IskaWebScraping.getInstance().getHomeModel(), IskaWebScraping.getInstance().getTablesMapList());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(() -> {
                    // Post execute
                    dialog.dismiss();
                    String msg = "2 ficheiros (Excel e Pdf) foram salvos em\n \"/Download/ISKA\"";
                    dialogMsg(msg);
                    nav_exportar.setEnabled(true);
                });
            });
        }
    }

    public boolean shouldAskPermission() {
        return true;
    }

    private boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                return Environment.isExternalStorageManager();
            } else {
                int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
                return permissionResult != PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    private boolean checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                try {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                    intent.setData(uri);
                    storageActivityResultLauncher.launch(intent);
                } catch (Exception ignored) {

                }
                return false;
            }
        } else {
            for (String permission : permissions) {
                if (shouldAskPermission(this, permission)) {
                    listPermissionsNeeded.add(permission);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }

        return true;
    }

    private void goToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }

    private void dialogMsg(String msg) {
        new AlertDialog.Builder(this).setTitle("Exportar").setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Abrir pasta", (dialog, which) -> {
                    File downloadFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "ISKA");
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", downloadFolder);

                    intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);

                    try {
                        startActivity(intent);
                    } catch (Exception ActivityNotFoundException) {
                        Toast toast = Toast.makeText(HomeActivity.this, "Instale uma aplicação para ler ficheiros", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                })
                .setPositiveButton("Fechar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showRational(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("TENHO CERTEZA", (dialog, which) -> {
                    // proceed with logic by disabling the related features or quit the app.
                    dialog.dismiss();
                })
                .setPositiveButton("TENTAR NOVAMENTE", (dialog, which) -> {
                    checkAndRequestPermissions();
                    dialog.dismiss();
                }).show();

    }

    private void dialogForSettings(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("AGORA NÂO", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("DEFINIÇÕES", (dialog, which) -> {
                    goToSettings();
                    dialog.dismiss();
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String TAG = "HomeActivity";
        Log.d(TAG, "Permission callback called-------");
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();

            // Initialize the map with both permissions
            for (String permission : this.permissions) {
                perms.put(permission, PackageManager.PERMISSION_GRANTED);
            }

            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                for(Integer i :perms.values()){
                    Log.d(TAG, i+"");
                }

                // Check for both permissions
                if (perms.values().stream().allMatch(val -> val == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "read & write permission granted");
                    // process the normal flow
                    //else any one or both the permissions are not granted
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                    // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                    for (String permission : this.permissions) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                            String temp = permission.substring(permission.lastIndexOf('.') + 1);
                            String op = temp.split("_")[0];

                            if(Objects.equals(op, "WRITE")){
                                showRational("Permissão escrita negada", "Sem essa permissão, este aplicativo não consegue gravar ficheiros multimédia. Tem certeza de que deseja negar esta permissão.");
                            } else if (Objects.equals(op, "READ")){
                                showRational("Permissão leitura negada", "Sem essa permissão, este aplicativo não consegue ler ficheiros multimédia. Tem certeza de que deseja negar esta permissão.");
                            }
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //proceed with logic by disabling the related features or quit the app.
                            String temp = permission.substring(permission.lastIndexOf('.') + 1);
                            String op = temp.split("_")[0];

                            if (Objects.equals(op, "WRITE")) {
                                dialogForSettings("Permissão escrita negada", "Agora você deve permitir o acesso à escrita nas definições.");
                            } else if (Objects.equals(op, "READ")) {
                                dialogForSettings("Permissão leitura negada", "Agora você deve permitir o acesso à leitura nas definições.");
                            }
                        }
                    }
                }
            }
        }
    }

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        // Here we handle the result of our intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // permission denied
                showRational("Permissão leitura e escrita negada", "Sem essas permissões, este aplicativo não consegue ler e escrever ficheiros multimédia. Tem certeza de que deseja negar estas permissões.");
            }
        }
    });


}