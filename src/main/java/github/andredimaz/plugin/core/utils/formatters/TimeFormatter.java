package github.andredimaz.plugin.core.utils.formatters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeFormatter {
    public static String getRemainingTime(long secondsIn) {
        long dayCount = TimeUnit.SECONDS.toDays(secondsIn);
        long secondsCount = secondsIn - TimeUnit.DAYS.toSeconds(dayCount);
        long hourCount = TimeUnit.SECONDS.toHours(secondsCount);
        secondsCount -= TimeUnit.HOURS.toSeconds(hourCount);
        long minutesCount = TimeUnit.SECONDS.toMinutes(secondsCount);
        secondsCount -= TimeUnit.MINUTES.toSeconds(minutesCount);
        StringBuilder sb = new StringBuilder();
        if (dayCount != 0L) {
            sb.append(String.format("%d %s, ", dayCount, dayCount == 1L ? "dia" : "dias"));
        }

        if (dayCount == 0L && secondsCount == 0L && hourCount != 0L) {
            sb.append(String.format("%d %s ", hourCount, hourCount == 1L ? "hora" : "horas"));
        } else if (hourCount != 0L) {
            sb.append(String.format("%d %s, ", hourCount, hourCount == 1L ? "hora" : "horas"));
        }

        if (minutesCount != 0L) {
            sb.append(String.format("%d %s ", minutesCount, minutesCount == 1L ? "minuto" : "minutos"));
        }

        if (secondsCount != 0L) {
            sb.append(String.format("%d %s", secondsCount, secondsCount == 1L ? "segundo" : "segundos"));
        }

        if (secondsIn == 0L) {
            sb.append(String.format("%s", "0 segundos"));
        }

        return sb.toString();
    }

    public static String getDateFormat(long time) {
        return (new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm")).format(time);
    }
}
