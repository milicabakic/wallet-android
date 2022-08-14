package rs.raf.wallet.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;
import rs.raf.wallet.view.fragments.ListPrihodFragment;
import rs.raf.wallet.view.fragments.ListRashodFragment;
import rs.raf.wallet.view_model.RecyclerViewModelPrihod;
import rs.raf.wallet.view_model.RecyclerViewModelRashod;

public class EditFinanceActivity extends AppCompatActivity {

    private Prihod prihod;
    private Rashod rashod;
    private MediaRecorder mediaRecorder;
    private File file;
    //flag if user has changed audio file of finance
    private boolean flagRecord = false;
    private static int counter = 0;

    //codes
    public static final String PRIHOD_KEY = "prihodEditKey";
    public static final String RASHOD_KEY = "rashodEditKey";
    public static final String ACTION_PRIHOD = "Edit Finance Prihod";
    public static final String ACTION_RASHOD = "Edit Finance Rashod";

    //View components
    EditText inputNaslov;
    EditText inputKolicina;
    EditText inputOpis;
    ImageView imageSound;
    ImageView imageRecord;
    Chronometer chronometer;
    Button   btnPotvrdi;
    Button   btnPonisti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfinance);
        init();
    }

    private void init(){
        initView();
        parseIntent();
        initListeners();
    }

    private void initView(){
        inputNaslov = findViewById(R.id.naslov);
        inputKolicina = findViewById(R.id.kolicina);
        inputOpis = findViewById(R.id.opis);
        btnPotvrdi = findViewById(R.id.btnPromeni);
        btnPonisti = findViewById(R.id.btnOdustani);
        imageSound = findViewById(R.id.pictureSound);
        imageSound.setVisibility(View.INVISIBLE);
        imageRecord = findViewById(R.id.pictureRecordAudio);
        imageRecord.setVisibility(View.INVISIBLE);
        chronometer = findViewById(R.id.chronometerone);
        chronometer.setVisibility(View.INVISIBLE);
    }

    private void parseIntent(){
        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getAction().equals(ACTION_PRIHOD)){
            Toast.makeText(this, "Podaci o prihodu", Toast.LENGTH_SHORT).show();
            this.prihod = (Prihod) intent.getExtras().getSerializable(PRIHOD_KEY);
            this.rashod = null;

            if(prihod != null) {
                inputNaslov.setText(prihod.getNaslov());
                inputKolicina.setText(prihod.getKolicina()+"");
                if(prihod.getAudioFile() != null){
                   inputOpis.setVisibility(View.INVISIBLE);
                   imageSound.setVisibility(View.VISIBLE);
                }
                else
                   inputOpis.setText(prihod.getOpis());
            }
            else{
                Toast.makeText(this, "Oops doslo je do greske :/", Toast.LENGTH_SHORT).show();
            }
        }
        else if(intent.getExtras() != null && intent.getAction().equals(ACTION_RASHOD)){
            Toast.makeText(this, "Podaci o rashodu", Toast.LENGTH_SHORT).show();
            this.rashod = (Rashod) intent.getExtras().getSerializable(RASHOD_KEY);
            this.prihod = null;

            if(rashod != null) {
                inputNaslov.setText(rashod.getNaslov());
                inputKolicina.setText(rashod.getKolicina()+"");
                if(rashod.getAudioFile() != null){
                    inputOpis.setVisibility(View.INVISIBLE);
                    imageSound.setVisibility(View.VISIBLE);
                }
                else
                    inputOpis.setText(rashod.getOpis());
            }
            else{
                Toast.makeText(this, "Oops doslo je do greske :/", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Oops doslo je do greske :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListeners(){
        Intent returnIntent = new Intent();
        btnPotvrdi.setOnClickListener(v -> {
            if(prihod != null) {
                prihod.setNaslov(inputNaslov.getText().toString());
                prihod.setKolicina(Integer.parseInt(inputKolicina.getText().toString()));
                if(prihod.getAudioFile() != null) {
                    if (flagRecord)
                        prihod.setAudioFile(file);
                }
                else {
                    prihod.setOpis(inputOpis.getText().toString());
                }

                returnIntent.putExtra(ListPrihodFragment.EDIT_PRIHOD_RECEIVED_MESSAGE, prihod);
                setResult(Activity.RESULT_OK, returnIntent);
                flagRecord = false;
            }
            else if(rashod != null){
                rashod.setNaslov(inputNaslov.getText().toString());
                rashod.setKolicina(Integer.parseInt(inputKolicina.getText().toString()));
                if(rashod.getAudioFile() != null) {
                    if (flagRecord)
                        rashod.setAudioFile(file);
                }
                else {
                    rashod.setOpis(inputOpis.getText().toString());
                }

                System.out.println("POKUPLJEN OPIS JE: " + inputOpis.getText().toString());
                System.out.println("RASHOD KOJI SE SALJE: " + rashod);
                returnIntent.putExtra(ListRashodFragment.EDIT_RASHOD_RECEIVED_MESSAGE, rashod);
                setResult(Activity.RESULT_OK, returnIntent);
                flagRecord = false;
            }
            else{
                setResult(Activity.RESULT_CANCELED);
            }

            Toast.makeText(this, "Izmene sacuvane.", Toast.LENGTH_SHORT).show();


        //    Intent intent = new Intent(this, MainActivity.class);
        //    startActivity(intent);

            finish();
        });

        btnPonisti.setOnClickListener(v -> {
            Toast.makeText(this, "Izmene ponistene.", Toast.LENGTH_SHORT).show();

            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        imageSound.setOnClickListener(v -> {
            try {
                File folder = new File(this.getFilesDir(), "sounds");
                if(!folder.exists()) folder.mkdir();
                file = new File(folder, "record"+ counter++ + ".3gp");
                flagRecord = true;
                imageSound.setVisibility(View.GONE);
                imageRecord.setVisibility(View.VISIBLE);
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

        imageRecord.setOnClickListener(v -> {
            imageSound.setVisibility(View.VISIBLE);
            imageRecord.setVisibility(View.GONE);
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


}
