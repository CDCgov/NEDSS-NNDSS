package gov.cdc.nnddatapollservice.share;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtil {
    public static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
