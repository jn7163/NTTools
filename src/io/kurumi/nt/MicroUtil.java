package io.kurumi.nt;
import org.mariotaku.microblog.library.*;
import org.mariotaku.restfu.http.*;
import org.mariotaku.restfu.*;
import org.mariotaku.microblog.library.twitter.*;
import javax.xml.parsers.*;

public class MicroUtil {
    
    {
        
        RestAPIFactory<MicroBlogException> factory = new RestAPIFactory<>();
        factory.setEndpoint(new Endpoint("https://example.com"));
        factory.setRestConverterFactory(new APIDataConverterFactory<APIException>());
        factory.setHttpClient(new OkHttpRestClient());
        MicroBlog api = factory.build(MicroBlog.class);
        
    }
    
}
