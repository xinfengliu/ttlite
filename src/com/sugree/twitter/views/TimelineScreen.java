// modified by XinfengLiu in Dec 2009

package com.sugree.twitter.views;

import java.util.Vector;
import java.util.Enumeration;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.substanceofcode.twitter.model.Status;

import com.substanceofcode.utils.Log;

import com.sugree.twitter.tasks.RequestTimelineTask;
import com.sugree.twitter.TwitterController;

import javax.microedition.lcdui.Font;

public class TimelineScreen extends List implements CommandListener {

    private TwitterController controller;
    //private Command replyCommand;
    private Command updateCommand;
    private Command friendsTimelineCommand;
    //private Command publicTimelineCommand;
    private Command clearCacheTimelineCommand;
    private Command userTimelineCommand;
    private Command repliesTimelineCommand;
    private Command directTimelineCommand;
    private Command favoritesTimelineCommand;
    private Command setupCommand;
    private Command logCommand;
    private Command aboutCommand;
    private Command minimizeCommand;
    private Command exitCommand;

    private Command moreTimelineCommand;
    private Command backCommand;

    private Vector statuses;
    private long selectedStatus;
    private int length;



    public TimelineScreen(TwitterController controller) {
        super("", Choice.IMPLICIT);
        this.controller = controller;

        selectedStatus = 0;
        statuses = new Vector();

        updateCommand = new Command("Write new tweet", Command.SCREEN, 2);
        addCommand(updateCommand);
//        replyCommand = new Command("Reply", Command.ITEM, 3);
//        addCommand(replyCommand);

        friendsTimelineCommand = new Command("Home Timeline", Command.SCREEN, 1);
        addCommand(friendsTimelineCommand);

        moreTimelineCommand = new Command ("More", Command.SCREEN, 3);
        addCommand(moreTimelineCommand);

        clearCacheTimelineCommand = new Command("Clear Cache", Command.SCREEN, 10);
        addCommand(clearCacheTimelineCommand);

        backCommand = new Command ("Back", Command.BACK, 4);
        addCommand(backCommand);

        repliesTimelineCommand = new Command("Replies Timeline", Command.SCREEN, 6);
        addCommand(repliesTimelineCommand);
        directTimelineCommand = new Command("Direct Messages", Command.SCREEN, 7);
        addCommand(directTimelineCommand);
        favoritesTimelineCommand = new Command("Favorite Tweets", Command.SCREEN, 8);
        addCommand(favoritesTimelineCommand);
        userTimelineCommand = new Command("User Timeline", Command.SCREEN, 9);
        addCommand(userTimelineCommand);
        //publicTimelineCommand = new Command("Public Timeline", Command.SCREEN, 10);
        //addCommand(publicTimelineCommand);

        setupCommand = new Command("Settings", Command.SCREEN, 16);
        addCommand(setupCommand);
        logCommand = new Command("Log", Command.SCREEN, 17);
        addCommand(logCommand);
        aboutCommand = new Command("About", Command.SCREEN, 18);
        addCommand(aboutCommand);
        minimizeCommand = new Command("Minimize", Command.SCREEN, 19);
        addCommand(minimizeCommand);
        exitCommand = new Command("Exit", Command.SCREEN, 20);
        addCommand(exitCommand);

        setCommandListener(this);
    }



//    private void saveSelected() {
//        int index = getSelectedIndex();
//
//        if (index >= 0 && index < statuses.size()) {
//            selectedStatus = ((Status) statuses.elementAt(index)).getId();
//        }
//    }
//
//    private void restoreSelected() {
//        int lastIndex = findStatus(selectedStatus);
//        if (lastIndex < 0) {
//            lastIndex = statuses.size() - 1;
//        }
//        if (lastIndex >= 0 && lastIndex < statuses.size()) {
//            setSelectedIndex(lastIndex, true);
//        }
//    }

//    public void setLength(int length) {
//        this.length = length;
//    }

//    public void clearTimeline() {
//        //statuses.removeAllElements();
//        //removeAll();
//        deleteAll();
//    }

//    public void addTimeline(Vector timeline) {
//        /*
//        saveSelected();
//        for (int i = 0; i < timeline.size(); i++) {
//            if (findStatus(((Status) timeline.elementAt(i)).getId()) < 0) {
//                if (i < statuses.size()) {
//                    statuses.insertElementAt(timeline.elementAt(i), i);
//                } else {
//                    statuses.addElement(timeline.elementAt(i));
//                }
//            }
//        }
//        if (statuses.size() > length) {
//            statuses.setSize(length);
//            statuses.trimToSize();
//        }
//        updateTimeline();
//        restoreSelected();
//        */
//        statuses = timeline;
//        updateTimeline();
//    }

//    public void update(Status status) {
//        int index = findStatus(status.getId());
//        if (index >= 0) {
//            statuses.setElementAt(status, index);
//            saveSelected();
//            updateTimeline();
//            restoreSelected();
//        }
//    }

//    public String getLastDate() {
//        if (statuses != null && statuses.size() > 0) {
//            Status status = (Status) statuses.elementAt(0);
//            return DateUtil.formatHTTPDate(status.getDate());
//        }
//        return "";
//    }
//
//    public String getLastId() {
//        if (statuses != null && statuses.size() > 0) {
//            Status status = (Status) statuses.elementAt(0);
//            return String.valueOf(status.getId());
//        }
//        return "";
//    }

    public Vector getTimeline() {
        return statuses;
    }

//    private int findStatus(long status) {
//        for (int i = 0; i < statuses.size(); i++) {
//            if (((Status) statuses.elementAt(i)).getId() == status) {
//                return i;
//            }
//        }
//        return -1;
//    }

//	public void removeAll() {
//		while (size() > 0) {
//			delete(0);
//		}
//	}
    public void updateTimeline(Vector statuses) {
        //removeAll();
        deleteAll();
        //clearTimeline();
        this.statuses = statuses;
        int idx = 0;
        Enumeration statusEnum = statuses.elements();
        //Log.debug("statuses:\n"+statuses);
        while (statusEnum.hasMoreElements()) {
            Status status = (Status) statusEnum.nextElement();
            idx = append(status.getScreenName() + ": " + status.getText() + "\n", null);

            //buggy on Sun Wireless Toolkit 2.5.2, O.K. on real phone.
            setFont(idx, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                Font.SIZE_SMALL));
        }


    }


    public void commandAction(Command cmd, Displayable display) {
        int index = getSelectedIndex();

        int currentFeedType = controller.getCurrentFeedType();
        int currentPageNo = controller.getCurrentPageNo();

        if (cmd == List.SELECT_COMMAND && index >= 0) {
            Status status = (Status) statuses.elementAt(index);
            controller.showStatus(status);
        }
//        if (cmd == replyCommand && index >= 0) {
//            Status status = (Status) statuses.elementAt(index);
//            controller.showUpdate("@" + status.getScreenName() + " ");
//        }
        else if (cmd == updateCommand) {
            controller.showUpdate("");
        } else if (cmd == friendsTimelineCommand) {
            controller.fetchTimeline(RequestTimelineTask.FEED_FRIENDS, 1);
        } else if (cmd == clearCacheTimelineCommand) {            
            controller.clearCache();
            statuses.removeAllElements();
            deleteAll();
            controller.showTimeline();
        } else if (cmd == userTimelineCommand) {
            controller.fetchTimeline(RequestTimelineTask.FEED_USER, 1);
        } else if (cmd == repliesTimelineCommand) {
            controller.fetchTimeline(RequestTimelineTask.FEED_REPLIES, 1);
        } else if (cmd == directTimelineCommand) {
            controller.fetchTimeline(RequestTimelineTask.FEED_DIRECT, 1);
        } else if (cmd == favoritesTimelineCommand) {
            controller.fetchTimeline(RequestTimelineTask.FEED_FAVORITES, 1);
        } else if (cmd == setupCommand) {
            controller.showSetup();
        } else if (cmd == logCommand) {
            controller.showLog();
        } else if (cmd == aboutCommand) {
            controller.showAbout();
        } else if (cmd == minimizeCommand) {
            controller.minimize();
        } else if (cmd == exitCommand) {
            controller.exit();
        } else if (cmd == backCommand){
            if (currentPageNo > 1)
                controller.fetchTimeline(currentFeedType, currentPageNo - 1);
        } else if (cmd == moreTimelineCommand){
            controller.fetchTimeline(currentFeedType, currentPageNo + 1);
        }
    }
}
