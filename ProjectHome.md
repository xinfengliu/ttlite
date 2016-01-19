On Feb. 3, 2010, this project was migrated from http://ttlite.kenai.com. Later Oracle decided to keep all projects on Kenai, so I would like to make this site as a mirror. For latest source codes please refer to http://ttlite.kenai.com

Note: Twitter will abandon http basic auth from July 1, 2010. I don't have time/resources to add OAUTH to ttlite. So, ttlite will probably end its life by then.

# About ttlite #

ttlite is an even small,fast and open source Java ME twitter client for mobile phones. It is an enhanced version to [jibjib-1.0.17](http://code.google.com/p/jibjib), which was created by [sugree](http://sugree.com/) ([@sugree](http://twitter.com/sugree)) based on [LoliTwitter](http://code.google.com/p/lolitwitter/) by [@pruet](http://twitter.com/pruet)  and [Twim](http://code.google.com/p/twim/) by [tlaukkanen](http://www.substanceofcode.com/) ([@tlaukkanen](http://twitter.com/tlaukkanen)).

# The enhancements of ttlite include: #

  * support displaying older tweets and caching tweets since application starting-up.
  * support transportation compression which means not only saving money but also speed. Thanks to [java4ever.com](http://www.java4ever.com/index.php?section=j2me&project=gzip&menu=main&lang=_en) providing gzip implementation for j2me.
  * support https.
  * refactored some codes and cleaned-up some un-necessary codes.
  * reorganized the menus.
  * fixed some minor bugs, such as: wrong time interval, some texts in Stringitem was truncated in display, could not make and display favorites correctly...
  * remain small size (around 40KB) with more features.
  * In march 2010, version 0.98 was added the features of sending tweets with GEO info (suppose your phone supports GPS or other location service) and sending SOS message with just one button push.
  * In April 2010, version 0.99 was added the features of Lists, RT, Official RT, Direct Messages, Delete, etc... and fixed some bugs.

# Installation and Usage #

  * The source code uses standard Java ME LCDUI apis and does not use any phone vendor specific APIs. It should install on most of phones that supports Java MIDP 2.0, including lots of mid or low end phone models.
  * Go to [Downloads](http://code.google.com/p/ttlite/downloads/list) page and grab the latest .jar file and transfer it to your phone, then install it on the phone. Some phones may also need the correspondent .jad file with the same name along with the jar file for installation.
  * **For first time use, menu -> setting, input your twitter username and password then click save.**
  * Select "Home timeline" from menu to read your home timeline. To read next page (older tweets), select "More" from the menu. **Note:** All tweets that you have downloaded are cached until the program exits. If you want to refresh the timeline, please menu -> "clear cache", then request timeline again.
  * For sending preset (SOS) messages, menu -> setting, input your SOS text and save. Then press SOS! to send tweet. _Note: Twitter currently does not allow you to send a new tweet which is duplicated with your recent tweets. So if you want to send SOS again in short time, you may need do some editing first._
  * For sending tweets with GEO info, your phone need support Java ME location API (JSR 179), you can check menu -> About, to see if "LAPI"-like words exist. Before sending GEO tweets, please menu -> setting, set "enable GEO" to yes and turn on GPS if the cell phone network does not provide location service.

# Support: #

  * If the software works on your phone, I will appreciate if you can tell your phone model to [@ttlite](http://twitter.com/ttlite) on twitter.
  * If you have problems, please get your phone model and error messages (from ttlite menu -> log), and log them in [issues-tracking](http://code.google.com/p/ttlite/issues/list). But I'm not sure I can solve them, I'm a new guy in J2me development and this is a my project in spare time.
  * This is a free and open source software, use it at your risk.

# How to build #

  * Download and install latest [Netbeans](http://www.netbeans.org)
  * From Netbeans menu -> tools -> plugins, install Java ME related plugins.
  * If you use linux, download and install  [Sun Java Wireless Toolkit for CLDC ](http://java.sun.com/products/sjwtoolkit/download.html).
  * (Optional), you can download and install  Sun Java ME SDK 3.0 [windows\_version](http://java.sun.com/javame/downloads/sdk30.jsp).
  * Check-out the source.
  * In Netbeans, Create a JavaME project, and import the source.
  * (for linux) In Netbeans menu, project properties -> platform -> manage emulators -> add platform, choose the directory where you installed Sun Java Wireless Toolkit for CLDC.
  * build and test!