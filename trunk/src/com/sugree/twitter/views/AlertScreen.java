// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.views;


import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;


import com.substanceofcode.utils.Log;

import com.sugree.twitter.TwitterController;
import java.util.Enumeration;
import java.util.Vector;

public class AlertScreen extends Form implements CommandListener {
	private TwitterController controller;
	private Displayable nextDisplay;

	private StringItem textField;
	private Command backCommand;
	private Command testCommand;
	private Command scheduleDowntimeCommand;

	public AlertScreen(TwitterController controller, String title, Vector texts, Displayable nextDisplay) {
		super(title);
		this.controller = controller;
		this.nextDisplay = nextDisplay;

//		if (title.equalsIgnoreCase("exception")) {
//			text += "\n\n"+Log.getText();
//		}
		for (Enumeration e = texts.elements() ; e.hasMoreElements() ;) {
            append((String)e.nextElement());
        }

        //textField = new StringItem("", text);
		//append(textField);
//        Log.debug("Log width: " + textField.getPreferredWidth());
//        Log.debug("Log height: " + textField.getPreferredHeight());
		backCommand = new Command("OK", Command.OK, 1);
		addCommand(backCommand);
//		testCommand = new Command("Test", Command.SCREEN, 2);
//		addCommand(testCommand);
//		scheduleDowntimeCommand = new Command("Schedule Downtime", Command.SCREEN, 3);
//		addCommand(scheduleDowntimeCommand);

		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable display) {
		if (cmd == backCommand) {
			controller.setCurrent(nextDisplay);
        }
//		 else if (cmd == testCommand) {
//			controller.fetchTest();
//		} else if (cmd == scheduleDowntimeCommand) {
//			controller.fetchScheduleDowntime();
//		}
	}
}
