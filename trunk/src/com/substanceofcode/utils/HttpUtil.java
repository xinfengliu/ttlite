/*
 * HttpUtil.java
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
// modified by XinfengLiu in Dec 2009
package com.substanceofcode.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.sugree.twitter.TwitterMIDlet;
import com.sugree.infrastructure.Device;
import javax.microedition.io.HttpsConnection;

import com.java4ever.apime.io.GZIP;

/**
 *
 * @author Tommi Laukkanen
 */
public class HttpUtil extends HttpAbstractUtil {

    /** Total bytes transfered */
    private static long totalBytes = 0;
    //private static String userAgent = "curl/7.18.0 (i486-pc-linux-gnu) libcurl/7.18.0 OpenSSL/0.9.8g zlib/1.2.3.3 libidn/1.1";
    private static String userAgent = "Profile/" +
            System.getProperty("microedition.profiles") +
            " Configuration/" +
            System.getProperty("microedition.configuration") +
            " gzip";
    private static boolean alternateAuthen = false;

    /** Creates a new instance of HttpUtil */
    public HttpUtil() {
    }

    public static void setUserAgent(String userAgent) {
        HttpUtil.userAgent = userAgent;
    }

    public static void setAlternateAuthentication(boolean flag) {
        HttpUtil.alternateAuthen = flag;
    }

    public static String doPost(String url, String postData) throws IOException, Exception {
        //return doRequest(url, prepareQuery(query), HttpConnection.POST);
        return doRequest(url, postData, HttpConnection.POST);
    }

    public static String doGet(String url, String query) throws IOException, Exception {
        String fullUrl = url;
        //query = prepareQuery(query);
        if (query.length() > 0) {
            fullUrl += "?" + query;
        }
        return doRequest(fullUrl, "", HttpConnection.GET);
    }

    public static String doRequest(String url, String postData, String requestMethod) throws IOException, Exception {
        String response = "";
        int status = -1;
        String message = null;
        int depth = 0;
        boolean redirected = false;
        String auth = null;
        InputStream is = null;
        OutputStream os = null;
        HttpConnection con = null;
        final String platform = Device.getPlatform();

        while (con == null) {
            Log.setState("connecting");
            if (url.startsWith("https")) {
                con = (HttpsConnection) Connector.open(url);
            } else {
                con = (HttpConnection) Connector.open(url);
            }
            Log.setState("connected");
            Log.verbose("opened connection to " + url);
            con.setRequestMethod(requestMethod);
            if (!alternateAuthen && username != null && password != null && username.length() > 0) {
                String userPass;
                Base64 b64 = new Base64();
                userPass = username + ":" + password;
                userPass = b64.encode(userPass.getBytes());
                con.setRequestProperty("Authorization", "Basic " + userPass);
            }
            con.setRequestProperty("User-Agent", userAgent);
            con.setRequestProperty("Connection", "close");
            if (!platform.equals(Device.PLATFORM_NOKIA)) {
                con.setRequestProperty("Host", con.getHost() + ":" + con.getPort());
            }
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("X-Twitter-Client", TwitterMIDlet.NAME);
            con.setRequestProperty("X-Twitter-Client-Version", TwitterMIDlet.VERSION);
            //con.setRequestProperty("X-Twitter-Client-URL", TwitterMIDlet.URL);
            con.setRequestProperty("Accept-Encoding", "gzip");

            //if (postData.length() > 0) { bug in create_favorite because content-length is not set
            if (requestMethod.equals(HttpConnection.POST)) {
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Content-Length", "" + postData.length());
                if (postData.length() > 0) {
                    os = con.openOutputStream();
                    Log.verbose("opened output stream");
                    os.write(postData.getBytes());
                    Log.verbose("sent query " + postData);
                    os.close();
                    os = null;
                    Log.verbose("closed output stream");
                }
            }

            Log.setState("sending request");
            status = con.getResponseCode();
            message = con.getResponseMessage();
            Log.setState("received response");
            Log.info(status + " " + message);
            Log.debug("user-agent " + con.getRequestProperty("User-Agent"));
            Log.verbose("response code " + status + " " + message);
            switch (status) {
                case HttpConnection.HTTP_OK:
                case HttpConnection.HTTP_NOT_MODIFIED:
                    break;
                case HttpConnection.HTTP_MOVED_TEMP:
                case HttpConnection.HTTP_TEMP_REDIRECT:
                case HttpConnection.HTTP_MOVED_PERM:
                    if (depth > 2) {
                        throw new IOException("Too many redirect");
                    }
                    redirected = true;
                    url = con.getHeaderField("location");
                    Log.verbose("redirected to " + url);
                    con.close();
                    con = null;
                    Log.verbose("closed connection");
                    depth++;
                    break;
                case 100:
                    throw new IOException("unexpected 100 Continue");
                default:
                    con.close();
                    con = null;
                    Log.verbose("closed connection");
                    throw new IOException("Response status not OK:" + status + " " + message);
            }
        }

        is = con.openInputStream();


        Log.setState("receiving data");
        Log.verbose("opened input stream");
        if (!redirected) {
            response = getUpdates(con, is, os);
        } else {
            try {
                if (con != null) {
                    con.close();
                    Log.verbose("closed connection");
                }
                if (os != null) {
                    os.close();
                    Log.verbose("closed output stream");
                }
                if (is != null) {
                    is.close();
                    Log.verbose("closed input stream");
                }
            } catch (IOException ioe) {
                throw ioe;
            }
        }

        return response;
    }

    private static String getUpdates(HttpConnection con, InputStream is, OutputStream os) throws IOException {

        String str;
        int ch = 0;
        try {
            int len = (int) con.getLength();
            Log.info("Size: " + len);
            Log.verbose("reading response");
            if (len != -1) {
                byte[] buffer = new byte[len];
                int actual = 0;
                int bytesread = 0;
                while ((bytesread != len) && (actual != -1)) {
                    actual = is.read(buffer, bytesread, len - bytesread);
                    bytesread += actual;
                    // Log.verbose("actual read:" + actual);
                    if (actual > 1000) {
                        Log.setProgress(bytesread * 100 / len);
                    }
                }
                //str = new String(buffer, 0, bytesread);
                //str = new String(buffer, 0, bytesread, "UTF8");
                if ("gzip".equals(con.getEncoding())) {
                    byte[] decompressedData = GZIP.inflate(buffer);
                    str = new String(decompressedData, "UTF-8");
                } else {
                    str = new String(buffer, 0, bytesread, "UTF-8");
                }

                //Log.verbose("return data:\n" + str);
                /*
                for (int i = 0; i < len; i++) {
                if ((ch = is.read()) != -1) {
                stb.append((char) ch);
                }
                if (i % 100 == 0 || i == len - 1) {
                Log.setProgress(i * 100 / len);
                }
                }*/
            } else { // To Do: decompress, should use bytearraystream?
                StringBuffer stb = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    len = is.available();
                    stb.append((char) ch);
                }
                str = stb.toString();
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            try {
                if (os != null) {
                    os.close();
                    Log.verbose("closed output stream");
                }
                if (is != null) {
                    is.close();
                    Log.verbose("closed input stream");
                }
                if (con != null) {
                    con.close();
                    Log.verbose("closed connection");
                }
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        //Log.debug("return str:\n"+str);
        return str;
    }
//    private static String prepareQuery(String query) {
//        if (alternateAuthen && username != null && password != null && username.length() > 0) {
//            String userPass;
//            Base64 b64 = new Base64();
//            userPass = username + ":" + password;
//            userPass = b64.encode(userPass.getBytes());
//            if (query.length() > 0) {
//                query += "&";
//            }
//            query += "__token__=" + StringUtil.urlEncode(userPass);
//        }
//        return query;
//    }
}
