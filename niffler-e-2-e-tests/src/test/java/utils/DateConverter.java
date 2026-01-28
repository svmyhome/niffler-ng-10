package utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

public class DateConverter {


    public static Date convertToDate(String stringDate) {
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // игнорировать регистр (jan, Jan, JAN)
                .appendPattern("MMM d, yyyy")
                .toFormatter(Locale.US);

        // Парсим в LocalDate
        LocalDate date = LocalDate.parse(stringDate, inputFormatter);

        // Форматируем в нужный формат
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        // Или явно: DateTimeFormatter.ofPattern("yyyy-MM-dd")

        System.out.println("LocalDate: " + date);  // 2026-01-23
        System.out.println("В формате yyyy-MM-dd: " + formattedDate);

        // Если ТРЕБУЕТСЯ именно java.util.Date (для legacy кода):
        java.util.Date legacyDate = java.util.Date.from(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        return legacyDate;
    }
}