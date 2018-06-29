package com.abhishektweet.serverless.tweetsentiment;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.BatchDetectSentimentItemResult;
import com.amazonaws.services.comprehend.model.BatchDetectSentimentRequest;
import com.amazonaws.services.comprehend.model.BatchDetectSentimentResult;
import com.amazonaws.services.comprehend.model.SentimentScore;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Core Tweet Sentiment Logic
@Slf4j
@RequiredArgsConstructor
public class TweetSentiment {

    @NonNull private final AmazonComprehend comprehend=AmazonComprehendClientBuilder.standard().build();;

    @NonNull private final AmazonCloudWatch cloudWatch=AmazonCloudWatchClientBuilder.standard().build();;

    private static final Logger logger = LoggerFactory.getLogger(TweetSentiment.class);

    public void publishSentimentMetrics(List<String> tweetStrings) {
        List<MetricDatum> metrics = toMetricData(tweetStrings);
        Lists.partition(metrics,20).forEach(this::putMetricData);

//        BatchDetectSentimentResult result = comprehend.batchDetectSentiment(new BatchDetectSentimentRequest()
//                .withLanguageCode("en")
//                .withTextList("Amazon.com, Inc. is located in Seattle, WA and was founded July 5th, 1994 by Jeff Bezos, allowing customers to buy everything from books to blenders. Seattle is north of Portland and south of Vancouver, BC. Other notable Seattle - based companies are Starbucks and Boeing."));
//        result.getResultList().get(0).getSentimentScore();
//        logger.info("Sentiment Results: {}",result);
        //logger.info("Received Tweets:"+String.valueOf(tweetStrings));
    }

    private void putMetricData(List<MetricDatum> metricData) {
        cloudWatch.putMetricData(new PutMetricDataRequest()
        .withNamespace("TweetSentiment")
        .withMetricData(metricData));
    }

    private List<MetricDatum> toMetricData(List<String> tweetStrings) {
        List<String> tweetText=tweetStrings.stream()
                .map(Tweet::new)
                .map(Tweet::getText)
                .collect(Collectors.toList());

        BatchDetectSentimentResult result = comprehend.batchDetectSentiment(new BatchDetectSentimentRequest()
                .withLanguageCode("en")
                .withTextList(tweetText));
        return result.getResultList().stream()
                .map(this::toSentimentMetrics)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<MetricDatum> toSentimentMetrics(BatchDetectSentimentItemResult sentimentItemResult) {
        List<MetricDatum> metrics = new ArrayList<>();
        SentimentScore sentimentScore=sentimentItemResult.getSentimentScore();
        metrics.add(toSentimentMetricDatum("Mixed",sentimentScore.getMixed().doubleValue()));
        metrics.add(toSentimentMetricDatum("Negative",sentimentScore.getNegative().doubleValue()));
        metrics.add(toSentimentMetricDatum("Neutral",sentimentScore.getNeutral().doubleValue()));
        metrics.add(toSentimentMetricDatum("Positive",sentimentScore.getPositive().doubleValue()));

        return metrics;
    }

    private MetricDatum toSentimentMetricDatum(String sentimentType, Double value) {
        return new MetricDatum()
                .withMetricName("SentimentScore")
                .withDimensions(new Dimension()
                .withName("SentimentType")
                .withValue(sentimentType))
                .withValue(value);
    }
}
