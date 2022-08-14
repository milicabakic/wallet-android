package rs.raf.wallet.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rs.raf.wallet.database.Database;
import rs.raf.wallet.model.Prihod;
import rs.raf.wallet.model.Rashod;

public class RecyclerViewModelRashod extends ViewModel {

    public final MutableLiveData<List<Rashod>> list = new MutableLiveData<>();
    public final List<Rashod> rashodiList = new ArrayList<>();


    public RecyclerViewModelRashod() {
        if(Database.rashodiList.size() > 0) {
            for(Rashod r : Database.rashodiList){
                if(!(rashodiList.contains(r)))
                     rashodiList.add(r);
            }
        }

        List<Rashod> listToSubmit = new ArrayList<>(rashodiList);
        list.setValue(listToSubmit);
    }

    public LiveData<List<Rashod>> getList() {
        return list;
    }

    public List<Rashod> getRashodiList() {
        return rashodiList;
    }

    public void addRashod(Rashod rashod) {
        if(!(rashodiList.contains(rashod)))
            rashodiList.add(rashod);
        List<Rashod> listToSubmit = new ArrayList<>(rashodiList);
        list.setValue(listToSubmit);
    }

    public void editRashod(Rashod rashod){
        System.out.println("EDIT RASHOD : " + rashod);
        for(Rashod r : rashodiList){
            if(r.getId() == rashod.getId()){
                System.out.println("RASHOD PRE EDITA: " + r);
                r.setNaslov(rashod.getNaslov());
                r.setKolicina(rashod.getKolicina());
                if(r.getAudioFile() != null)
                    r.setAudioFile(rashod.getAudioFile());
                else
                    r.setOpis(rashod.getOpis());
                break;
            }
        }

        List<Rashod> listToSubmit = new ArrayList<>(rashodiList);
        list.setValue(listToSubmit);
    }


    public void editRashod(int id, String naslov, int kolicina, String opis){
        for(Rashod rashod : rashodiList){
            if(rashod.getId() == id){
                rashod.setNaslov(naslov);
                rashod.setKolicina(kolicina);
                rashod.setOpis(opis);
                break;
            }
        }

        List<Rashod> listToSubmit = new ArrayList<>(rashodiList);
        list.setValue(listToSubmit);

    }

    public void deleteRashod(int id){
        Rashod toRemove = null;
        for(Rashod rashod : rashodiList){
            if(rashod.getId() == id){
                toRemove = rashod;
                break;
            }
        }

        Database.rashodiList.remove(toRemove);
        rashodiList.remove(toRemove);
        List<Rashod> listToSubmit = new ArrayList<>(rashodiList);
        list.setValue(listToSubmit);

    }

}
