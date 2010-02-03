// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.tasks;

import java.util.Vector;

import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.twitter.TwitterApi;
import com.substanceofcode.utils.Log;
import com.sugree.twitter.TwitterController;
import com.sugree.twitter.TwitterException;

public class RequestTimelineTask extends AbstractTask {

    private TwitterController controller;
    private TwitterApi api;
    private int feedType;
    private int pageNo;
    public final static int FEED_FRIENDS = 0;
    public final static int FEED_REPLIES = 1;
    public final static int FEED_USER = 2;
    public final static int FEED_PUBLIC = 3;
    public final static int FEED_DIRECT = 4;
    public final static int FEED_FAVORITES = 5;

    public static String getFeedName(int feedType) {
        switch (feedType) {
            case FEED_FRIENDS:
                return "Home Timeline";
            case FEED_REPLIES:
                return "Replies Messages";
            case FEED_USER:
                return "User Timeline";
            case FEED_DIRECT:
                return "Direct Messages";
            case FEED_FAVORITES:
                return "Favorites Messages";
            default:
                return "";
        }
    }

    public RequestTimelineTask(TwitterController controller, TwitterApi api, int feedType, int pageNo) {
        this.controller = controller;
        this.api = api;
        this.feedType = feedType;
        this.pageNo = pageNo;
    }

    public void doTask() {
        Vector timeline = new Vector();

        try {
            if (feedType == FEED_FRIENDS) {
                timeline = api.requestFriendsTimeline(pageNo);
            } else if (feedType == FEED_REPLIES) {
                timeline = api.requestRepliesTimeline(pageNo);
            } else if (feedType == FEED_USER) {
                timeline = api.requestUserTimeline(pageNo);
            } else if (feedType == FEED_PUBLIC) {
                timeline = api.requestPublicTimeline();
            } else if (feedType == FEED_DIRECT) {
                timeline = api.requestDirectMessagesTimeline(pageNo);
            } else if (feedType == FEED_FAVORITES) {
                timeline = api.requestFavoritesTimeline(pageNo);
            }
//Log.debug("timeline vector:\n" + timeline);

            controller.setCurrentPageNo(pageNo);
            controller.setCurrentFeedType(feedType);


            controller.addTweetsInCache(timeline);
            controller.updateTimeline(timeline);
            controller.showTimeline();
        } catch (TwitterException e) {
            e.printStackTrace();
            controller.showError(e, controller.SCREEN_TIMELINE);
        }
    }
}
