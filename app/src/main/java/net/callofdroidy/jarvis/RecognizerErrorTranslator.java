package net.callofdroidy.jarvis;

/**
 * Created by admin on 28/10/15.
 */
public class RecognizerErrorTranslator {

    private static final String error_1 = "Network operation timed out.";
    private static final String error_2 = "Other network related errors.";
    private static final String error_3 = "Audio recording error.";
    private static final String error_4 = "Server sends error status.";
    private static final String error_5 = "Other client side errors.";
    private static final String error_6 = "No speech input.";
    private static final String error_7 = "No recognition result matched.";
    private static final String error_8 = "RecognitionService busy.";
    private static final String error_9 = "Insufficient permissions";

    public static String translateErrorCode(int errorCode){
        String error_plainText = null;
        switch (errorCode) {
            case 1:
                error_plainText = error_1;
            case 2:
                error_plainText = error_2;
            case 3:
                error_plainText =error_3;
            case 4:
                error_plainText = error_4;
            case 5:
                error_plainText = error_5;
            case 6:
                error_plainText = error_6;
            case 7:
                error_plainText = error_7;
            case 8:
                error_plainText = error_8;
            case 9:
                error_plainText = error_9;
        }
        return error_plainText;
    }
}
