package rs.raf.wallet.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import rs.raf.wallet.R;
import rs.raf.wallet.database.Database;
import rs.raf.wallet.view.activities.EditUserActivity;
import rs.raf.wallet.view.activities.LoginActivity;
import rs.raf.wallet.view.activities.MainActivity;

public class FourthFragment extends Fragment {

    //View components
    TextView name;
    TextView surname;
    TextView bank;
    Button btnIzmeni;
    Button btnOdjava;


    public FourthFragment() {
        super(R.layout.fragment_fourth);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners(view);
    }

    private void initView(View view){
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        bank = view.findViewById(R.id.bank);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name", ""));
        surname.setText(sharedPreferences.getString("surname", ""));
        bank.setText(sharedPreferences.getString("bank", ""));

        btnIzmeni = view.findViewById(R.id.btnIzmeni);
        btnOdjava = view.findViewById(R.id.btnOdjava);
    }

    private void initListeners(View view){
        btnIzmeni.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditUserActivity.class);
            startActivity(intent);
            getActivity().finish();


        });

        btnOdjava.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Uspesno ste se izlogovali.", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("name", "")
                    .putString("surname", "")
                    .putString("bank", "")
                    .putString("password", "")
                    .apply();

            Database.rashodiList.clear();
            Database.prihodiList.clear();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
//            getActivity().finish();
        });
    }

}