package Domus.DatasetUtils;

public enum SensorState {
    Open("Open"),
    Close("Close"),
    DEBUG("");

    //for debug only
    String label;
    SensorState(String string)
    {
        this.label=string;
    }

    @Override
    public String toString() {
        return label;
    }
}
