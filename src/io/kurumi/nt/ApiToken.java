package io.kurumi.nt;

import cn.hutool.json.*;
import twitter4j.*;
import twitter4j.conf.*;

import cn.hutool.json.JSONObject;

public class ApiToken {

    public static final ApiToken defaultToken = new ApiToken("Default", "ygCoKRB56hIl9c57xVDFlr3AB", "tx6BkPwYIW9TV0zQa2HKxWsF5fgyyawhOcoiXRCWmSaohGJuYz");
    public static final ApiToken twidereToken = new ApiToken("Twidere", "0WEJk1x6AlgtjGRhyABXw", "gWXNqEFhO3fMkAqoIKpTdjK0MOJs68xnOky0FRdDTP8");

    public String apiName;
    public String apiToken;
    public String apiSecToken;
    
    public ApiToken(JSONObject json) {
        this(json.getStr("apiName"),json.getStr("apiToken"),json.getStr("apiSecToken"));
    }
    
    public ApiToken(String apiName, String apiToken, String apiSecToken) {
        this.apiName = apiName;
        this.apiToken = apiToken;
        this.apiSecToken = apiSecToken;
    }

    public Configuration createConfig() {
        
        return new ConfigurationBuilder()
        .setOAuthConsumerKey(apiToken)
        .setOAuthConsumerSecret(apiSecToken)
        .build();
        
    }
    
    public Twitter createApi() {
        
        return new TwitterFactory(createConfig()).getInstance();
        
    }

    public JSONObject toJSONObject() {

        return new JSONObject()
            .put("apiName", apiName)
            .put("apiToken", apiToken)
            .put("apiSecToken", apiSecToken);

    }

}
