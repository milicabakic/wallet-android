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

public class LoginActivity extends AppCompatActivity {

    private static String sifra = "projekat1";

    //View components
    Button btnLogin;
    EditText inputName;
    EditText inputSurname;
    EditText inputBank;
    EditText inputPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        initView();
        initListeners();
    }

    private void initView(){
        btnLogin = findViewById(R.id.btnLogin);
        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputBank = findViewById(R.id.inputBank);
        inputPassword = findViewById(R.id.inputPassword);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            if(sharedPreferences.getString("name", "").equals("")){
                String name = inputName.getText().toString();
                String surname = inputSurname.getText().toString();
                String bank = inputBank.getText().toString();
                String password = inputPassword.getText().toString();

                //provera validnosti upisanih podataka
                if(name.equals("") || surname.equals("") || bank.equals("") || password.equals("")){
                    Toast.makeText(this, "Sva polja moraju biti popunjena!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 5){
                    Toast.makeText(this, "Sifra mora imati minimum 5 karaktera!", Toast.LENGTH_SHORT).show();
                }
                else if(!(password.equals(sifra))){
                    Toast.makeText(this, "Pogresna sifra!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //ako je sve proslo, upisujemo podatke u SharedPreferences
                    sharedPreferences.edit().putString("name", name)
                            .putString("surname", surname)
                            .putString("bank", bank)
                            .putString("password", password)
                            .apply();

                    System.out.println("name " + sharedPreferences.getString("name", ""));
                    System.out.println("surname " + sharedPreferences.getString("surname", ""));
                    System.out.println("bank " + sharedPreferences.getString("bank", ""));
                    System.out.println("password " + sharedPreferences.getString("password", ""));

                    Toast.makeText(this, "Uspesno ste se ulogovali", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            else{

                Toast.makeText(this, "Korisnik vec ulogovan", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
