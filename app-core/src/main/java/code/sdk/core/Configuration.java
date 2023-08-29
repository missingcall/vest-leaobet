package code.sdk.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private String channel;

    private String brand;

    private String country;

    private String lighterHost;

    private String[] shfSpareHosts;

    private String shfBaseHost;

    /* adjust start */
    private String adjustAppId;

    private String adjustEventStart;

    private String adjustEventGreeting;

    private String adjustEventAccess;

    private String adjustEventUpdated;

    /* facebook start */
    private String facebookAppId = "";

    private String facebookClientToken = "";

    /* thinking data start */
    private String thinkingDataAppId = "";

    private String thinkingDataHost = "";


    private String httpdnsAuthId = "";

    private String httpdnsAppId = "";

    private String httpdnsDesKey = "";

    private String httpdnsIp = "";

    public static Configuration fromJson(String json) {
        Configuration configuration = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            configuration = new Configuration();
            configuration.setChannel(jsonObject.optString("channel"));
            configuration.setBrand(jsonObject.optString("brand"));
            configuration.setCountry(jsonObject.optString("country"));
            configuration.setLighterHost(jsonObject.optString("lighter_host"));
            configuration.setShfBaseHost(jsonObject.optString("shf_base_domain"));
            JSONArray shfSpareHosts = jsonObject.optJSONArray("shf_spare_domains");
            List<String> shfSpareHostList = new ArrayList<>();
            if(shfSpareHosts != null){
                for (int i = 0; i < shfSpareHosts.length(); i++) {
                    shfSpareHostList.add(shfSpareHosts.optString(i));
                }
            }
            configuration.setShfSpareHosts(shfSpareHostList.toArray(new String[]{}));
            configuration.setAdjustAppId(jsonObject.optString("adjust_app_id"));
            configuration.setAdjustEventStart(jsonObject.optString("adjust_event_start"));
            configuration.setAdjustEventGreeting(jsonObject.optString("adjust_event_greeting"));
            configuration.setAdjustEventAccess(jsonObject.optString("adjust_event_access"));
            configuration.setAdjustEventUpdated(jsonObject.optString("adjust_event_updated"));
            configuration.setFacebookAppId(jsonObject.optString("facebook_app_id"));
            configuration.setFacebookClientToken(jsonObject.optString("facebook_client_token"));
            configuration.setThinkingDataAppId(jsonObject.optString("thinking_data_app_id"));
            configuration.setThinkingDataHost(jsonObject.optString("thinking_data_host"));
            configuration.setHttpdnsAuthId(jsonObject.optString("httpdns_auth_id"));
            configuration.setHttpdnsAppId(jsonObject.optString("httpdns_app_id"));
            configuration.setHttpdnsDesKey(jsonObject.optString("httpdns_des_key"));
            configuration.setHttpdnsIp(jsonObject.optString("httpdns_ip"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLighterHost() {
        return lighterHost;
    }

    public void setLighterHost(String lighterHost) {
        this.lighterHost = lighterHost;
    }

    public String[] getShfSpareHosts() {
        return shfSpareHosts;
    }

    public void setShfSpareHosts(String[] shfSpareHosts) {
        this.shfSpareHosts = shfSpareHosts;
    }

    public String getShfBaseHost() {
        return shfBaseHost;
    }

    public void setShfBaseHost(String shfBaseHost) {
        this.shfBaseHost = shfBaseHost;
    }

    public String getAdjustAppId() {
        return adjustAppId;
    }

    public void setAdjustAppId(String adjustAppId) {
        this.adjustAppId = adjustAppId;
    }

    public String getAdjustEventStart() {
        return adjustEventStart;
    }

    public void setAdjustEventStart(String adjustEventStart) {
        this.adjustEventStart = adjustEventStart;
    }

    public String getAdjustEventGreeting() {
        return adjustEventGreeting;
    }

    public void setAdjustEventGreeting(String adjustEventGreeting) {
        this.adjustEventGreeting = adjustEventGreeting;
    }

    public String getAdjustEventAccess() {
        return adjustEventAccess;
    }

    public void setAdjustEventAccess(String adjustEventAccess) {
        this.adjustEventAccess = adjustEventAccess;
    }

    public String getAdjustEventUpdated() {
        return adjustEventUpdated;
    }

    public void setAdjustEventUpdated(String adjustEventUpdated) {
        this.adjustEventUpdated = adjustEventUpdated;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public String getFacebookClientToken() {
        return facebookClientToken;
    }

    public void setFacebookClientToken(String facebookClientToken) {
        this.facebookClientToken = facebookClientToken;
    }

    public String getThinkingDataAppId() {
        return thinkingDataAppId;
    }

    public void setThinkingDataAppId(String thinkingDataAppId) {
        this.thinkingDataAppId = thinkingDataAppId;
    }

    public String getThinkingDataHost() {
        return thinkingDataHost;
    }

    public void setThinkingDataHost(String thinkingDataHost) {
        this.thinkingDataHost = thinkingDataHost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHttpdnsAuthId() {
        return httpdnsAuthId;
    }

    public void setHttpdnsAuthId(String httpdnsAuthId) {
        this.httpdnsAuthId = httpdnsAuthId;
    }

    public String getHttpdnsAppId() {
        return httpdnsAppId;
    }

    public void setHttpdnsAppId(String httpdnsAppId) {
        this.httpdnsAppId = httpdnsAppId;
    }

    public String getHttpdnsDesKey() {
        return httpdnsDesKey;
    }

    public void setHttpdnsDesKey(String httpdnsDesKey) {
        this.httpdnsDesKey = httpdnsDesKey;
    }

    public String getHttpdnsIp() {
        return httpdnsIp;
    }

    public void setHttpdnsIp(String httpdnsIp) {
        this.httpdnsIp = httpdnsIp;
    }
}