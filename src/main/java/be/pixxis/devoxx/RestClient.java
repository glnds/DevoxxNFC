package be.pixxis.devoxx;

import be.pixxis.devoxx.types.NFCAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Gert Leenders
 */
public class RestClient {

    private RestClient() {
    }

    public static void post(final NFCAction nfcAction, final String id) throws IOException {


        final long timestamp = System.currentTimeMillis() / 1000;
        final String data;
        final String path;
        if (nfcAction == NFCAction.VOTE_UP) {

            path =  "/rooms/"+NFCScanner.ROOM_NUMBER +"/votes";
            data = "{" +
                    "timestamp: " + timestamp + "," +
                    "tagId:" + id + "," +
                    "like: true" +
                    "}";

        } else if (nfcAction == NFCAction.VOTE_DOWN) {

            path =  "/rooms/"+NFCScanner.ROOM_NUMBER +"/votes";
            data = "{" +
                    "timestamp: " + timestamp + "," +
                    "tagId:" + id + "," +
                    "like: false" +
                    "}";

        } else if (nfcAction == NFCAction.FAVORITE) {

            path =  "/rooms/"+NFCScanner.ROOM_NUMBER +"/favourites";
            data = "{" +
                    "timestamp: " + timestamp + "," +
                    "tagId:" + id +
                    "}";
        } else {
            throw new IOException("Unknown NFC action.");
        }


        // Encode the query
        String encodedData = URLEncoder.encode(data, "UTF-8");

        // Connect to google.com
        URL url = new URL("http://" + NFCScanner.SERVER_IP + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));

        connection.setUseCaches(false);

        // Write data
        OutputStream os = connection.getOutputStream();
        os.write(encodedData.getBytes());

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = br.readLine()) != null)
            responseSB.append(line);

        // Close streams
        br.close();
        os.close();

        NFCScanner.log(responseSB.toString());
    }
}
