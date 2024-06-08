package space.ngrix.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NgrixAuth {

    private String licenseKey;
    private String response;
    private String pluginName;
    private boolean isChecked = false;
    public NgrixAuth(String licenseKey, String pluginName){
        this.licenseKey = licenseKey;
        this.pluginName = pluginName;
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

    public String getResponse(){
        if (!isChecked) {
            throw new IllegalStateException("License check has not been done yet.");
        }
        return response;
    }

    public String getClientName() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("clientName");
    }

    public String getEndDate() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("endDate");
    }

    public boolean isValid() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getBoolean("isValid");
    }

    public Integer getDaysLeft() {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getInt("daysLeft");
    }
}
