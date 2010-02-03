// modified by XinfengLiu in Dec 2009
package com.sugree.twitter.tasks;


import com.substanceofcode.tasks.AbstractTask;
import com.substanceofcode.twitter.TwitterApi;
import com.substanceofcode.twitter.model.Status;
import com.sugree.twitter.TwitterController;
import com.sugree.twitter.TwitterException;

public class UpdateStatusTask extends AbstractTask {
	private TwitterController controller;
	private TwitterApi api;
	private String statusStr;

	public UpdateStatusTask(TwitterController controller, TwitterApi api, String status) {
		this.controller = controller;
		this.api = api;
		this.statusStr = status;
	}

	public void doTask() {
		try {
			Status status = api.updateStatus(statusStr);
			//controller.showTimeline();
            controller.showStatus(status);
		} catch (TwitterException e) {
			controller.showError(e, TwitterController.SCREEN_UPDATE);
		}
	}
}
