package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INetssCaseService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class MessageProcessingService {
    private static Logger logger = LoggerFactory.getLogger(MessageProcessingService.class);

    private final INetssCaseService netssCaseService;

    @Value("${functional.date}")
    private String date;


    @Value("${functional.prior}")
    private boolean prior;

    private final String KEY_WEEK = "WEEK";
    private final String KEY_YEAR = "YEAR";
    public MessageProcessingService(INetssCaseService netssCaseService) {
        this.netssCaseService = netssCaseService;
    }

    @PostConstruct
    public void initialize() {
        try {
            scheduleDataFetch();
        } catch (Exception e) {
            logger.info("SERVICE ENDED IN ERROR: " + e.getMessage());
        }
        System.exit(0);
    }

    public void scheduleDataFetch() throws DataProcessorException {

        logger.info("SERVICE STARTED");

        var dateData = processDateInput(date);
        // 2023 and 52
        if (!dateData.isEmpty()) {
            netssCaseService.getNetssCases(dateData.get(KEY_YEAR), dateData.get(KEY_WEEK), prior);
        }

        logger.info("SERVICE ENDED");

    }

    protected Map<String, Short>  processDateInput(String date) throws DataProcessorException {
        Date specifiedEndingDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            specifiedEndingDate = formatter.parse(date);
            return getMMWRAndPriorYear(specifiedEndingDate);
        } catch (Exception e) {
            throw new DataProcessorException(e.getMessage());
        }
    }

    protected Map<String, Short> getMMWRAndPriorYear(Date specifiedDate) {
        Map<String, Short> returnMap = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        if (specifiedDate != null) {
            cal.setTime(specifiedDate);
        }

        // Backup to the previous Saturday
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }

        Date lastSaturday = cal.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateLastSaturdayStr = dateFormatter.format(lastSaturday);

        // Get the MMWR week and year for the previous Saturday
        logger.info("Date previous Saturday was {}", dateLastSaturdayStr);
        int[] weekYear = calcMMWR(dateLastSaturdayStr);
        if (weekYear != null && weekYear.length > 0) {
            returnMap.put(KEY_WEEK, (short) weekYear[0]);
            returnMap.put(KEY_YEAR, (short) weekYear[1]);
        }

        logger.info("MMWR Week and Year are: {} {}", returnMap.get(KEY_WEEK), returnMap.get(KEY_YEAR));

        // Find out if Saturday was after the cutoff
        String cutoffDateStr = "05/31/" + dateLastSaturdayStr.substring(dateLastSaturdayStr.length() - 4);

        try {
            Date cutoffDate = dateFormatter.parse(cutoffDateStr);
        } catch (ParseException e) {
            logger.info("Exception processing cutoff date", e);
        }

        return returnMap;
    }

    protected int[] calcMMWR(String pDate) {
        int[] result = {0, 0};

        try {
            // Define constants
            final int SECOND = 1000;
            final int MINUTE = 60 * SECOND;
            final int HOUR = 60 * MINUTE;
            final int DAY = 24 * HOUR;
            final int WEEK = 7 * DAY;

            // Convert to date object
            Date varDate = new SimpleDateFormat("MM/dd/yyyy").parse(pDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(varDate);
            long varTime = cal.getTimeInMillis();

            // Get January 1st of given year
            Date varJan1Date = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/" + cal.get(Calendar.YEAR));
            Calendar calJan1 = Calendar.getInstance();
            calJan1.setTime(varJan1Date);
            int varJan1Day = calJan1.get(Calendar.DAY_OF_WEEK);
            long varJan1Time = calJan1.getTimeInMillis();

            long t = varJan1Time;
            int y = calJan1.get(Calendar.YEAR);
            int w = 0;

            if (varJan1Day < 5) {
                t -= ((varJan1Day - 1) * DAY);
                while (t < varTime) {
                    w++;
                    t += WEEK;
                    t = adjustForDaylightSavings(t);
                }
                if (w == 53) {
                    Date varNextJan1Date = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/" + (cal.get(Calendar.YEAR) + 1));
                    Calendar varNextJan1Cal = Calendar.getInstance();
                    varNextJan1Cal.setTime(varNextJan1Date);
                    int varNextJan1Day = varNextJan1Cal.get(Calendar.DAY_OF_WEEK);
                    if (varNextJan1Day < 5) {
                        y++;
                        w = 1;
                    }
                }
            } else {
                t += ((7 - (varJan1Day - 1)) * DAY);
                while (t <= varTime) {
                    w++;
                    t += WEEK;
                    t = adjustForDaylightSavings(t);
                }
                if (w == 0) {
                    y = adjustYearForWeekZero(t, y);
                }
            }
            result[0] = w;
            result[1] = y;
        } catch (Exception ex) {
            logger.error("Exception occurred while calculating MMWR: ", ex);
        }

        return result;
    }

    protected long adjustForDaylightSavings(long t) {
        Date d = new Date(t);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int h = cal.get(Calendar.HOUR);
        if (h == 1) {
            t -= 60 * 60 * 1000;
        }
        if (h == 23 || h == 11) {
            t += 60 * 60 * 1000;
        }
        return t;
    }

    protected int adjustYearForWeekZero(long t, int y) {
        Date d = new Date(t);
        Calendar dCal = Calendar.getInstance();
        dCal.setTime(d);
        if ((dCal.get(Calendar.MONTH) == 0) && (dCal.get(Calendar.DAY_OF_WEEK) <= 5)) {
            y--;
            int[] prevYearWeek = calcMMWR("12/31/" + y);
            return prevYearWeek[0];
        }
        return y;
    }

}
