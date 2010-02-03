// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.views;

import java.io.IOException;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;


import com.substanceofcode.twitter.Settings;
import com.substanceofcode.twitter.TwitterApi;
import com.substanceofcode.utils.Log;
import com.sugree.twitter.TwitterController;


public class SetupScreen extends Form implements CommandListener, ItemStateListener {
	private final String[] gatewaysLabel = {
		"Custom",
		"Twitter",
//		"Birdnest appspot",  //because nest.appspot.com is blocked by Google
		"Birdnest onedd",
	};
	private final String[] gatewaysValue = {
		null,
		"http://twitter.com/",
//		"http://nest.appspot.com/text/",
		"http://nest.onedd.net/text/",
	};

//	private final String[] startsLabel = {
//		"Empty Timeline",
//		"Tweet",
//		"Friends Timeline",
//		"Replies Timeline",
//	};
//	private final int[] startsValue = {
//		TwitterController.START_EMPTY_TIMELINE,
//		TwitterController.START_UPDATE,
//		TwitterController.START_FRIENDS_TIMELINE,
//		TwitterController.START_REPLIES_TIMELINE,
//	};
//
//	private final String[] flagsLabel = {
//		"Alternate authentication",
//	};

	private TwitterController controller;
	private TwitterApi api;
	private TimelineScreen timeline;
	private StatusScreen status;

	private TextField usernameField;
	private TextField passwordField;
	private TextField timelineLengthField;
	private TextField gatewayField;
	private ChoiceGroup gatewaysField;
	private ChoiceGroup startsField;
	private TextField timeOffsetField;
	private ChoiceGroup flagsField;
	private Command saveCommand;
	private Command cancelCommand;

	public SetupScreen(TwitterController controller, TwitterApi api, TimelineScreen timeline, StatusScreen status) {
		super("Setup");
		this.controller = controller;
		this.api = api;
		this.timeline = timeline;
		this.status = status;

		Settings settings = controller.getSettings();

		String username = settings.getStringProperty(Settings.USERNAME, "");
		usernameField = new TextField("Username", username, 32, TextField.ANY);
		append(usernameField);

		String password = settings.getStringProperty(Settings.PASSWORD, "");
		passwordField = new TextField("Password", password, 32, TextField.PASSWORD);
		append(passwordField);

//		int timelineLength = settings.getIntProperty(Settings.TIMELINE_LENGTH, 20);
//		timelineLengthField = new TextField("Timeline Length", String.valueOf(timelineLength), 32, TextField.NUMERIC);
//		append(timelineLengthField);

		String gateway = settings.getStringProperty(Settings.GATEWAY, "http://twitter.com/");
		gatewayField = new TextField("Gateway", gateway, 128, TextField.URL);
		append(gatewayField);

		gatewaysField = new ChoiceGroup("Preset Gateways", Choice.EXCLUSIVE, gatewaysLabel, null);
		append(gatewaysField);

//		int startScreen = settings.getIntProperty(Settings.START_SCREEN, 0);
//		startsField = new ChoiceGroup("Start Screen", Choice.EXCLUSIVE, startsLabel, null);
//		startsField.setSelectedIndex(startScreen, true);
//		append(startsField);

//		String timeOffset = settings.getStringProperty(Settings.TIME_OFFSET, "0000");
//		timeOffsetField = new TextField("Time Offset", timeOffset, 5, TextField.NUMERIC);
//		append(timeOffsetField);
//
//		boolean[] flags = {
//			settings.getBooleanProperty(Settings.ALTERNATE_AUTHEN, false),
//		};
//		flagsField = new ChoiceGroup("Flags", Choice.MULTIPLE, flagsLabel, null);
//		flagsField.setSelectedFlags(flags);
//		append(flagsField);

		saveCommand = new Command("Save", Command.OK, 1);
		addCommand(saveCommand);
		cancelCommand = new Command("Cancel", Command.CANCEL, 2);
		addCommand(cancelCommand);

		setCommandListener(this);
		setItemStateListener(this);
	}

	public void itemStateChanged(Item item) {
		if (item == gatewaysField) {
			String url = gatewaysValue[gatewaysField.getSelectedIndex()];
			if (url != null) {
				gatewayField.setString(url);
			}
		}
	}

	public void commandAction(Command cmd, Displayable display) {
		if (cmd == saveCommand) {
			String username = usernameField.getString();
			String password = passwordField.getString();
			String gateway = gatewayField.getString();
			//int timelineLength = Integer.parseInt(timelineLengthField.getString());
			//int startScreen = startsField.getSelectedIndex();
			//String timeOffset = timeOffsetField.getString();
			//boolean[] flags = new boolean[1];
			//flagsField.getSelectedFlags(flags);

			if (!gateway.endsWith("/")) {
				gateway += "/";
			}

			Settings settings = controller.getSettings();

			settings.setStringProperty(Settings.USERNAME, username);
			settings.setStringProperty(Settings.PASSWORD, password);
			settings.setStringProperty(Settings.GATEWAY, gateway);
			//settings.setIntProperty(Settings.TIMELINE_LENGTH, 20);
			//settings.setIntProperty(Settings.START_SCREEN, TwitterController.START_EMPTY_TIMELINE);
			//settings.setStringProperty(Settings.TIME_OFFSET, "0000");
			//settings.setBooleanProperty(Settings.ALTERNATE_AUTHEN, false);

			api.setGateway(gateway);
			api.setUsername(username);
			api.setPassword(password);
//			api.setAlternateAuthentication(flags[0]);
//			timeline.setLength(timelineLength);
//			status.setTimeOffset(timeOffset);

			try {
				settings.save(true);
			} catch (IOException e) {
				Log.error(e.toString());
			} catch (RecordStoreException e) {
				Log.error(e.toString());
			}
			controller.showTimeline();
		} else if (cmd == cancelCommand) {
			controller.showTimeline();
		}
	}
}
