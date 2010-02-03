/*
 * Log.java
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

package com.substanceofcode.utils;

import java.util.Vector;

import com.sugree.utils.Loggable;

/**
 * Log
 * 
 * @author Tommi Laukkanen (tlaukkanen at gmail dot com)
 */
public class Log {
    
	private static int MAX_ENTRIES = 50;
	private static Loggable console = null;
	private static Vector log = new Vector();

	public static void setConsole(Loggable console) {
		Log.console = console;
	}

	public static void setState(String text) {
		console.setState(text);
	}

	public static void setProgress(int value) {
		console.setProgress(value);
	}

	public static String getText() {
		String text = "";
		for(int i=0; i<log.size(); i++) {
			text += (String)log.elementAt(i)+"\n";
		}
		return text;
	}
    
	public static void clear() {
		log.removeAllElements();
	}

    protected static void add(String entry) {
        System.out.println(entry);
		log.addElement(entry);
		if (log.size() > MAX_ENTRIES) {
			log.removeElementAt(0);
		}
    }

	public static void info(String entry) {
        add("INFO: "+entry);
		if (console != null) {
			console.println(entry);
		}
	}
    
    public static void verbose(String entry) {
        add("LOG: "+entry);
    }
    
    public static void debug(String entry) {
        add("DBG: "+entry);
    }
    
    public static void error(String entry) {
        add("ERR: "+entry);
    }
    
}
