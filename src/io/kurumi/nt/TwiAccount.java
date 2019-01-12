package io.kurumi.nt;

import cn.hutool.json.*;
import twitter4j.*;
import twitter4j.conf.*;

import cn.hutool.json.JSONObject;

public class TwiAccount {

    private NTUser user;

    private String apiToken;
    private String apiSecToken;
    private String accToken;
    private String accSecToken;

    public Long accountId;
    public String screenName;
    public String name;

    public TwiAccount(NTUser user, JSONObject json) {

        this(user,
             json.getStr("apiToken"),
             json.getStr("apiSecToken"),
             json.getStr("accToken"),
             json.getStr("accSecToken"));

        accountId = json.getLong("accountId");
        screenName = json.getStr("screenName");
        name = json.getStr("name");

    }

    public TwiAccount(NTUser user, String apiToken, String apiSecToken, String accToken, String accSecToken) {
        this.user = user;
        this.apiToken = apiToken;
        this.apiSecToken = apiSecToken;
        this.accToken = accToken;
        this.accSecToken = accSecToken;
    }

    public boolean refresh() {

        try {
            
            Twitter api = createApi();
            accountId = api.getId();
            User thisAcc = api.showUser(accountId);
            screenName = thisAcc.getScreenName();
            name = thisAcc.getName();
            
            return true;

        } catch (TwitterException e) {}
        
        return false;

    }

    public Twitter createApi() {

        return new TwitterFactory(createConfig()).getInstance();

    }

    public Configuration createConfig() {

        return new ConfigurationBuilder()
            .setOAuthConsumerKey(apiToken)
            .setOAuthConsumerSecret(apiSecToken)
            .setOAuthAccessToken(accToken)
            .setOAuthAccessTokenSecret(accSecToken)
            .build();

    }

    public JSONObject toJsonObject() {

        return new JSONObject()
            .put("apiToken", apiToken)
            .put("apiSecToken", apiSecToken)
            .put("accToken", accToken)
            .put("accSecToken", accSecToken)
            .put("accountId", accountId)
            .put("screenName", screenName)
            .put("name",name);

    }

}
