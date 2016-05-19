package com.unm.rodolphe.unnouveaumonde.objects1;

public class Tarif {

    double journee, journeeRepas, demiejournee, demiejourneeRepas, semaine;

    public Tarif(double journee, double journeeRepas, double demiejournee, double demiejourneeRepas, double semaine) {
        this.journee = journee;
        this.journeeRepas = journeeRepas;
        this.demiejournee = demiejournee;
        this.demiejourneeRepas = demiejourneeRepas;
        this.semaine = semaine;
    }

    public double getJournee() {
        return journee;
    }

    public double getJourneeRepas() {
        return journeeRepas;
    }

    public double getDemiejournee() {
        return demiejournee;
    }

    public double getDemiejourneeRepas() {
        return demiejourneeRepas;
    }

    public double getSemaine() {
        return semaine;
    }
}
