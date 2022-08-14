package rs.raf.wallet.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if(sharedPreferences.getString("name", "").equals("")){
            //podaci prazni, korisnik nije ulogovan
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            //korisnik je vec ulogovan, postoje podaci u SharedPreferences
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
