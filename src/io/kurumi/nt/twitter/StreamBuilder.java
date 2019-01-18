package io.kurumi.nt.twitter;

import cn.hutool.core.builder.*;
import twitter4j.*;
import io.kurumi.nt.*;

public class StreamBuilder implements Builder<TwitterStream> {

    private TwitterStream stream;
    
    public StreamBuilder(TwiAccount acc) {
        
        stream = new TwitterStreamFactory(acc.createConfig()).getInstance();
        
    }
    
    public StreamBuilder addListener(StatusListener listener) {
        
        stream.addListener(listener);
        
        return this;
        
    }
    
    @Override
    public TwitterStream build() {
        return stream;
    }

}
