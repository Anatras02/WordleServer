package it.unipi.lab3.abalderi1.data;

import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;
import it.unipi.lab3.abalderi1.data.orm.Orm;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe che rappresenta una parola del giorno associata a un timestamp.

 * @see DailyWordOrm per dettagli sull'ORM associato.
 */
public class DailyWord extends Model implements Cloneable {
    transient DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

    public String word;
    public LocalDateTime dateTime;

    /**
     * Costruttore che inizializza una nuova istanza di DailyWord con la parola fornita e imposta il timestamp corrente.
     *
     * @param word La parola del giorno.
     */
    public DailyWord(String word) {
        super();

        this.word = word;
        this.dateTime = LocalDateTime.now();
    }

    @Override
    protected Orm getOrm() {
        return dailyWordOrm;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNullElseGet(dateTime, LocalDateTime::now);
    }

    /**
     * Salva la parola del giorno corrente utilizzando l'ORM.
     */
    public void salva() {
        DailyWordOrm dailyWordOrm = (DailyWordOrm) getOrm();

        dailyWordOrm.updateWord(this.word);
    }

    @Override
    public DailyWord clone() {
        try {
            return (DailyWord) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
