package com.abhishektweet.serverless.tweetsentiment.dagger;

import com.abhishektweet.serverless.tweetsentiment.TweetSentiment;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {
    @Provides
    @Singleton
    public TweetSentiment provideTweetSentiment(){
        return new TweetSentiment();
    }
}
