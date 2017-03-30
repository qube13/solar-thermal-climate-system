package de.hska.stcs.flink.model.cep;

/**
 * Created by patrickwiener on 26.01.17.
 */
public class Pump extends Event {

    private float psop;
    private float psos;

    public Pump(String timestamp, float psop, float psos) {
        super(timestamp);
        this.psop = psop;
        this.psos = psos;
    }

    public float getPsos() {
        return psos;
    }

    public void setPsos(int psos) {
        this.psos = psos;
    }

    public float getPsop() {
        return psop;
    }

    public void setPsop(int psop) {
        this.psop = psop;
    }


    @Override
    public String toString() {
        return "Pump{" +
                "timestamp=" + getTimestamp() +
                ", psop=" + psop +
                ", psos=" + psos +
                '}';
    }
}
