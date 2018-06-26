package com.abhishektweet.serverless.tweetsentiment.dagger;

import com.abhishektweet.serverless.tweetsentiment.TweetSentiment;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    TweetSentiment tweetSentiment();
}
