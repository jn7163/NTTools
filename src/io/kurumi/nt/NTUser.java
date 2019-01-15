package io.kurumi.nt;

import cn.hutool.core.io.*;
import cn.hutool.json.*;
import java.io.*;
import java.util.*;

public class NTUser {

    public NTContext context;
    
    public String userId;

    public LinkedList<ApiToken> apiTokens = new LinkedList<>();
    public LinkedHashMap<Long,TwiAccount> twiAccounts = new LinkedHashMap<>();
    public JSONObject userData = new JSONObject();

    public NTUser(NTContext context, String userId) {
        this.context = context;
        this.userId = userId;
        refresh();
    }

    public File getConfigFile() {
        return new File(context.getUserDir(), userId + "/" + "config.json");
    }

    public ApiToken getToken(int index) {

        switch (index) {
            case -1 : return ApiToken.defaultToken;
            case -2 : return ApiToken.twidereToken;
            default : return apiTokens.get(index);
        }

    }

    public void refresh() {

        try {

            String configJson = FileUtil.readUtf8String(getConfigFile());

            JSONObject config = new JSONObject(configJson);
            JSONArray apiTokenList = config.getJSONArray("apiTokens");
            JSONObject twiAccountMap = config.getJSONObject("twiAccounts");
            userData = config.getJSONObject("userData");
           
            apiTokens.clear();
            twiAccounts.clear();
            for (JSONObject tokenObj : ((List<JSONObject>)(Object)apiTokenList)) {
                apiTokens.add(new ApiToken(tokenObj));
            }
            for (Map.Entry<String,JSONObject> userObj : ((Map<String,JSONObject>)(Object)twiAccountMap).entrySet()) {
                TwiAccount acc = new TwiAccount(this, userObj.getValue());
                twiAccounts.put(acc.accountId, acc);
            }

        } catch (IORuntimeException ex) {}

    }
    
    public JSONObject getConfigObject() {
        
        JSONObject config = new JSONObject();

        JSONArray apiTokenList = new JSONArray();
        JSONObject twiAccountMap = new JSONObject();

        for (ApiToken apiToken : apiTokens) {
            apiTokenList.add(apiToken.toJSONObject());
        }
        for (Map.Entry<Long,TwiAccount> acc : twiAccounts.entrySet()) {
            twiAccountMap.put(acc.getKey().toString(), acc.getValue().toJsonObject());
        }

        config.put("apiTokens", apiTokenList);
        config.put("twiAccounts", twiAccountMap);
        config.put("userData",userData);

        return config;
        
    }
    
    
    public void save() {

        FileUtil.writeUtf8String(getConfigObject().toStringPretty(), getConfigFile());
        
    }


}
