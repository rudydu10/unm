package com.unm.unnouveaumonde.objects;

public class Enfant {

    private int id;
    private String enfant;

    public Enfant(int id, String enfant) {
        this.id = id;
        this.enfant = enfant;
    }

    public int getId() {
        return id;
    }

    public String getEnfant() {
        return enfant;
    }
}
