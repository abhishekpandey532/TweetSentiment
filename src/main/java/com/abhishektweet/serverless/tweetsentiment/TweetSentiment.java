package com.abhishektweet.serverless.tweetsentiment;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.BatchDetectSentimentRequest;
import com.amazonaws.services.comprehend.model.BatchDetectSentimentResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//Core Tweet Sentiment Logic
@Slf4j
@RequiredArgsConstructor
public class TweetSentiment {

    @NonNull private final AmazonComprehend comprehend=AmazonComprehendClientBuilder.standard().build();;

    @NonNull private final AmazonCloudWatch cloudWatch=AmazonCloudWatchClientBuilder.standard().build();;

    private static final Logger logger = LoggerFactory.getLogger(TweetSentiment.class);

    public void publishSentimentMetrics(List<String> tweetStrings) {
        BatchDetectSentimentResult result = comprehend.batchDetectSentiment(new BatchDetectSentimentRequest()
                .withLanguageCode("en ")
                .withTextList("Amazon.com, Inc. is located in Seattle, WA and was founded July 5th, 1994 by Jeff Bezos, allowing customers to buy everything from books to blenders. Seattle is north of Portland and south of Vancouver, BC. Other notable Seattle - based companies are Starbucks and Boeing."));
        result.getResultList().get(0).getSentimentScore();
        logger.info("Sentiment Results: {}",result);
        //logger.info("Received Tweets:"+String.valueOf(tweetStrings));
    }
}
