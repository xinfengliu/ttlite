// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.views;


import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;


import com.sugree.twitter.TwitterController;

public class UpdateStatusScreen extends TextBox implements CommandListener {
	private TwitterController controller;

	private Command sendCommand;
	private Command cancelCommand;
//	private Command insertCommand;
	private Command statCommand;

	public UpdateStatusScreen(TwitterController controller, String text) {
		super("What are you doing?", text, 140, TextField.ANY);
		this.controller = controller;

		sendCommand = new Command("Send", Command.OK, 1);
		addCommand(sendCommand);
		cancelCommand = new Command("Cancel", Command.CANCEL, 2);
		addCommand(cancelCommand);
//		insertCommand = new Command("Insert", Command.SCREEN, 3);
//		addCommand(insertCommand);
		statCommand = new Command("Word counts", Command.SCREEN, 4);
		addCommand(statCommand);

		setCommandListener(this);
	}

//	public void insert(String text) {
//		insert(text, getCaretPosition());
//	}

	public void commandAction(Command cmd, Displayable display) {
		if (cmd == sendCommand) {
			controller.updateStatus(getString());
		} else if (cmd == cancelCommand) {
			controller.showTimeline();
                }
//		 else if (cmd == insertCommand) {
//			controller.showInsert();
		 else if (cmd == statCommand) {
			controller.showStat(getString());
		}
	}
}
