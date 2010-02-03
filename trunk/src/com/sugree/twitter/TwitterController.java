// modified by XinfengLiu in Dec 2009
package com.sugree.twitter;

import java.util.Vector;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;

import com.substanceofcode.twitter.Settings;
import com.substanceofcode.twitter.TwitterApi;
import com.substanceofcode.twitter.model.Status;

import com.substanceofcode.utils.Log;

import com.sugree.twitter.tasks.RequestTimelineTask;
import com.sugree.twitter.tasks.RequestObjectTask;
import com.sugree.twitter.tasks.UpdateStatusTask;
import com.sugree.twitter.views.TimelineScreen;
import com.sugree.twitter.views.SetupScreen;
import com.sugree.twitter.views.StatusScreen;
import com.sugree.twitter.views.UpdateStatusScreen;
//import com.sugree.twitter.views.InsertScreen;
import com.sugree.twitter.views.WaitScreen;
import com.sugree.twitter.views.AlertScreen;
import com.sugree.twitter.views.LogScreen;
import java.util.Hashtable;
import javax.microedition.lcdui.Choice;

public class TwitterController {

    public static final int SCREEN_CURRENT = 0;
    public static final int SCREEN_TIMELINE = 1;
    public static final int SCREEN_UPDATE = 2;
    public static final int SCREEN_STATUS = 3;
    public static final int START_EMPTY_TIMELINE = 0;
    public static final int START_UPDATE = 1;
    public static final int START_FRIENDS_TIMELINE = 2;
    public static final int START_REPLIES_TIMELINE = 3;
    private TwitterMIDlet midlet;
    private TwitterApi api;
    private Display display;
    private Settings settings;
    private TimelineScreen timelineScreen;
    private UpdateStatusScreen updateScreen;
    private StatusScreen statusScreen;
//    private InsertScreen insertScreen;
    private int currentFeedType;
    private int currentPageNo; //lxf
    private Hashtable cachedTweets; //lxf

    public TwitterController(TwitterMIDlet midlet) {
        try {
            this.midlet = midlet;

            //HttpUtil.setUserAgent("jibjib/1.0.0 (j2me)" + " Profile/" + System.getProperty("microedition.profiles") + " Configuration/" + System.getProperty("microedition.configuration"));
            //HttpUtil.setUserAgent("Profile/" + System.getProperty("microedition.profiles") + " Configuration/" + System.getProperty("microedition.configuration"));
            display = Display.getDisplay(midlet);
            api = new TwitterApi(TwitterMIDlet.NAME);
            settings = Settings.getInstance(midlet);
            api.setUsername(settings.getStringProperty(Settings.USERNAME, ""));
            api.setPassword(settings.getStringProperty(Settings.PASSWORD, ""));
            api.setGateway(settings.getStringProperty(Settings.GATEWAY, "http://twitter.com/"));
            //api.setAlternateAuthentication(settings.getBooleanProperty(Settings.ALTERNATE_AUTHEN, false));

            timelineScreen = new TimelineScreen(this);
            //timelineScreen.setLength(settings.getIntProperty(Settings.TIMELINE_LENGTH, 20));
            timelineScreen.setFitPolicy(Choice.TEXT_WRAP_ON);
            updateScreen = new UpdateStatusScreen(this, "");
//            insertScreen = new InsertScreen(this);
            statusScreen = new StatusScreen(this);
            //statusScreen.setTimeOffset(settings.getStringProperty(Settings.TIME_OFFSET, "0000"));

            currentPageNo = 0;
            currentFeedType = -1;
            cachedTweets = new Hashtable();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void addTweetsInCache(Vector timelines) {
        String key = String.valueOf(currentFeedType) + "-" + String.valueOf(currentPageNo);
        cachedTweets.put(key, timelines);
    }

    public void minimize() {
        try {
            display.setCurrent(null);
            midlet.pauseApp();
        } catch (Exception e) {
            Log.error("Minimize: " + e.toString());
            showTimeline();
        }
    }

    public void exit() {
        try {
            midlet.destroyApp(true);
        } catch (Exception e) {
            Log.error("Exit: " + e.toString());
        }
    }

//    private Vector extractWords(Vector statuses) {
//        Vector words = new Vector();
//
//        for (int i = 0; i < statuses.size(); i++) {
//            Status tmp_status = (Status) statuses.elementAt(i);
//            if (!words.contains("@" + tmp_status.getScreenName())) {
//                words.addElement("@" + tmp_status.getScreenName());
//            }
//            String[] splited = StringUtil.split(tmp_status.getText(), " ");
//            for (int j = 0; j < splited.length; j++) {
//                if (splited[j].length() > 1 &&
//                        (splited[j].charAt(0) == '@' || splited[j].charAt(0) == '#')) {
//                    if (!words.contains(splited[j])) {
//                        words.addElement(splited[j]);
//                    }
//                }
//            }
//        }
//        return words;
//    }

//    public void updateStatus(Status status) {
//        timelineScreen.update(status);
//    }
    public void updateTimeline(Vector statuses) {
        timelineScreen.updateTimeline(statuses);
        //Vector words = extractWords(timelineScreen.getTimeline());
        //insertScreen.setWords(words);
    }

//    public String getLastStatus() {
//        return timelineScreen.getLastDate();
//    }
//
//    public String getLastId() {
//        return timelineScreen.getLastId();
//    }
    public void refresh() {
        display.setCurrent(display.getCurrent());
    }

    public void setCurrent(Displayable display) {
        this.display.setCurrent(display);
    }

    public void setCurrent(int id) {
        this.display.setCurrent(getScreen(id));
    }

    public void toggleFavorited(Status status) {
        int objectType = 0;

        if (status.getFavorited()) {
            objectType = RequestObjectTask.FAVORITE_DESTROY;
        } else {
            objectType = RequestObjectTask.FAVORITE_CREATE;
        }
        RequestObjectTask task = new RequestObjectTask(this, api, objectType, String.valueOf(status.getId()));
        WaitScreen wait = new WaitScreen(this, task, SCREEN_STATUS);
        wait.println("updating...");
        wait.start();
        display.setCurrent(wait);
    }

    public void updateStatus(String text) {
        UpdateStatusTask task = new UpdateStatusTask(this, api, text);
        WaitScreen wait = new WaitScreen(this, task, SCREEN_UPDATE);
        wait.println("updating...");
        wait.start();
        display.setCurrent(wait);
    }

    public void fetchTimeline(int feedType, int pageNo) {

        String key = String.valueOf(feedType) + "-" + String.valueOf(pageNo);
        if (cachedTweets.containsKey(key)) {
            currentPageNo = pageNo;
            currentFeedType = feedType;
            updateTimeline((Vector) cachedTweets.get(key));
            showTimeline();
        } else {
            RequestTimelineTask task = new RequestTimelineTask(this, api, feedType, pageNo);
            WaitScreen wait = new WaitScreen(this, task, SCREEN_TIMELINE);
            wait.println("fetching...");
            display.setCurrent(wait);
            wait.start();
        }
    }

    public void setCurrentPageNo(int pageNo) {
        currentPageNo = pageNo;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public int getCurrentFeedType() {
        return currentFeedType;
    }

    public void setCurrentFeedType(int type) {
        currentFeedType = type;
    }

    public void clearCache() {
        cachedTweets.clear();
        currentPageNo = 0;
    }

//    public void fetchTest() {
//        RequestObjectTask task = new RequestObjectTask(this, api, RequestObjectTask.TEST, "");
//        WaitScreen wait = new WaitScreen(this, task, SCREEN_TIMELINE);
//        wait.println("fetching...");
//        wait.start();
//        display.setCurrent(wait);
//    }
//
//    public void fetchScheduleDowntime() {
//        RequestObjectTask task = new RequestObjectTask(this, api, RequestObjectTask.SCHEDULE_DOWNTIME, "");
//        WaitScreen wait = new WaitScreen(this, task, SCREEN_TIMELINE);
//        wait.println("fetching...");
//        wait.start();
//        display.setCurrent(wait);
//    }

//    public void showStart() {
//        int id = settings.getIntProperty(Settings.START_SCREEN, 0);
//        switch (id) {
//            case START_EMPTY_TIMELINE:
//                showTimeline();
//                break;
//            case START_UPDATE:
//                showUpdate();
//                break;
//            case START_FRIENDS_TIMELINE:
//                fetchTimeline(RequestTimelineTask.FEED_FRIENDS, 1);
//                break;
//            case START_REPLIES_TIMELINE:
//                fetchTimeline(RequestTimelineTask.FEED_REPLIES, 1);
//                break;
//        }
//    }

    public void showTimeline() {
        String title = RequestTimelineTask.getFeedName(currentFeedType);
        timelineScreen.setTitle(title);
        display.setCurrent(timelineScreen);
    }

    public void showSetup() {
        SetupScreen setup = new SetupScreen(this, api, timelineScreen, statusScreen);
        display.setCurrent(setup);
    }

    public void showLog() {
        LogScreen log = new LogScreen(this);
        display.setCurrent(log);
    }

    public void showStatus(Status status) {
        this.statusScreen.setStatus(status);
        display.setCurrent(this.statusScreen);
    }

//    public void showUpdate() {
//        showUpdate("");
//    }

    public void showUpdate(String text) {
        updateScreen.setString(text);
        display.setCurrent(updateScreen);
    }

//    public void showInsert() {
//        display.setCurrent(insertScreen);
//    }

//    public void insertUpdate(String text) {
//        updateScreen.insert(text);
//        display.setCurrent(updateScreen);
//    }

    public void showAbout() {
        Vector texts = new Vector();
        String str = "This is an enhanced version to jibjib-1.0.17.\n\n" +
                 "Acknowledgements:\njibjib is created by @sugree based on " +
                "LoliTwitter by @pruet " +
                "and Twim by @tlaukkanen.\n";
        texts.addElement(str);
       
        texts.addElement("\nPlatform: " + System.getProperty("microedition.platform"));
        texts.addElement("\nCLDC: " + System.getProperty("microedition.configuration"));
        texts.addElement("\nMIDP: " + System.getProperty("microedition.profiles"));
        texts.addElement("\nLocale: " + System.getProperty("microedition.locale"));
        texts.addElement("\nEncoding: " + System.getProperty("microedition.encoding"));
//        text += "MMAPI: " + System.getProperty("microedition.media.version") + "\n";
//        text += "Video Capture: " + System.getProperty("supports.video.capture") + "\n";
//        text += "Snapshot Encodings: " + System.getProperty("video.snapshot.encodings") + "\n";
        texts.addElement("\nLAPI: " + System.getProperty("microedition.location.version"));
        showAlert("About", texts);
    }

    public void showAlert(String title, Vector texts) {
        AlertScreen about = new AlertScreen(this, title, texts, timelineScreen);
        display.setCurrent(about);
    }

    public void showStat(String text) {
        int lenChars = text.length();
        int lenBytes = lenChars;
        try {
            lenBytes = new String(text.getBytes("UTF-8"), "ISO-8859-1").length();
        } catch (UnsupportedEncodingException e) {
        }
        Alert stat = new Alert("Statistics", lenChars + " characters\n" + lenBytes + " bytes", null, AlertType.INFO);
        stat.setTimeout(Alert.FOREVER);
        display.setCurrent(stat);
    }



    public void showError(Exception e, int nextDisplay) {
        Displayable screen = getScreen(nextDisplay);
        String text = e.getMessage();
        Log.error(text);
        Vector texts = new Vector();
        texts.addElement(text);
        AlertScreen alert = new AlertScreen(this, "Exception", texts, screen);
        display.setCurrent(alert);
    }

    private Displayable getScreen(int id) {
        Displayable screen = display.getCurrent();
        switch (id) {
            case SCREEN_CURRENT:
                break;
            case SCREEN_TIMELINE:
                screen = timelineScreen;
                break;
            case SCREEN_UPDATE:
                screen = updateScreen;
                break;
            case SCREEN_STATUS:
                screen = statusScreen;
                break;
        }
        return screen;
    }
}
