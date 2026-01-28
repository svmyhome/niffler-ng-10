package guru.qa.niffler.model.spend;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatterUtil {
    private static final DateTimeFormatter INPUT_FORMATTER =
        DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter OUTPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
    
    public static String formatDate(String dateString) {
        return formatDate(dateString, "N/A");
    }
    
    public static String formatDate(String dateString, String defaultValue) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return defaultValue;
        }
        
        try {
            return ZonedDateTime.parse(dateString, INPUT_FORMATTER)
                .toLocalDate()
                .format(OUTPUT_FORMATTER);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}