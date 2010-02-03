/*
 * TwitterApi.java
 *
 * Copyright (C) 2005-2008 Tommi Laukkanen
 * http://www.substanceofcode.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// modified by XinfengLiu in Dec 2009
package com.substanceofcode.twitter;

import com.substanceofcode.twitter.model.Status;
import com.substanceofcode.utils.HttpUtil;
import com.substanceofcode.utils.Log;
import com.substanceofcode.utils.StringUtil;
import java.io.IOException;
import java.util.Vector;


import com.sugree.twitter.JSONTwitterParser;
import com.sugree.twitter.TwitterException;

/**
 * TwitterApi
 *
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class TwitterApi {

    private String gateway;
    private String source;
    private String username;
    private String password;
    private static final String PUBLIC_TIMELINE_URL = "statuses/public_timeline.json";
    //lxf: using home_timeline instead of fridends_timeline for viewing retweets.
    //private static final String FRIENDS_TIMELINE_URL = "statuses/friends_timeline.json";
    private static final String FRIENDS_TIMELINE_URL = "statuses/home_timeline.json";
    private static final String USER_TIMELINE_URL = "statuses/user_timeline.json";
    private static final String REPLIES_TIMELINE_URL = "statuses/replies.json";
    private static final String STATUS_UPDATE_URL = "statuses/update.json";
    private static final String DIRECT_MESSAGES_URL = "direct_messages.json";
    private static final String FAVORITES_URL = "favorites.json";
    private static final String FAVORITES_CREATE_URL = "favorites/create/%d.json";
    private static final String FAVORITES_DESTROY_URL = "favorites/destroy/%d.json";
//    private static final String TEST_URL = "help/test.json";
//    private static final String SCHEDULE_DOWNTIME_URL = "help/schedule_downtime.json";

    /** Creates a new instance of TwitterApi */
    public TwitterApi(String source) {
        this.source = source; //"jibjib";
        //this.source = "jibjib";
        this.gateway = "http://twitter.com/";
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public void setAlternateAuthentication(boolean flag) {
//        HttpUtil.setAlternateAuthentication(flag);
//    }

    /**
     * Request public timeline from Twitter API.
     * @return Vector containing StatusEntry items.
     */
    public Vector requestPublicTimeline() throws TwitterException {
        HttpUtil.setBasicAuthentication("", "");
        return requestTimeline(gateway + PUBLIC_TIMELINE_URL, "");
    }

    /**
     * Request public timeline from Twitter API.
     * @return Vector containing StatusEntry items.
     */
    public Vector requestFriendsTimeline(int page) throws TwitterException {
        String params = prepareParam(page);
        HttpUtil.setBasicAuthentication(username, password);
        return requestTimeline(gateway + FRIENDS_TIMELINE_URL, params);
    }

    /**
     * Request public timeline from Twitter API.
     * @return Vector containing StatusEntry items.
     */
    public Vector requestUserTimeline(int page) throws TwitterException {
        String params = prepareParam(page);
        HttpUtil.setBasicAuthentication(username, password);
        return requestTimeline(gateway + USER_TIMELINE_URL, params);
    }

    /**
     * Request responses timeline from Twitter API.{
     * @return Vector containing StatusEntry items.
     */
    public Vector requestRepliesTimeline(int page) throws TwitterException {
        String params = prepareParam(page);
        HttpUtil.setBasicAuthentication(username, password);
        return requestTimeline(gateway + REPLIES_TIMELINE_URL, params);
    }

    /**
     * Request direct messages timeline from Twitter API.{
     * @return Vector containing StatusEntry items.
     */
    public Vector requestDirectMessagesTimeline(int page) throws TwitterException {
        String params = prepareParam(page);
        HttpUtil.setBasicAuthentication(username, password);
        return requestTimeline(gateway + DIRECT_MESSAGES_URL, params);
    }

    /**
     * Request favorites timeline from Twitter API.{
     * @return Vector containing StatusEntry items.
     */
    public Vector requestFavoritesTimeline(int page) throws TwitterException {
        String params = prepareParam(page);
        HttpUtil.setBasicAuthentication(username, password);
        return requestTimeline(gateway + FAVORITES_URL, params);
    }

    public Status createFavorite(String id) throws TwitterException {
        HttpUtil.setBasicAuthentication(username, password);
        return requestObject(gateway + FAVORITES_CREATE_URL, id);
    }

    public Status destroyFavorite(String id) throws TwitterException {
        HttpUtil.setBasicAuthentication(username, password);
        return requestObject(gateway + FAVORITES_DESTROY_URL, id);
    }

    public Status updateStatus(String status) throws TwitterException {
        String response = "";
        try {
            String query = "status=" + StringUtil.urlEncode(status);
            HttpUtil.setBasicAuthentication(username, password);
            response = HttpUtil.doPost(gateway + STATUS_UPDATE_URL, query);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.error("Error while updating status: " + ex.getMessage());
            throw new TwitterException("update " + ex.toString());
        }
        //return null;
        return JSONTwitterParser.parseStatus(response);
    }

//    public String requestTest() throws TwitterException {
//        String result = "";
//        HttpUtil.setBasicAuthentication("", "");
//        try {
//            String response = HttpUtil.doGet(gateway + TEST_URL, "");
//            if (response.length() > 0) {
//                result = JSONTwitterParser.parseTest(response);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            throw new TwitterException("request " + ex);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new TwitterException("request " + ex);
//        }
//        return result;
//    }

//    public String requestScheduleDowntime() throws TwitterException {
//        String result = "";
//        HttpUtil.setBasicAuthentication("", "");
//        try {
//            String response = HttpUtil.doGet(gateway + SCHEDULE_DOWNTIME_URL, "");
//            if (response.length() > 0) {
//                result = JSONTwitterParser.parseScheduleDowntime(response);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            throw new TwitterException("request " + ex);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new TwitterException("request " + ex);
//        }
//        return result;
//    }

    private Status requestObject(String url, String id) throws TwitterException {
        String response = "";
        Status status = null;
        try {
            url = StringUtil.replace(url, "%d", id);
            HttpUtil.setBasicAuthentication(username, password);
            response = HttpUtil.doPost(url, ""); //change to doPost from doGet
            status = JSONTwitterParser.parseStatus(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TwitterException("request " + ex);
        }
        return status;
    }

    private Vector requestTimeline(String timelineUrl, String param) throws TwitterException {
        Vector entries = new Vector();
        try {
            String response = HttpUtil.doGet(timelineUrl, param);
            if (response.length() > 0) {
                entries = JSONTwitterParser.parseStatuses(response);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new TwitterException("request " + ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TwitterException("request " + ex);
        }
        return entries;
    }

//    private String addTwitterSourceParam(String param) {
//        String newParam = "";
//        if (param.length() > 0) {
//            newParam = param + "&source=" + source;
//        } else {
//            newParam = "source=" + source;
//        }
//        return newParam;
//    }

    private String prepareParam(int page) {
        String params = "";
        if (page > 0) {
            params = "page=" + String.valueOf(page);
        }
        return params;
    }
}
