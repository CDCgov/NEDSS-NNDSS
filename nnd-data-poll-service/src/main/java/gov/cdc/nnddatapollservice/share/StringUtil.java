package gov.cdc.nnddatapollservice.share;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtil {
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

}
