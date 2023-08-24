package it.unipi.lab3.abalderi1.data;

import it.unipi.lab3.abalderi1.data.orm.DailyWordOrm;
import it.unipi.lab3.abalderi1.data.orm.Orm;

import java.time.LocalDateTime;

public class DailyWord extends Model implements Cloneable {
    transient DailyWordOrm dailyWordOrm = DailyWordOrm.getInstance();

    public String word;
    public LocalDateTime dateTime;

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
        if (dateTime == null) {
            this.dateTime = LocalDateTime.now();
        } else {
            this.dateTime = dateTime;
        }
    }

    public void salva() {
        DailyWordOrm dailyWordOrm = (DailyWordOrm) getOrm();

        dailyWordOrm.updateWord(this.word);
    }
}
