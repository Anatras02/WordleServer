package it.unipi.lab3.abalderi1.data;

import it.unipi.lab3.abalderi1.data.orm.Orm;

public abstract class Model {
    protected abstract Orm getOrm();

    public void salva() {
        getOrm().salvaSuFile();
    }
    
    public Model clone() throws CloneNotSupportedException {
        return (Model) super.clone();
    }
}
