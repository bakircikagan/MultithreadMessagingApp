package network;

import java.util.Calendar;
import java.util.Date;

public final class Log implements Comparable<Log> {
    private final long timestamp;
    private final String message;
    private final String owner;

    private Log(long timestamp, String message, String owner) {
        this.timestamp = timestamp;
        this.message = message;
        this.owner = owner;
    }
    public Log(String message, String owner) {
        this(System.currentTimeMillis(), message, owner);
    }

    public String getMessage() {
        return message;
    }

    public String getOwner() {
        return owner;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Log other) {
        Long myTimestamp = this.timestamp;
        Long otherTimestamp = other.timestamp;
        return myTimestamp.compareTo(otherTimestamp);
    }
    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return formatTime(hours) + ":" + formatTime(minutes) + " " + owner  + ": " + message;
    }
    private static String formatTime(int time) {
        String result = time + "";
        return time > 9 ? result : '0' + result;
    }
}
