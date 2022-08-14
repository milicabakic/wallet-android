package rs.raf.wallet.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rs.raf.wallet.database.Database;
import rs.raf.wallet.model.Prihod;

public class RecyclerViewModelPrihod extends ViewModel {

    public final MutableLiveData<List<Prihod>> prihodi = new MutableLiveData<>();
    public final List<Prihod> prihodiList = new ArrayList<>();


    public RecyclerViewModelPrihod() {
        if(Database.prihodiList.size() > 0) {
            for(Prihod p : Database.prihodiList){
                if(!(prihodiList.contains(p)))
                    prihodiList.add(p);
            }
        }

        List<Prihod> listToSubmit = new ArrayList<>(prihodiList);
        prihodi.setValue(listToSubmit);
    }

    public LiveData<List<Prihod>> getPrihodi() {
        return prihodi;
    }

    public List<Prihod> getPrihodiList() {
        return prihodiList;
    }

    public void addPrihod(Prihod prihod) {
        if(!(prihodiList.contains(prihod)))
             prihodiList.add(prihod);
        List<Prihod> listToSubmit = new ArrayList<>(prihodiList);
        prihodi.setValue(listToSubmit);
    }

    public void editPrihod(Prihod prihod){
        for(Prihod p : prihodiList){
            if(p.getId() == prihod.getId()){
                p.setNaslov(prihod.getNaslov());
                p.setKolicina(prihod.getKolicina());
                if(p.getAudioFile() != null)
                    p.setAudioFile(prihod.getAudioFile());
                else
                    p.setOpis(prihod.getOpis());
                break;
            }
        }

        List<Prihod> listToSubmit = new ArrayList<>(prihodiList);
        prihodi.setValue(listToSubmit);
    }

    public void deletePrihod(int id){
        Prihod toRemove = null;
        for(Prihod p : prihodiList){
            if(p.getId() == id){
                toRemove = p;
                break;
            }
        }

        prihodiList.remove(toRemove);
        Database.prihodiList.remove(toRemove);

        List<Prihod> listToSubmit = new ArrayList<>(prihodiList);
        prihodi.setValue(listToSubmit);

    }

}