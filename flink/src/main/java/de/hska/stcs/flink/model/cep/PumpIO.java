package de.hska.stcs.flink.model.cep;

/**
 * Created by patrickwiener on 26.01.17.
 */
public class PumpIO {

    private double timestamp;
    private String primaryPump;
    private int psop;
    private String secondaryPump;
    private int psos;
    private String msg;

    public PumpIO(double timestamp, String primaryPump, int psop, String secondaryPump, int psos, String msg) {
        this.timestamp = timestamp;
        this.primaryPump = primaryPump;
        this.psop = psop;
        this.secondaryPump = secondaryPump;
        this.psos = psos;
        this.msg = msg;
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

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Pumpmsg{" +
                "timestamp=" + timestamp +
                ", primaryPump='" + primaryPump + '\'' +
                ", psop=" + psop +
                ", secondaryPump='" + secondaryPump + '\'' +
                ", psos=" + psos +
                ", io='" + msg + '\'' +
                '}';
    }
}
