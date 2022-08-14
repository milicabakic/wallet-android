package rs.raf.wallet.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import rs.raf.wallet.R;

public class EditUserActivity extends AppCompatActivity {

    //View components
    EditText nameInput;
    EditText surnameInput;
    EditText bankInput;
    Button btnPotvrdi;
    Button btnPonisti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        init();
    }

    private void init(){
        initView();
        initListeners();
    }

    private void initView(){
        nameInput = findViewById(R.id.nameValue);
        surnameInput = findViewById(R.id.surnameValue);
        bankInput = findViewById(R.id.bankValue);
        btnPonisti = findViewById(R.id.btnPonisti);
        btnPotvrdi = findViewById(R.id.btnPotvrdi);

        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        nameInput.setHint(sharedPreferences.getString("name", ""));
        surnameInput.setHint(sharedPreferences.getString("surname", ""));
        bankInput.setHint(sharedPreferences.getString("bank", ""));

    }

    private void initListeners(){
        btnPotvrdi.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String surname = surnameInput.getText().toString();
            String bank = bankInput.getText().toString();

            SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
            if(!name.equals(""))
                sharedPreferences.edit().putString("name", name).apply();
            if(!surname.equals(""))
               sharedPreferences.edit().putString("surname", surname).apply();
            if(!bank.equals(""))
                sharedPreferences.edit().putString("bank", bank).apply();


            Toast.makeText(this, "Izmene sacuvane.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnPonisti.setOnClickListener(v -> {
            Toast.makeText(this, "Izmene ponistene.", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            finish();
        });

    }

}
