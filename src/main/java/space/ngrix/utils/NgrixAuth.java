package space.ngrix.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is responsible for checking the license of a plugin.
 * It communicates with a server to verify the license key and plugin name.
 */
public class NgrixAuth {

    private String licenseKey;
    private String response;
    private String pluginName;
    private boolean isChecked = false;

    /**
     * Constructor for the NgrixAuth class.
     * @param licenseKey The license key to be checked.
     * @param pluginName The name of the plugin to be checked.
     */
    public NgrixAuth(String licenseKey, String pluginName){
        this.licenseKey = licenseKey;
        this.pluginName = pluginName;
    }

    /**
     * Checks the license by sending a POST request to the server.
     * @return true if the license is valid, false otherwise.
     */
    public boolean checkLicense(){
        try {
            return postWebContent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a POST request to the server with the license key and plugin name.
     * @return true if the server response contains "success", false otherwise.
     * @throws Exception if an error occurs during the HTTP request.
     */
    private boolean postWebContent() throws Exception {
        String url = "https://auth.ngrix.space/license";

        String postData = "licenseKey=" + licenseKey;
        postData += "&pluginName=" + pluginName;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData.getBytes());
        os.flush();
        os.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String jsonResponse = response.toString();
        this.response = jsonResponse;
        isChecked = true;
        if (jsonResponse.contains("success")) {
            return true;
        }
        return false;
    }

    /**
     * Returns the server response.
     * @return the server response as a string.
     * @throws IllegalStateException if the license has not been checked yet.
     */
    public String getResponse(){
        if (!isChecked) {
            throw new IllegalStateException("License check has not been done yet.");
        }
        return response;
    }

    /**
     * Extracts the client name from the server response.
     * @return the client name as a string.
     */
    public String getClientName() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("clientName");
    }

    /**
     * Extracts the end date from the server response.
     * @return the end date as a string.
     */
    public String getEndDate() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("endDate");
    }

    /**
     * Checks if the license is valid by examining the server response.
     * @return true if the license is valid, false otherwise.
     */
    public boolean isValid() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getBoolean("isValid");
    }

    /**
     * Extracts the number of days left until the license expires from the server response.
     * @return the number of days left as an integer.
     */
    public Integer getDaysLeft() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getInt("daysLeft");
    }
}
