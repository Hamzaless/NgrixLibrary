package space.ngrix.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is responsible for checking the version of a plugin.
 * It communicates with a server to verify the plugin name and version.
 */
public class PluginUpdater {

    private String response;
    private String pluginName;
    private String pluginVersion;

    /**
     * Constructor for the PluginUpdater class.
     * @param pluginName The name of the plugin to be checked.
     * @param version The version of the plugin to be checked.
     */
    public PluginUpdater(String pluginName, String version) {
        this.pluginName = pluginName;
        this.pluginVersion = version;
    }

    /**
     * Checks the version by sending a POST request to the server.
     * @return true if the server response contains "success", false otherwise.
     */
    public boolean checkVersion(){
        try {
            return postWebContent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a POST request to the server with the plugin name.
     * @return true if the server response contains "success", false otherwise.
     * @throws Exception if an error occurs during the HTTP request.
     */
    private boolean postWebContent() throws Exception {
        String url = "https://auth.ngrix.space/version";

        String postData = "pluginName=" + pluginName;

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
        if (jsonResponse.contains("success")) {
            return true;
        }
        return false;
    }

    /**
     * Extracts the version from the server response.
     * @return the version as a string.
     */
    public String getVersion(){
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("version");
    }

    /**
     * Checks if the plugin is outdated by comparing the version from the server response with the local version.
     * @return true if the plugin is outdated, false otherwise.
     */
    public boolean isOutdated() {
        JSONObject jsonObject = new JSONObject(response);
        String version = jsonObject.getString("version");
        return !version.equals(this.pluginVersion);
    }
}