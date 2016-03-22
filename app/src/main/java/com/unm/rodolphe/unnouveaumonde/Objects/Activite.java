package com.unm.rodolphe.unnouveaumonde.Objects;

public class Activite {

    private int id;
    private String activite;

    public Activite(int id, String activite) {
        this.id = id;
        this.activite = activite;
    }

    public int getId() {
        return id;
    }

    public String getActivite() {
        return activite;
    }
}
