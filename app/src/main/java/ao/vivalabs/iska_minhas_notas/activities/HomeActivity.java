package ao.vivalabs.iska_minhas_notas.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ao.vivalabs.iska_minhas_notas.R;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentAbout;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentChooseYearPeriodDiscipline;
import ao.vivalabs.iska_minhas_notas.fragments.FragmentHome;
import ao.vivalabs.iska_minhas_notas.scraping.IskaWebScraping;
import ao.vivalabs.iska_minhas_notas.utils.ConvertToTable;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean permissions_granted = false;
    private final String TAG = "ISKA_LOG";
    private DrawerLayout drawer;
    private ConvertToTable convert;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private FragmentChooseYearPeriodDiscipline fragmentChooseYearPeriodDiscipline;
    private FragmentAbout fragmentAbout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        permissions_granted = checkAndRequestPermissions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuCompat.setGroupDividerEnabled(navigationView.getMenu(), true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
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
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawer.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome(), "FRAG_HOME").commit();
                break;
            case R.id.nav_notes:
                drawer.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentChooseYearPeriodDiscipline(), "FRAG_SELECT").commit();
                break;
            case R.id.nav_exportar:
                drawer.closeDrawers();
                exportarDados();
                break;
            case R.id.nav_sobre:
                drawer.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbout(), "FRAG_ABOUT").commit();
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        fragmentChooseYearPeriodDiscipline = (FragmentChooseYearPeriodDiscipline) getSupportFragmentManager().findFragmentByTag("FRAG_SELECT");
        fragmentAbout = (FragmentAbout) getSupportFragmentManager().findFragmentByTag("FRAG_ABOUT");;

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragmentChooseYearPeriodDiscipline != null && fragmentChooseYearPeriodDiscipline.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        }else if(fragmentAbout != null && fragmentAbout.isVisible()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        }else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }else{
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Clique 2 vezes em VOLTAR para sair", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
            }


        }
    }

    private void exportarDados() {

        permissions_granted = checkAndRequestPermissions();

        if(permissions_granted){
            try {
                convert.convert("ISKA_NOTAS.xlsx", IskaWebScraping.getInstance().getTablesMapList());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                convert.convertExcelToPdf("ISKA_NOTAS.xlsx");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String msg = String.format("2 ficheiros (Excel e Pdf) foram salvo em\n \"/Download/ISKA\"");
            dialogMsg("Exportar", msg);
            
        }
    }

    public boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
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
                } catch (Exception e){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    storageActivityResultLauncher.launch(intent);
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

    private void dialogMsg(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showRational(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("TENHO CERTEZA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // proceed with logic by disabling the related features or quit the app.
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("TENTAR NOVAMENTE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkAndRequestPermissions();
                        dialog.dismiss();
                    }
                }).show();

    }

    private void dialogForSettings(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("AGORA NÂO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("DEFINIÇÕES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSettings();
                        dialog.dismiss();
                    }
                }).show();
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // Here we handle the result of our intent
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                if(Environment.isExternalStorageManager()){
                    // permission granted do something
                } else{
                    // permission denied
                }
            } else {

            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

                            if(Objects.equals(op, "WRITE")){
                                dialogForSettings("Permissão escrita negada", "Agora você deve permitir o acesso à escrita nas definições.");
                            } else if (Objects.equals(op, "READ")){
                                dialogForSettings("Permissão leitura negada", "Agora você deve permitir o acesso à leitura nas definições.");
                            }
                        }
                    }
                }
            }
        }
    }
}