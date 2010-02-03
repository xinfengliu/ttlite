/*
 * StatusEntry.java
 *
 * Copyright (C) 2005-2008 Tommi Laukkanen
 * http://www.substanceofcode.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.substanceofcode.twitter.model;

import java.util.Date;

/**
 * StatusEntry
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class Status {

	private long id;
    private String screenName;
    private String statusText;
    private Date date;
	private String source;
	private boolean favorited;
    
    /** Creates a new instance of StatusEntry 
     * @param screenName 
     * @param statusText 
     * @param date 
     */
    public Status(long id, String screenName, String statusText, Date date, String source, boolean favorited) {
		this.id = id;
        this.screenName = screenName;
        this.statusText = statusText;
        this.date = date;
        this.source = source;
        this.favorited = favorited;
    }


	public long getId() {
		return id;
	}
    
    public String getText() {
        return statusText;
    }
    
    public String getScreenName() {
        return screenName;
    }
    
    public Date getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

	public boolean getFavorited() {
		return favorited;
	}

}
