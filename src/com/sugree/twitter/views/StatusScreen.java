// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.views;

import java.util.Date;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.substanceofcode.utils.TimeUtil;
import com.substanceofcode.twitter.model.Status;

import com.sugree.twitter.TwitterController;
import javax.microedition.lcdui.Font;


public class StatusScreen extends Form implements CommandListener {

    private TwitterController controller;
    private Status status;
    private int timeOffset;
    private StringItem textField;
    // have to use 2 StringItems, otherwise the tailing text might be truncated 
    // abnormally. (lxf)
    private StringItem footField;
    private Command replyCommand;
    private Command favoriteCommand;
    private Command backCommand;

    public StatusScreen(TwitterController controller) {
        super("");
        this.controller = controller;

        textField = new StringItem(null, "");
        footField = new StringItem(null, "");
        //buggy with setFont on Sun Wireless Toolkit 2.5.2, O.K. on real phone.
        //textField.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
               // Font.SIZE_SMALL));
        //footField.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                //Font.SIZE_SMALL));
        append(textField);
        append(footField);

        backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        replyCommand = new Command("Reply", Command.SCREEN, 2);
        addCommand(replyCommand);
        favoriteCommand = new Command("Toggle Favorite", Command.SCREEN, 3);
        addCommand(favoriteCommand);

        setCommandListener(this);
    }

//	public void setTimeOffset(String offset) {
//		try {
//			int i = Integer.parseInt(offset);
//			timeOffset = (i/100*3600+i%100*60)*1000;
//		} catch (NumberFormatException e) {
//			Log.error(e.toString());
//		}
//	}
    public void setStatus(Status status) {
        String favorited = "";
        Date now = new Date();
        Date date = new Date(status.getDate().getTime() + timeOffset);
        String interval = "";
        if (now.getTime() > date.getTime()) {
            interval = TimeUtil.getTimeInterval(date, now) + " ago";
        } else {
            interval = TimeUtil.getTimeInterval(now, date) + " in the future";
        }

        this.status = status;
        if (status.getFavorited()) {
            favorited = " *";
        }
        setTitle(status.getScreenName());

        //textField.setLayout(Item.LAYOUT_VEXPAND);
        //textField.setLayout(Item.LAYOUT_2);
        //textField.setPreferredSize(-1, -1);
        textField.setText(status.getText());

        String str = "\nabout " + interval + " via "
                + status.getSource()  + favorited;
        footField.setText(str);
    }

    public void commandAction(Command cmd, Displayable display) {
        if (cmd == replyCommand) {
            controller.showUpdate("@" + getTitle() + " ");
        } else if (cmd == backCommand) {
            status = null;
            controller.showTimeline();
        } else if (cmd == favoriteCommand) {
            controller.toggleFavorited(status);
        }
    }
}
