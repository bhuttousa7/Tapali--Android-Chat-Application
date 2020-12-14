package com.dajjas.tapali;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionHandler {
    private SessionHandler() {
    }

    private static SessionHandler session;

    public static SessionHandler getInstance() {
        if (session == null) {
            session = new SessionHandler();
        }

        return session;
    }

    public static Boolean LoginUser(String username, String password) {

        String link="http://tapali.dajjas.com/login.php";
        String data  = null;
        try {
            data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            if (sb.toString().equals("")) return false;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Don't store login information unless requested
        if (!getStoreLoginInformation()) {
            username = null;
            password = null;
        }

        setUsername(username);
        setPassword(password);
        if (getDisplayName() == null) setDisplayName(username);
        return true;
    }

    public static Boolean RegisterUser(String username, String password) {

        String link="http://tapali.dajjas.com/register.php";
        String data  = null;
        try {
            data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            if (sb.toString().equals("")) return false;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return true;
    }

    public static User getUser() {

        String link="http://tapali.dajjas.com/user.php";
        String data  = null;
        try {
            data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(getUsername(), "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            JSONObject obj = new JSONObject(sb.toString());
            User user = new User();
            user.setUserId(obj.getInt("id"));
            user.setUsername(obj.getString("username"));
            user.setDisplayName(obj.getString("display_name"));

            return user;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static Chat getChat(int chatId) {

        String link="http://tapali.dajjas.com/chat.php";
        String data  = null;
        try {
            data = URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatId, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            JSONObject obj = jsonArray.getJSONObject(0);

            Chat chat = new Chat();
            List<Integer> listINT = new ArrayList<>();
            chat.setId(obj.getInt("id"));
            chat.setChatTitle(obj.getString("title"));
            List<String> listString = Arrays.asList(obj.getString("users").split("\\s*,\\s*"));
            for (String s : listString) listINT.add(Integer.parseInt(s));

            return chat;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static List<Chat> getChats() {
        String link = "http://tapali.dajjas.com/chats.php";
        String data  = null;
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + getUser().getUserId(), "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            List<Chat> chats = new ArrayList<>();
            List<Integer> listINT = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Chat chat = new Chat();
                chat.setId(obj.getInt("id"));
                chat.setChatTitle(obj.getString("title"));
                List<String> listString = Arrays.asList(obj.getString("users").split("\\s*,\\s*"));
                for (String s : listString) listINT.add(Integer.parseInt(s));
                chat.setUsers(listINT);
                chats.add(chat);
            }
            return chats;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static Message sendMessage(int chatId, String message) {
        if (message.equals("")) return null;

        String link = "http://tapali.dajjas.com/message.php";
        String data  = null;
        try {
            data = URLEncoder.encode("chat_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatId, "UTF-8");
            data += "&" + URLEncoder.encode("message", "UTF-8") + "=" +
                    URLEncoder.encode(message, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + getUser().getUserId(), "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            //Log.e("Tapali", sb.toString());
            JSONObject obj = new JSONObject(sb.toString());
            Message msg = new Message();
            msg.setChatId(obj.getInt("chat_id"));
            msg.setMessage(obj.getString("message"));
            msg.setMessageId(obj.getInt("message_id"));
            msg.setTimestamp(obj.getString("message_timestamp"));
            msg.setUserId(obj.getInt("user_id"));
            return msg;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static List<Message> getChatMessages(int chatId, int lastId) {
        String link="http://tapali.dajjas.com/messages.php";
        String data  = null;
        try {
            data = URLEncoder.encode("chat_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatId, "UTF-8");
            data += "&" + URLEncoder.encode("last_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + lastId, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.e("Tapali Messages", lastId + " => " + sb.toString());
            JSONArray jsonArray = new JSONArray(sb.toString());
            List<Message> msgs = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Log.e("Tapali", lastId + " => " + obj.getInt("message_id"));
                //if (obj.getInt("message_id") > lastId) continue;
                Message msg = new Message();
                msg.setChatId(obj.getInt("chat_id"));
                msg.setMessage(obj.getString("message"));
                msg.setMessageId(obj.getInt("message_id"));
                msg.setTimestamp(obj.getString("message_timestamp"));
                msg.setUserId(obj.getInt("user_id"));
                msgs.add(msg);
            }
            return msgs;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static User getProfileById(int userId) {

        String link="http://tapali.dajjas.com/user.php";
        String data  = null;
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                    URLEncoder.encode("" + userId, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            if (sb.toString() != null) {
                JSONObject obj = new JSONObject(sb.toString());
                User user = new User();
                user.setUserId(obj.getInt("id"));
                user.setUsername(obj.getString("username"));
                user.setDisplayName(obj.getString("display_name"));

                return user;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static int inviteUserToChat(int chatId, String username) {
        int res = 0;

        String link="http://tapali.dajjas.com/invite.php";
        String data  = null;
        try {
            data = URLEncoder.encode("chat", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatId, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            res = Integer.parseInt(sb.toString());
            Log.e("Tapali", "INVITE => " + res);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return res;
    }

    public static Boolean updateChat(int chatId, String chatTitle) {
        String link="http://tapali.dajjas.com/update_chat.php";
        String data  = null;
        try {
            data = URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatId, "UTF-8");
            data += "&" + URLEncoder.encode("title", "UTF-8") + "=" +
                    URLEncoder.encode(chatTitle, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            if (!sb.toString().equals("")) return true;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public static Boolean updateUser(String displayName, String password) {
        String link="http://tapali.dajjas.com/update_user.php";
        String data  = null;
        try {
            data = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(getUsername(), "UTF-8");
            data += "&" + URLEncoder.encode("display_name", "UTF-8") + "=" +
                    URLEncoder.encode(displayName, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(((password == null) ? getPassword() : password), "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            if (sb.toString().equals("1")) return true;

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public static int createChatGroup(String chatTitle) {
        String link="http://tapali.dajjas.com/group.php";
        String data  = null;
        try {
            data = URLEncoder.encode("title", "UTF-8") + "=" +
                    URLEncoder.encode("" + chatTitle, "UTF-8");
            data += "&" + URLEncoder.encode("users", "UTF-8") + "=" +
                    URLEncoder.encode("" + getUser().getUserId(), "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return Integer.parseInt(sb.toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return -1;
    }


    public static Boolean getStoreLoginInformation() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getBoolean("store_login_information", false);
    }

    public static void setStoreLoginInformation(Boolean val) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putBoolean("store_login_information", val);
        prefs.commit();

        if(val == false) {
            // Remove login information
            setUsername(null);
            setPassword(null);
        }
    }

    public static Boolean getAutomaticLogin() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getBoolean("auto_login", false);
    }

    public static void setAutomaticLogin(Boolean val) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putBoolean("auto_login", val);
        prefs.commit();
    }

    public static String getUsername() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString("username", "");
    }

    public static void setUsername(String val) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putString("username", val);
        prefs.commit();
    }

    public static String getDisplayName() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString("display_name", "");
    }

    public static void setDisplayName(String val) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putString("display_name", val);
        prefs.commit();
    }

    public static String getPassword() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString("password", "");
    }

    public static void setPassword(String val) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putString("password", val);
        prefs.commit();
    }

    private SharedPreferences sharedPreferences;
    public static SharedPreferences getSharedPreferences() {
        return getInstance().sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences val) {
        getInstance().sharedPreferences = val;
    }
}
