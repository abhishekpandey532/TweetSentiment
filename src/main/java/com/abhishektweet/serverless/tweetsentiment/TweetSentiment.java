package com.abhishektweet.serverless.tweetsentiment;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//Core Tweet Sentiment Logic
@Slf4j
public class TweetSentiment {
    private static final Logger logger = LoggerFactory.getLogger(TweetSentiment.class);

    public void publishSentimentMetrics(List<String> tweetStrings) {
        //log.info("Received Tweets: {}",tweetStrings);
        //System.out.println(tweetStrings);
        //log.info();
        logger.info("Received Tweets:"+String.valueOf(tweetStrings));
    }
}
