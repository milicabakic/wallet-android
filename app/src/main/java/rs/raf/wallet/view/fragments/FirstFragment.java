package rs.raf.wallet.view.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import rs.raf.wallet.R;
import rs.raf.wallet.database.Database;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;
import rs.raf.wallet.view_model.RecyclerViewModelPrihod;
import rs.raf.wallet.view_model.RecyclerViewModelRashod;

public class FirstFragment extends Fragment {

    public int totalPrihodi = 0;
    public int totalRashodi = 0;

    //View components
    TextView prihod;
    TextView rashod;
    TextView razlika;


    public FirstFragment() {
        super(R.layout.fragment_first);
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
    }

    private void initView(View view){

        prihod = view.findViewById(R.id.prihodValue);
        rashod = view.findViewById(R.id.rashodValue);

        if(Database.rashodiList.size() > 0 || Database.prihodiList.size() > 0) {
            totalValuePrihod(Database.prihodiList);
            totalValueRashod(Database.rashodiList);

            prihod.setText(String.valueOf(totalPrihodi));
            rashod.setText(String.valueOf(totalRashodi));
        }
        else {
            prihod.setText("100");
            rashod.setText("50");
        }

        razlika = view.findViewById(R.id.razlikaValue);
        int rezultat = Integer.parseInt(prihod.getText().toString()) - Integer.parseInt(rashod.getText().toString());
        razlika.setText(rezultat+"");
        if( rezultat > 0 ){
            razlika.setTextColor(getResources().getColor(R.color.green));
        }
        else{
            razlika.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("on resume FIRST FRAGMENT");

        prihod = getView().findViewById(R.id.prihodValue);
        rashod = getView().findViewById(R.id.rashodValue);

        if(Database.rashodiList.size() > 0 || Database.prihodiList.size() > 0) {
            totalValuePrihod(Database.prihodiList);
            totalValueRashod(Database.rashodiList);

            prihod.setText(String.valueOf(totalPrihodi));
            rashod.setText(String.valueOf(totalRashodi));
        }
        else {
            prihod.setText("100");
            rashod.setText("50");
        }

        razlika = getView().findViewById(R.id.razlikaValue);
        int rezultat = Integer.parseInt(prihod.getText().toString()) - Integer.parseInt(rashod.getText().toString());
        razlika.setText(rezultat+"");
        if( rezultat > 0 ){
            razlika.setTextColor(getResources().getColor(R.color.green));
        }
        else{
            razlika.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void totalValuePrihod(List<Prihod> list){
        totalPrihodi = 0;
        for(Prihod p : list)
            totalPrihodi += p.getKolicina();
    }

    private void totalValueRashod(List<Rashod> list){
        totalRashodi = 0;
        for(Rashod r : list)
            totalRashodi += r.getKolicina();
    }

}