package it.unipi.lab3.abalderi1.data;

import it.unipi.lab3.abalderi1.data.orm.Orm;

/**
 * Classe astratta {@code Model} rappresenta un modello generico di dati. Fornisce metodi per salvare
 * il modello e clonarlo.
 */
public abstract class Model {
    /**
     * Ottiene un'istanza dell'ORM associato a questo modello.
     *
     * @return un'istanza di {@link Orm}.
     */
    protected abstract Orm getOrm();

    /**
     * Salva questo modello usando l'ORM associato.
     */
    public void salva() {
        getOrm().salvaSuFile();
    }

    /**
     * Crea e restituisce una copia di questo oggetto.
     *
     * @return una copia di questo oggetto.
     * @throws CloneNotSupportedException se la classe non supporta l'operazione di clonazione.
     */
    public Model clone() throws CloneNotSupportedException {
        return (Model) super.clone();
    }
}
