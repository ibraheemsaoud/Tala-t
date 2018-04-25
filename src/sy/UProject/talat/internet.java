package sy.UProject.talat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.ResponseHandler;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.BasicResponseHandler;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import sy.UProject.UI.DialogAdapter;

public class internet {
    // String link = "http://hangouts.sitecloud.cytanium.com/new";
    // String link = "http://uproject-talat.ddns.net/t";
    // String link = "http://192.168.1.3/t";
    // static String link = "http://192.168.137.55/t";
     static String link = "http://u-talat.asuscomm.com/t";
    String version = "0";

    @SuppressLint("NewApi")
    public String login(String username, String password) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/login.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String register(String username, String password, String email,
                           String phone, String sections, String region) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/register.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("sections", sections));
            nameValuePairs.add(new BasicNameValuePair("region", region));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String updateBasics(String username, String oldPassword, String email, String password) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/update_profile.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("password2", oldPassword));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String getProfileUpdates(String username, String password) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/profile_updates.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String updateAbout(String username, String password, String about) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/update_about.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("about", about));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String updateSections(String username, String password, String sections) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/update_sections.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("data", sections));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String updateRegions(String username, String password, String regions) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/update_regions.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("data", regions));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            Log.v("Internet Connection", "Updating- region - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String search(String username, String password, String query) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/search.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("q", query));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            Log.v("Internet Connection", "Doing - searching - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String stream(String username, String password, String start, String end, String force, String link, String x1, String x2) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(this.link + "/" + link + ".php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("force", force));
            nameValuePairs.add(new BasicNameValuePair("start", start));
            nameValuePairs.add(new BasicNameValuePair("end", end));
            nameValuePairs.add(new BasicNameValuePair("extra1", x1));
            nameValuePairs.add(new BasicNameValuePair("extra2", x2));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            Log.v("Internet Connection", "Doing - streaming - " + response);
            if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String stream(String username, String password, String start, String end, String force) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/stream.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("force", force));
            nameValuePairs.add(new BasicNameValuePair("start", start));
            nameValuePairs.add(new BasicNameValuePair("end", end));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            Log.v("Internet Connection", "Doing - streaming - " + response);
            if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String Upcoming(String username, String password, String start, String end, String force) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/upcoming.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("force", force));
            nameValuePairs.add(new BasicNameValuePair("start", start));
            nameValuePairs.add(new BasicNameValuePair("end", end));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            Log.v("Internet Connection", "Doing - streaming - " + response);
            if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String manager(String username, String password) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/manager.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String createEvent(String username, String password, String headline, String desc, String section, String regions, String group, String cost, String sDate, String eDate, String sTime, String eTime, String seats) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/add_event.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("EH", headline));
            nameValuePairs.add(new BasicNameValuePair("EDe", desc));
            nameValuePairs.add(new BasicNameValuePair("ES", section));
            nameValuePairs.add(new BasicNameValuePair("ER", regions));
            nameValuePairs.add(new BasicNameValuePair("EG", group));
            nameValuePairs.add(new BasicNameValuePair("EC", cost));
            nameValuePairs.add(new BasicNameValuePair("SD", sDate));
            nameValuePairs.add(new BasicNameValuePair("ED", eDate));
            nameValuePairs.add(new BasicNameValuePair("ST", sTime));
            nameValuePairs.add(new BasicNameValuePair("ET", eTime));
            nameValuePairs.add(new BasicNameValuePair("ESe", seats));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);

            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String FoLlow(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/follow.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("userID", id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "Doing - following - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String hang(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/hang.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("eventID", id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "Doing - Hangging - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String updateCover(String username, String password, String id, String cover) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/change_cover.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("eventID", id));
            nameValuePairs.add(new BasicNameValuePair("cover", cover));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "Updating - Cover - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String getHanggers(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/get_hanggers.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("eventID", id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "getting - Hanggers - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String getFollowers(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/get_followers.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("userID", id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "getting - Followers - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String getUpdates(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/get_updates.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            nameValuePairs.add(new BasicNameValuePair("eventID", id));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            Log.v("Internet Connection", "getting - updates - " + response);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                return response;
            } else {
                return "error";
            }
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public String fetch_user(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/fetch_user.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("id", id));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return "error";
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                //id, headline, des, host, EX1, EX2, cost, going, fe, rank, place, s_date, e_date, s_time, e_time, grop
                //0id, 1headline, 2des, 3host, 4EX1, 5EX2, 6cost, 7going, 8fe, 9rank, 10place, 11s_date, 12e_date, 13s_time, 14e_time, 15section, 16grop, 17ishangging
                Log.v("Internet Connection", "Doing - fetching - " + response);
                return response;
            } else return "error";
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return "error";

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return "error";
        }
    }

    @SuppressLint("NewApi")
    public Event fetch_event(String username, String password, String id) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(link + "/fetch.php");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("id", id));
            nameValuePairs.add(new BasicNameValuePair("version", version));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            if (response.startsWith("VERSION<>")) {
                ERROR();
                return null;
            } else if (response.startsWith("Talat<>")) {
                response = response.replaceFirst("Talat\\<\\>", "");
                //id, headline, des, host, EX1, EX2, cost, going, fe, rank, place, s_date, e_date, s_time, e_time, grop
                //0id, 1headline, 2des, 3host, 4EX1, 5EX2, 6cost, 7going, 8fe, 9rank, 10place, 11s_date, 12e_date, 13s_time, 14e_time, 15section, 16grop, 17ishangging
                Log.v("Internet Connection", "Doing - fetching - " + response);
                String[] data = response.split("\\<BR \\/\\>");
                if(data.length < 10)
                    return null;
                Event e = new Event(data[0], data[1], data[2], data[3], Integer.parseInt(data[15]) - 1, Integer.parseInt(data[7]), Long.parseLong(data[11]), Long.parseLong(data[12]), Long.parseLong(data[13]), Long.parseLong(data[14]));
                e.setCost(Integer.parseInt(data[6]));
                e.setFeatured(Integer.parseInt(data[8]));
                e.setRank(Integer.parseInt(data[9]));
                e.setIsHangging(Integer.parseInt(data[17]));
                return e;
            } else return null;
        } catch (ClientProtocolException e) {
            Log.w("ERROR", e.toString());
            return null;

        } catch (IOException e) {
            Log.w("ERROR", e.toString());
            return null;
        }
    }

    private void ERROR() {
        if (MainActivity.context != null) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    DialogAdapter DA = new DialogAdapter(MainActivity.context);
                    DA.setFancy();
                    DA.attach(MainActivity.parent);
                    TextView tv = new TextView(MainActivity.context);
                    tv.setTextColor(MainActivity.context.getResources().getColor(R.color.purple));
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setPadding(10, 0, 10, 0);
                    tv.setTextSize(20);
                    tv.setText("This version of the application is not compatable, please update it!");
                    DA.add(tv);
                    DA.setOnDialogDestroyedListener(new DialogAdapter.OnDialogDestroyedListener() {
                        @Override
                        public void onDestroyed() {
                            ((Activity) MainActivity.context).finish();
                        }
                    });
                    DA.fire();
                }
            };
            ((Activity) MainActivity.context).runOnUiThread(r);
        }
    }
}
