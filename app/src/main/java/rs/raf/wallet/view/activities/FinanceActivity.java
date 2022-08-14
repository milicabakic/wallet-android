package rs.raf.wallet.view.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rs.raf.wallet.R;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;
import timber.log.Timber;

public class FinanceActivity extends AppCompatActivity {

    //codes
    public static String PRIHOD_KEY = "prihodKey";
    public static String RASHOD_KEY = "rashodKey";
    public static final String ACTION_PRIHOD = "View Finance Prihod";
    public static final String ACTION_RASHOD = "View Finance Rashod";

    Prihod prihod;
    Rashod rashod;
    private static boolean flagAudio = false;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private AudioFocusRequest audioFocusRequest;
    private Handler handler = new Handler();

    //View components
    TextView inputNaslov;
    TextView inputKolicina;
    TextView inputOpis;
    ImageView imagePlayAudio;
    ImageView imageStopAudio;
    TextView playerDuration;
    Button btnBack;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financedata);
        init();
    }

    private void init(){
        initView();
        parseIntent();
        initPlayer();
        initListeners();
    }

    private void initView(){
        inputNaslov = findViewById(R.id.naslovText);
        inputKolicina = findViewById(R.id.kolicinaText);
        inputOpis = findViewById(R.id.opisText);
        btnBack = findViewById(R.id.btnBack);
        imagePlayAudio = findViewById(R.id.picturePlayAudio);
        imagePlayAudio.setVisibility(View.INVISIBLE);
        imageStopAudio = findViewById(R.id.pictureStopAudio);
        imageStopAudio.setVisibility(View.INVISIBLE);
        playerDuration = findViewById(R.id.player_duration);
        playerDuration.setVisibility(View.INVISIBLE);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getAction().equals(ACTION_PRIHOD)){
            Toast.makeText(this, "Podaci o prihodu", Toast.LENGTH_SHORT).show();
            this.prihod = (Prihod) intent.getExtras().getSerializable(PRIHOD_KEY);

            if(prihod != null) {
                inputNaslov.setText(prihod.getNaslov());
                inputKolicina.setText(prihod.getKolicina()+"");

                if(prihod.getAudioFile() != null){
                    inputOpis.setVisibility(View.INVISIBLE);

                    imagePlayAudio.setVisibility(View.VISIBLE);
                    playerDuration.setVisibility(View.VISIBLE);
                }
                else
                    inputOpis.setText(prihod.getOpis());
            }
            else{
                Toast.makeText(this, "Oops doslo je do greske :(", Toast.LENGTH_SHORT).show();
            }
        }
        else if(intent.getExtras() != null && intent.getAction().equals(ACTION_RASHOD)){
            Toast.makeText(this, "Podaci o rashodu", Toast.LENGTH_SHORT).show();

            this.rashod = (Rashod) intent.getExtras().getSerializable(RASHOD_KEY);

            if(rashod != null) {
                inputNaslov.setText(rashod.getNaslov());
                inputKolicina.setText(rashod.getKolicina()+"");

                if(rashod.getAudioFile() != null){
                    inputOpis.setVisibility(View.INVISIBLE);

                    imagePlayAudio.setVisibility(View.VISIBLE);
                    playerDuration.setVisibility(View.VISIBLE);
                }
                else
                    inputOpis.setText(rashod.getOpis());
            }
            else{
                Toast.makeText(this, "Oops doslo je do greske :(", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Oops doslo je do greske :(", Toast.LENGTH_SHORT).show();
        }

    }

    private void initListeners(){
        btnBack.setOnClickListener(v -> {
            this.rashod = null;
            this.prihod = null;
            finish();
        });
    }

    private void initPlayer() {
        // Initialize media player
        if((prihod != null && prihod.getAudioFile() != null) || (rashod != null && rashod.getAudioFile() != null)){
            if(prihod != null)
               mediaPlayer = MediaPlayer.create(this, Uri.fromFile(prihod.getAudioFile()));
            else
                mediaPlayer = MediaPlayer.create(this, Uri.fromFile(rashod.getAudioFile()));

            if(mediaPlayer == null)
                System.out.println("MEDIA PLAYER JE NULL");

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // Get duration of media player
            int duration = mediaPlayer.getDuration();
            // Convert millisecond to minute and second
            String sDuration = convertFormat(duration);
            // Set duration on text view
            playerDuration.setText(sDuration);

            initPlayerListeners();
        }
    }

    private void initPlayerListeners(){
        imagePlayAudio.setOnClickListener(v -> play());

        imageStopAudio.setOnClickListener(v -> pause());

        // We have to handle focus changes
        onAudioFocusChangeListener = focusChange -> {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS: {
                    // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a short amount of time.
                    // The AUDIOFOCUS_LOSS case means we've lost audio focus
                    Timber.e("AUDIOFOCUS_LOSS_TRANSIENT or AUDIOFOCUS_LOSS");
                    pause();
                } break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                    // The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                    // our app is allowed to continue playing sound but at a lower volume.
                    Timber.e("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    playerDuck(true);
                } break;
                case AudioManager.AUDIOFOCUS_GAIN: {
                    // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                    Timber.e("AUDIOFOCUS_GAIN");
                    playerDuck(false);
                    play();
                } break;
            }
        };

        mediaPlayer.setOnCompletionListener(mp -> {
            // Hide pause button
            imageStopAudio.setVisibility(View.GONE);
            // Show play button
            imagePlayAudio.setVisibility(View.VISIBLE);
            // Set media player to initial position
            mediaPlayer.seekTo(0);
        });

        // Description of the audioFocusRequest
        audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                .build();
    }

    private void pause() {
        // Hide pause button
        imageStopAudio.setVisibility(View.GONE);
        // Show play button
        imagePlayAudio.setVisibility(View.VISIBLE);
        // Pause media player
        mediaPlayer.pause();
    }

    private void play() {
        // Request audio focus
        int result = audioManager.requestAudioFocus(audioFocusRequest);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Timber.e("AUDIOFOCUS_REQUEST_GRANTED");
            // Hide play button
            imagePlayAudio.setVisibility(View.GONE);
            // Show pause button
            imageStopAudio.setVisibility(View.VISIBLE);
            // Start media player
            mediaPlayer.start();

        }
    }

    private void releaseMediaPlayer() {
        // Release resources
        if(mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        audioManager.abandonAudioFocusRequest(audioFocusRequest);
        // Remove and stop running threads
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    public synchronized void playerDuck(boolean duck) {
        if (mediaPlayer != null) {
            // Reduce the volume when ducking - otherwise play at full volume.
            mediaPlayer.setVolume(duck ? 0.2f : 1.0f, duck ? 0.2f : 1.0f);
        }
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

}
