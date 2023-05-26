package kozmikoda.passwordspace;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Helper class to send reset codes via SMS or E-Mail
 */
public class ResetCodeSender {

    final private static String apiID = "ba0ff55d11927c7a8b4924fd";
    final private static String apiKey = "c3896e07a614220f78b49465";
    final private static String sender = "SMS TEST";
    final private static String apiUrl = "https://api.vatansms.net/api/v1/1toN";


    /**
     * sends reset code using SMS
     * @param message content of the SMS
     * @param phoneNumber where to send the sms
     */
    public static void sendViaSMS(String message, String phoneNumber) {


        String jsonFormData = "{ \"api_id\": \"%s\", \"api_key\": \"%s\", \"sender\": \"%s\", \"message_type\": \"normal\", \"message\": \"%s\", \"phones\": [ \"%s\" ] }".formatted(
                apiID, apiKey, sender, message, phoneNumber
        );


        sendJSON(jsonFormData);

    }

    /**
     * sends JSON over the network to the API url
     * @param JSONData
     */
    private static void sendJSON(String JSONData) {
        try {
            URL url = new URL(apiUrl);

            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoOutput(true);
            connect.setConnectTimeout(5000);
            connect.setDoInput(true);
            connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connect.setRequestMethod("POST");

            OutputStream prepareFormData = connect.getOutputStream();
            prepareFormData.write(JSONData.getBytes(StandardCharsets.UTF_8));
            prepareFormData.close();

            InputStream inputStream = new BufferedInputStream(connect.getInputStream());
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            System.out.println(result);

            inputStream.close();
            connect.disconnect();

        } catch (Exception e) {
            System.out.println("An error occurred while trying to send the SMS: " + e.getMessage());
        }
    }

    /**
     * Same as sendViaSms but this version can get more phone numbers to send at once
     * @param message content of the sms
     * @param phoneNumbers which phones to send the sms
     */
    public static void sendViaSMS(String message, String... phoneNumbers) {

        StringBuilder targets = new StringBuilder();

        for (String number : phoneNumbers) {
            targets.append(number).append(",");
        }


        String jsonFormData = "{ \"api_id\": \"%s\", \"api_key\": \"%s\", \"sender\": \"%s\", \"message_type\": \"normal\", \"message\": \"%s\", \"phones\": [ \"%s\" ] }".formatted(
                apiID, apiKey, sender, message, targets.toString()
        );

        sendJSON(jsonFormData);
    }


}
