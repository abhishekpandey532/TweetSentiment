package com.abhishektweet.serverless.tweetsentiment.lambda;

import com.abhishektweet.serverless.tweetsentiment.TweetSentiment;
import com.abhishektweet.serverless.tweetsentiment.dagger.AppComponent;
import com.abhishektweet.serverless.tweetsentiment.dagger.DaggerAppComponent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;

/**
 * Lambda Entry Point
 */
public class TweetSentimentHandler implements RequestHandler<List<String>,Void> {
    private final TweetSentiment tweetSentiment;
    public TweetSentimentHandler(){
        AppComponent component=DaggerAppComponent.create();
        tweetSentiment=component.tweetSentiment();
    }
    @Override
    public Void handleRequest(List<String> tweets, Context context) {
        tweetSentiment.publishSentimentMetrics(tweets);
        return null;
    }
}
