package io.kurumi.nt;

import twitter4j.conf.*;
import twitter4j.*;
import cn.hutool.core.util.*;
import cn.hutool.http.*;
import java.util.*;
import twitter4j.auth.*;

public class TwiOAuth {

    private NTUser user;
    private int apiTokenIndex;
    private ApiToken apiToken;
    private Configuration conf;
    private Twitter twitter;
    
    private RequestToken requestToken;

    public TwiOAuth(NTUser user,int apiTokenIndex) {

        this.user = user;
        this.apiTokenIndex = apiTokenIndex;
        this.apiToken = user.getToken(apiTokenIndex);

        this.conf = new ConfigurationBuilder()
            .setOAuthConsumerKey(apiToken.apiToken)
            .setOAuthConsumerSecret(apiToken.apiSecToken)
            .build();

        this.twitter = new TwitterFactory(conf).getInstance();

    }

//    public String createAuthUrl(String callback) {
//
//        try {
//            
//            requestToken = twitter.getOAuthRequestToken(callback);
//
//            return requestToken.getAuthorizationURL();
//
//        } catch (TwitterException e) {}
//
//        return null;
//        
//    }
//   
//    public TwiAccount getAccountByCallbackUrl(String url) {
//        
//        
//        return getAccount(oauthVerifier);
//
//    }
//    
//    public TwiAccount loginAccount(String id,String password) {
//        
//        try {
//            
//           AccessToken token = twitter.getOAuthAccessToken(id, password);
//           return new TwiAccount(user,apiToken.getApiToken(),apiToken.getApiSecToken(),token.getToken(),token.getTokenSecret());
//            
//        } catch (TwitterException e) {}
//        
//        return null;
//
//    }
//    
//    public TwiAccount getAccount(String oauthVerifier) {
//        
//        try {
//            
//           AccessToken token =  twitter.getOAuthAccessToken(requestToken,oauthVerifier);
//            return new TwiAccount(user,apiTokenIndex,token.getToken(),token.getTokenSecret());
//            
//        } catch (TwitterException e) {}
//        
//        return null;
//
//    }
//
}
