package quyennv.becamex.voteeoffice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static String ConvertStringDateToString(String dateString, String inputDateFormater, String outputDateFormater, String timeZone) {
        if (dateString == null || dateString.equals(""))
            return "";
        String result = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormater);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            SimpleDateFormat sdf = new SimpleDateFormat(outputDateFormater);
            //   sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            result = sdf.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
        //  System.out.println(convertedDate);
    }
}
