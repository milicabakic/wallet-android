package rs.raf.wallet.model;

import java.io.File;
import java.io.Serializable;

public class Prihod implements Serializable {

    private int id;
    private String slika;
    private String naslov;
    private int kolicina;
    private String opis;
    public File audioFile;


    public Prihod(int id, String naslov, int kolicina, String opis){
        this.id = id;
        this.naslov = naslov;
        this.kolicina = kolicina;
        this.opis = opis;
        this.audioFile = null;
    }

    public Prihod(int id, String naslov, int kolicina, File audioFile){
        this.id = id;
        this.naslov = naslov;
        this.kolicina = kolicina;
        this.audioFile = audioFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    @Override
    public String toString() {
        return "Prihod" + id + ": naslov " + naslov+", kolicina " + kolicina + ", opis " + opis + ", file " + audioFile;
    }

}
