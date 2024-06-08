package space.ngrix.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PluginUpdater {


    private String response;
    private String pluginName;
    private String pluginVersion;

    public PluginUpdater(String pluginName, String version) {
        this.pluginName = pluginName;
        this.pluginVersion = version;
    }
    public boolean checkLicense(){
        try {
            return postWebContent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public String getVersion(){
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("version");
    }

    public boolean isOutdated() {
        JSONObject jsonObject = new JSONObject(response);
        String version = jsonObject.getString("version");
        return !version.equals(this.pluginVersion);
    }


}
