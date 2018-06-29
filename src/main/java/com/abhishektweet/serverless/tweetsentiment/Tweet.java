package com.abhishektweet.serverless.tweetsentiment;

import org.json.JSONObject;

public class Tweet {
     private final JSONObject json;
     public Tweet(String tweetJsontring){
         json=new JSONObject(tweetJsontring);
     }
     public String getText(){
         return json.getString("text");
     }
}
