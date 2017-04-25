package apps.veery.com.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sijith on 9/15/2016.
 */
public class DateMatcher {

    public static String getTicketDuration(String ticketCreatedDate) {
        String timeDifference = "time error";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (ticketCreatedDate.isEmpty()) {
            return timeDifference;
        }
        else {
            try {
                Date date = simpleDateFormat.parse(ticketCreatedDate);
                long dif = new Date().getTime() - date.getTime();

                long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(dif);
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(dif);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(dif);
                long diffInDays = TimeUnit.MILLISECONDS.toDays(dif);
//                Log.d("Test","days: "+diffInDays+" Hours:"+diffInHours+" Mins:"+diffInMinutes+" sec:"+diffInSeconds);
                if (diffInDays < 1) {
                    if (diffInHours < 1) {
                        //***********
                        if (diffInMinutes < 1) {
                            //***********
                            if (diffInSeconds < 1) {
                                timeDifference = "Just Now";
                            } else {
                                if (diffInSeconds > 1)
                                    timeDifference = diffInSeconds + " Seconds Ago";
                                else
                                    timeDifference = diffInSeconds + " Second Ago";
                            }
                            //********
                        } else {
                            if (diffInMinutes > 1)
                                timeDifference = diffInMinutes + " Minutes Ago";
                            else
                                timeDifference = diffInMinutes + " Minute Ago";
                        }
                        //********
                    } else {
                        if (diffInHours > 1)
                            timeDifference = diffInHours + " Hours Ago";
                        else
                            timeDifference = diffInHours + " Hour Ago";
                    }
                } else {
                    if (diffInDays > 30)
                        timeDifference = "more than 1 month";
                    else {
                        if (diffInDays > 1)
                            timeDifference = diffInDays + " Days ago";
                        else
                            timeDifference = diffInDays + " Day ago";
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeDifference;
    }

    public static String getTimeDuration(String timeSet){
        long millis = Long.valueOf(timeSet);
        String hms = String.format(Locale.getDefault(),"%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        return hms;
    }
}
