package de.hska.stcs.flink.model.cep;

/**
 * Created by patrickwiener on 26.01.17.
 */
public class PumpWarning {

    private double timestamp;
    private String primaryPump;
    private int psop;
    private String secondaryPump;
    private int psos;
    private String warning;

    public PumpWarning(double timestamp, String primaryPump, int psop, String secondaryPump, int psos, String warning) {
        this.timestamp = timestamp;
        this.primaryPump = primaryPump;
        this.psop = psop;
        this.secondaryPump = secondaryPump;
        this.psos = psos;
        this.warning = warning;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrimaryPump() {
        return primaryPump;
    }

    public void setPrimaryPump(String primaryPump) {
        this.primaryPump = primaryPump;
    }

    public int getPsop() {
        return psop;
    }

    public void setPsop(int psop) {
        this.psop = psop;
    }

    public String getSecondaryPump() {
        return secondaryPump;
    }

    public void setSecondaryPump(String secondaryPump) {
        this.secondaryPump = secondaryPump;
    }

    public int getPsos() {
        return psos;
    }

    public void setPsos(int psos) {
        this.psos = psos;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public String toString() {
        return "PumpWarning{" +
                "timestamp=" + timestamp +
                ", primaryPump='" + primaryPump + '\'' +
                ", psop=" + psop +
                ", secondaryPump='" + secondaryPump + '\'' +
                ", psos=" + psos +
                ", warning='" + warning + '\'' +
                '}';
    }
}
