package Domus.DatasetUtils;

public record Time(Integer hours, Integer minutes, Integer seconds) {

    public String toString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Time fromString(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return new Time(hours, minutes, seconds);
    }
}
