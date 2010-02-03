package com.sugree.twitter.views;


import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.utils.Log;
import com.sugree.twitter.TwitterController;
import com.sugree.utils.Loggable;

public class WaitScreen extends Form implements CommandListener, Runnable, Loggable {
	private TwitterController controller;
	private AbstractTask task;
	private int cancelScreen;

	private StringItem stateField;
	private Gauge progressField;
	private StringItem logField;
	private Command cancelCommand;

	private Thread thread;

	public WaitScreen(TwitterController controller, AbstractTask task, int cancelScreen) {
		super("Wait");
		this.controller = controller;
		this.task = task;
		this.cancelScreen = cancelScreen;

		thread = new Thread(this);

		stateField = new StringItem("", "waiting");
		append(stateField);
		progressField = new Gauge("Progress", false, 100, 0);
		append(progressField);
		logField = new StringItem("", "");
		append(logField);

		cancelCommand = new Command("Cancel", Command.STOP, 1);
		addCommand(cancelCommand);

		setCommandListener(this);
	}

	public void setState(String text) {
		stateField.setText(text);
	}

	public void setProgress(int value) {
		progressField.setValue(value);
	}

	public void clear() {
		setText("");
	}

	public void print(String text) {
		setText(logField.getText()+text);
	}

	public void println(String text) {
		setText(logField.getText()+text+"\n");
	}

	public void setText(String text) {
		logField.setText(text);
	}

	public void commandAction(Command cmd, Displayable display) {
		if (cmd == cancelCommand) {
			controller.setCurrent(cancelScreen);
		}
	}

	public void start() {
		thread.start();
	}

	public void run() {
		Log.setConsole(this);
		try {
			task.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.setConsole(null);
	}
}
