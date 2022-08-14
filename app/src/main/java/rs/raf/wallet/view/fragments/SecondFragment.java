package rs.raf.wallet.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import rs.raf.wallet.R;
import rs.raf.wallet.database.Database;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;
import rs.raf.wallet.view_model.RecyclerViewModelPrihod;
import rs.raf.wallet.view_model.RecyclerViewModelRashod;

public class SecondFragment extends Fragment {

    private static int idCounter = 0;

    private MediaRecorder mediaRecorder;
    private final int PERMISSION_ALL = 1;
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public File file;

    //View components
    Spinner spinner;
    EditText inputNaslov;
    EditText inputKolicina;
    Button btnDodaj;
    ImageView audioPicture;
    ImageView recordAudioPicture;
    EditText inputOpis;
    CheckBox audio_checkBox;
    Chronometer chronometer;

    View view;


    public SecondFragment() {
        super(R.layout.fragment_second);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        // Pitamo da li je aplikacija vec prihvatila ove dozvole
        if(hasPermissions(getActivity(), PERMISSIONS)) {
            init(view);
        }else {
            // Ukoliko nije, trazimo ih
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }
        init(view);

    }

    private void init(View view){
        initView(view);
        initListeners(view);
    }

    private void initView(View view){
        spinner = view.findViewById(R.id.spinner);
        btnDodaj = view.findViewById(R.id.btnDodaj);
        inputNaslov = view.findViewById(R.id.inputNaslov);
        inputKolicina = view.findViewById(R.id.inputKolicina);
        inputOpis = view.findViewById(R.id.inputOpis);
        audioPicture = view.findViewById(R.id.pictureAudio);
        audioPicture.setVisibility(View.INVISIBLE);
        recordAudioPicture = view.findViewById(R.id.pictureRecord);
        recordAudioPicture.setVisibility(View.INVISIBLE);
        audio_checkBox = view.findViewById(R.id.checkbox_audio);
        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setVisibility(View.INVISIBLE);
    }

    private void initListeners(View view){
        btnDodaj.setOnClickListener(v -> {
            String spinnerText = spinner.getSelectedItem().toString();
            String naslov = inputNaslov.getText().toString();
            String kolicina = inputKolicina.getText().toString();
            String opis = "";
            if(!audio_checkBox.isChecked()) {
                opis = inputOpis.getText().toString();
            }

            if(naslov.equals("") || kolicina.equals("")){
                Toast.makeText(getActivity(), "Sva polja moraju biti popunjena!", Toast.LENGTH_SHORT).show();
            }
            else if (!isNum(kolicina)) Toast.makeText(getActivity(), "Kolicina mora biti broj!", Toast.LENGTH_SHORT).show();
            else if(inputOpis.getVisibility() == View.VISIBLE && opis.equals("")){
                Toast.makeText(getActivity(), "Morate uneti opis!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(spinnerText.equals("Prihod")){
                    Prihod prihod;

                    if(audio_checkBox.isChecked()) {
                        prihod = new Prihod(idCounter++, naslov, Integer.parseInt(kolicina), file);
                    }
                    else
                        prihod = new Prihod(idCounter++, naslov, Integer.parseInt(kolicina), opis);

                    Database.prihodiList.add(prihod);

                    RecyclerViewModelPrihod recyclerViewModel = ViewModelProviders.of(getActivity()).get(RecyclerViewModelPrihod.class);
                    recyclerViewModel.addPrihod(prihod);

                }
                else{
                    Rashod rashod;

                    if(audio_checkBox.isChecked()){
                        rashod = new Rashod(idCounter++, naslov, Integer.parseInt(kolicina), file);
                    }
                    else
                        rashod = new Rashod(idCounter++, naslov, Integer.parseInt(kolicina), opis);

                    Database.rashodiList.add(rashod);

                    RecyclerViewModelRashod recyclerViewModel = ViewModelProviders.of(getActivity()).get(RecyclerViewModelRashod.class);
                    recyclerViewModel.addRashod(rashod);

                }
                inputNaslov.setText("");
                inputKolicina.setText("");
                inputOpis.setText("");
                file = null;

                Toast.makeText(getActivity(), "Uspesno ste dodali novu finansiju", Toast.LENGTH_SHORT).show();
            }

        });

        audio_checkBox.setOnClickListener(v -> {
            if(audio_checkBox.isChecked()){
                inputOpis.setVisibility(View.INVISIBLE);
                audioPicture.setVisibility(View.VISIBLE);
                chronometer.setVisibility(View.VISIBLE);
            }
            else {
                audioPicture.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);
                inputOpis.setVisibility(View.VISIBLE);
            }
        });

        audioPicture.setOnClickListener(v -> {
            try {
                File folder = new File(getActivity().getFilesDir(), "sounds");
                if(!folder.exists()) folder.mkdir();
                file = new File(folder, "record"+ idCounter +".3gp");
                audioPicture.setVisibility(View.GONE);
                recordAudioPicture.setVisibility(View.VISIBLE);
                initMediaRecorder(file);
                // Pokrecemo snimanje
                mediaRecorder.prepare();
                mediaRecorder.start();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        recordAudioPicture.setOnClickListener(v -> {
            audioPicture.setVisibility(View.VISIBLE);
            recordAudioPicture.setVisibility(View.GONE);
            // Zaustavljamo snimanje i oslobadjamo resurse
            // Metodom stop() se snimljeni resurs cuva u fajlu koji smo prosledili pri inicijalizaciji mediaRecorder-a
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
        });

    }

    private void initMediaRecorder(File file) {
        mediaRecorder = new MediaRecorder();
        // Postavljanje parametara za mediaRecorder
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(file);
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissionsList, int @NotNull [] grantResults) {
        // Ovde dobijamo odgovor na requestPermissions
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
                StringBuilder permissionsDenied = new StringBuilder();
                for(int i=0;i<grantResults.length;i++){
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionsDenied.append("\n").append(permissionsList[i]);
                    }
                }
                if (permissionsDenied.toString().length() == 0) {
                    // Ukoliko nema odbijenih dozvola, nastavljamo dalje
                    init(view);
                }else {
                    Toast.makeText(getActivity(), "Missing permissions! " + permissionsDenied.toString(), Toast.LENGTH_LONG).show();
                    // Ukoliko ima odbijenih dozvola ispisujemo poruku i zatvaramo activity
                    getActivity().finish();
                }
            }
        }
    }


    private boolean isNum(String s){
        try{
            Integer.parseInt(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }



}
