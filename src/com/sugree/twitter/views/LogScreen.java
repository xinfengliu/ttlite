package com.sugree.twitter.views;


import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.substanceofcode.utils.Log;
import com.sugree.twitter.TwitterController;

public class LogScreen extends Form implements CommandListener {
	private TwitterController controller;

	private StringItem textField;
	private Command backCommand;
	private Command clearCommand;

	public LogScreen(TwitterController controller) {
		super("Log");
		this.controller = controller;

		textField = new StringItem("", Log.getText());
		append(textField);

		backCommand = new Command("Back", Command.OK, 1);
		addCommand(backCommand);
		clearCommand = new Command("Clear", Command.CANCEL, 2);
		addCommand(clearCommand);

		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable display) {
		if (cmd == backCommand) {
			controller.showTimeline();
		} else if (cmd == clearCommand) {
			Log.clear();
			controller.showTimeline();
		}
	}
}
