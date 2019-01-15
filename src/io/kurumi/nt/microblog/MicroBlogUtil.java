package io.kurumi.nt.microblog;

import org.mariotaku.microblog.library.*;
import org.mariotaku.restfu.oauth.*;
import io.kurumi.nt.*;
import org.mariotaku.restfu.*;
import org.mariotaku.restfu.http.*;
import org.mariotaku.microblog.library.twitter.*;

public class MicroBlogUtil {
    
    public static final String DEFAULT_TWITTER_API_URL_FORMAT = "https://[DOMAIN.]twitter.com/";
    
    public MicroBlog createApi(ApiToken token) {
        
        RestAPIFactory<MicroBlogException> factory = new RestAPIFactory<>();
        
        OAuthAuthorization auth = new OAuthAuthorization(token.apiToken, token.apiSecToken);

        factory.setAuthorization(auth);

    }
    
    public Endpoint getEndPoint(Class z) {
        
        String domain = "";
        String versionSuffix = "";
        
        if (Twitter.class.isAssignableFrom(z) || MicroBlog.class.isAssignableFrom(z) ) {
            
            domain = "api";
            versionSuffix = "/1.1/";
            
        } else if(TwitterUpload.class.isAssignableFrom(z)) {
            
            domain = "upload";
            versionSuffix = "/1.1/";
            
        } else if(TwitterOAuth.class.isAssignableFrom(z)) {
            
            domain = "api";
            versionSuffix = null;
        } else if(TwitterOAuth2.class.isAssignableFrom(z)) {
            
            domain = "api";
            versionSuffix = null;
            
        }
        
    }
    
}
