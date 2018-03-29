package nl.tue.moviematch;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException {
        // declare local variables
        // set the string data to empty
        String data = "";
        // initialize the inputStream
        InputStream inputStream = null;
        // set the url connection
        HttpURLConnection urlConnection = null;

        try {
            // create new URL url
            URL url = new URL(myUrl);
            // make the connection for url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            // initiate the inputStream
            inputStream = urlConnection.getInputStream();
            // create new bufferedreader
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            // create new stringbuffer
            StringBuffer sb = new StringBuffer();

            // create new empty String line
            String line = "";
            // while the line is not null, the stringbuffer appends the line
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // set the data
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            // close the bufferedReader
            br.close();

        } catch (MalformedURLException e) { // catch the MalformedURL
            e.printStackTrace();
        } catch (IOException e) { // catchIOException
            e.printStackTrace();
        }
        finally { // always execute
            // if the inputStream is not null
            if (inputStream != null)
                // close the input stream
                inputStream.close();
            // disconnect the url connection
            urlConnection.disconnect();
        }

        Log.d("data downlaod",data);
        // return the data
        return data;
    }
}