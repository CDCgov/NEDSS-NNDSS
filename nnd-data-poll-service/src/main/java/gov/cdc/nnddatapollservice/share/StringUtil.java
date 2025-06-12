package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtil {

    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);


    private StringUtil() {
        //SONARQ
    }
    public static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static boolean hasOnlyOneKey(String keyList) {
        if (keyList == null || keyList.trim().isEmpty()) {
            return false; // No key
        }
        return !keyList.contains(",") && !keyList.contains(" ");
    }

    public static <T> void printDebugLog(String operation, T data, boolean detailLogApplied) {
        if (detailLogApplied) {
            Gson gsonForPrint = new Gson();
            var str = gsonForPrint.toJson(data);
            logger.info("{}:\t{}", operation, str);
        }

    }

}
