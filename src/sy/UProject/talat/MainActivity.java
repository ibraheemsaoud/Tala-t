package sy.UProject.talat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.InternetTools;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.Utility;

public class MainActivity extends ActionBarActivity {

    static SQlite sql;
    static Context context;
    static RelativeLayout parent;
    static ImageView defaultActionButton, extraActionButton;
    static public LightNotificationAdapter LN;
    static Integer pre;
    static String username, password, email, sections, phone, region, money, about;
    static boolean ongoing_animation = false, notifications_exists = false,
            needRefresh = false;
    static Map<String, Boolean> userProfilePictureNeedsRefresh;

    private Date lastOpen;
    private Toolbar toolbar;
    private ListView mDrawerList;
    private List<DrawerItem> dataList;
    private DrawerLayout mDrawerLayout;
    private CustomDrawerAdapter adapter;
    private CharSequence mTitle, mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentOptions fragmentOptions;
    private FragmentStream fragmentStream;
    private FragmentSearch fragmentSearch;
    private FragmentUpcoming fragmentUpcoming;
    private FragmentEManager fragmentEManager;

    public MainActivity() {
    }

    static public void show_error(String text) {
        LN = new LightNotificationAdapter(MainActivity.context);
        LN.setTextColor(context.getResources().getColor(R.color.silver1)).setText(text);
        LN.attach((RelativeLayout) ((Activity) context).findViewById(R.id.master_parent)).fire();
    }

    private void construct() {
        pre = -1;
        needRefresh = false;
        sql = new SQlite(this);
        context = MainActivity.this;
        userProfilePictureNeedsRefresh = new HashMap<String, Boolean>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        parent = (RelativeLayout) findViewById(R.id.parent);
        extraActionButton = (ImageView) findViewById(R.id.temp2_action_button);
        defaultActionButton = (ImageView) findViewById(R.id.temp_action_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        defaultActionButton.setVisibility(View.INVISIBLE);
        defaultActionButton.setTag("1");
        setSupportActionBar(toolbar);

        String[] data = Utility.readFile(this, "user_data").split("\\<SPC\\/\\>");
        if (data.length > 7) {
            username = data[0];
            password = data[1];
            email = data[2];
            sections = data[3];
            money = data[4];
            region = data[5];
            phone = data[6];
            about = data[7];
        } else {
            Utility.WriteFile(this, "user_data", "");
            Utility.WriteFile(this, "notifications", "");
            Utility.WriteFile(this, "groups", "");
            Utility.WriteFile(this, "member_hangging", "");
            startActivity(new Intent(this,
                    WelcomeActivity.class));
            finish();
        }
    /*   SharedPreferences sharedpreferences = getApplicationContext()
                .getSharedPreferences("UProject-talaat", 0);
         if ("ar".equals(sharedpreferences.getString(
                "UProject-talaat_LANG", "en"))) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);
        }
    */
    }

    protected void onCreate(Bundle savedInstanceState) {
        //	if (savedInstanceState != null)
        //		super.onSaveInstanceState(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        construct();
        //TODO remove before release
        findViewById(R.id.offline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internet.link = "http://192.168.1.3/t";
            }
        });

        CharSequence charsequence;
        dataList = new ArrayList<DrawerItem>();
        charsequence = getString(R.string.home);
        mDrawerTitle = getString(R.string.app_name);
        mTitle = charsequence;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        dataList.add(new DrawerItem(getString(R.string.home),
                R.drawable.ic_action_home));
        dataList.add(new DrawerItem(getString(R.string.upcoming),
                R.drawable.ic_action_event));
        if (!"guest".equals(username)) {
            dataList.add(new DrawerItem(getString(R.string.hangging),
                    R.drawable.ic_action_make_available_offline, true));
            dataList.add(new DrawerItem(getString(R.string.search),
                    R.drawable.ic_action_search));
            dataList.add(new DrawerItem(getString(R.string.eventManager),
                    R.drawable.ic_action_edit));
            dataList.add(new DrawerItem(getString(R.string.groups),
                    R.drawable.ic_action_group, true));
            dataList.add(new DrawerItem(getString(R.string.settings),
                    R.drawable.ic_action_settings));
        } else {
            dataList.add(new DrawerItem(getString(R.string.search),
                    R.drawable.ic_action_search, true));
            dataList.add(new DrawerItem(getString(R.string.logout),
                    R.drawable.ic_action_remove));
        }
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(null));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.abc_action_bar_up_description) {
            public void onDrawerClosed(View view) {
                toolbar.setTitle(mTitle);
                if (android.os.Build.VERSION.SDK_INT > 13)
                    supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                toolbar.setTitle(mDrawerTitle);
                if (android.os.Build.VERSION.SDK_INT > 13)
                    supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        View drawerHeader = getLayoutInflater().inflate(R.layout.notification_drawer, null);
        DesignFunctions.setPuddle(drawerHeader, getResources().getColor(R.color.purple), getResources().getColor(R.color.purple7));
        mDrawerList.addHeaderView(drawerHeader);
        ((TextView) drawerHeader.findViewById(R.id.notification_header_username)).setText(username);
        final ImageView profilePic = (ImageView) drawerHeader.findViewById(R.id.notification_header_pic);
        DesignFunctions.setPoggy(profilePic);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap dbl = bitmap;
                                profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Bitmap dbl = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_template);
                profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.with(MainActivity.this)
                .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", username + ".jpg"))
                .placeholder(R.drawable.ic_user_template)
                .into(target);
        profilePic.setTag(target);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.context, Profile.class);
                i.putExtra("id", MainActivity.username);
                startActivity(i);
            }
        });
        drawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectItem(0);
            }
        });
        // if (savedInstanceState == null)
        SelectItem(1);
    }

    public void SelectItem(Integer i) {
        Bundle bundle;
        Fragment obj;
        bundle = new Bundle();
        obj = null;
        i--;
        if (pre == i) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }
        switch (i) {
            // notifications
            case -1:
                mDrawerLayout.closeDrawer(mDrawerList);
                showNotifications();
                break;
            // stream
            case 0:
                pre = 0;
                fragmentStream = new FragmentStream();
                obj = fragmentStream;
                break;
            // upcoming
            case 1:
                pre = 1;
                fragmentUpcoming = new FragmentUpcoming();
                obj = fragmentUpcoming;
                break;
            // hangging
            case 2:
                pre = 2;
                if ("guest".equals(username)) {
                    fragmentSearch = new FragmentSearch();
                    obj = fragmentSearch;
                    break;
                }
                break;
            // search
            case 3:
                pre = 3;
                if ("guest".equals(username)) {
                    sql.clear();
                    new ProfileQL(this).clear();
                    Utility.WriteFile(this, "user_data", "");
                    Utility.WriteFile(this, "notifications", "");
                    Utility.WriteFile(this, "groups", "");
                    Utility.WriteFile(this, "member_hangging", "");
                    startActivity(new Intent(this,
                            WelcomeActivity.class));
                    finish();
                    break;
                }
                fragmentSearch = new FragmentSearch();
                obj = fragmentSearch;
                break;
            // event manager
            case 4:
                pre = 4;
                fragmentEManager = new FragmentEManager();
                obj = fragmentEManager;
                break;
            // groups
            case 5:
                pre = 5;
                break;
            // settings
            case 6:
                pre = 6;
                fragmentOptions = new FragmentOptions();
                obj = fragmentOptions;
                break;
        }
        if (obj != null) {
            obj.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, obj).commit();
            mDrawerList.setItemChecked(i, true);
            setTitle(dataList.get(i).getItemName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList))
            mDrawerLayout.closeDrawer(mDrawerList);
        else if (fragmentSearch != null && fragmentSearch.hideSearchButton()) ;
        else if (DialogAdapter.KillLast(this)) ;
        else super.onBackPressed();
    }

    static public void ongoingNotification(String title) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "Talat";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        Intent notificationIntent = new Intent(MainActivity.context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, "", null);
        mNotificationManager.notify(1123581321, notification);
    }

    static public void stopOngoingNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        Log.d("s", "s");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        mNotificationManager.cancel(1123581321);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentOptions != null && fragmentOptions.internetTools != null) {
            if (fragmentOptions.internetTools != null && resultCode != 0) {
                ongoingNotification("uploading");
                fragmentOptions.internetTools.onResult(requestCode, resultCode, data, MainActivity.username, MainActivity.password);
            }
        }
    }

    public void setTitle(CharSequence charsequence) {
        mTitle = charsequence;
        // getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements
            android.widget.AdapterView.OnItemClickListener {

        private DrawerItemClickListener() {
        }

        DrawerItemClickListener(DrawerItemClickListener draweritemclicklistener) {
            this();
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                long l) {
            SelectItem(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastOpen == null)
            lastOpen = new Date();
        if (new Date(lastOpen.getTime() - new Date().getTime()).getTime() > 60 * 60 * 1000)
            userProfilePictureNeedsRefresh = new HashMap<String, Boolean>();
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
        mDrawerToggle.onOptionsItemSelected(menuitem);
        // int id = menuitem.getItemId();
        return true;
    }

    static void getImage(final String username) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.v("s", "problems");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory()
                                + File.separator + /*".Talat" + File.separator +*/ username + ".jpg");
                        //    if(!file.exists() && !file.mkdir()){
                        //        return;
                        //    }
                        Log.v("s", "s1");
                        try {
                            file.createNewFile();
                            FileOutputStream oStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, oStream);
                            oStream.close();
                            Log.v("s", "done");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("s", "error");
                        }
                        Log.v("s", file.toString());
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.v("s", "faild");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.v("s", "here");
                if (placeHolderDrawable != null) {
                }
            }
        };
        final ImageView tempImage = (ImageView) ((Activity) MainActivity.context).findViewById(R.id.temp_profile_pic);
        if (userProfilePictureNeedsRefresh.containsKey(username)) ;
        else {
            userProfilePictureNeedsRefresh.put(username, true);
    /*        Picasso.with(MainActivity.context)
                    .load(internet.link+"/users/" + username + "/profilePic.png")
                    .into(tempImage, new Callback.EmptyCallback());
            Picasso.with(MainActivity.context)
                    .load(internet.link+"/users/" + username + "/profilePic.png")
                    .into(target);
            */
        }
    }

    void showNotifications() {
        DialogAdapter DA = new DialogAdapter(this).setFancy().
                attach((RelativeLayout) findViewById(R.id.master_parent)).
                setDestroyable().setPadding(0, 0, 0, 6);
        DA.showHeadline().setHeadlineText(getString(R.string.notifications))
                .setHeadlineDividerColor(getResources().getColor(R.color.black7))
                .setHeadlineTextColor(getResources().getColor(R.color.purple));
        NotificationQL NQL = new NotificationQL(this);
        ArrayList<NotificationAdapter> notifications = new ArrayList<NotificationAdapter>();
        notifications.add(new NotificationAdapter("0", new Date().toString(), "0", "pony"));
        notifications.add(new NotificationAdapter("1", new Date().toString() + 60 * 1000, "0", "syriatel").setRead("1"));
        notifications.add(new NotificationAdapter("2", "2", new Date().toString() + 60 * 1000 * 7, "2", "4").setRead("1"));
        notifications.add(new NotificationAdapter("3", new Date().toString() + 60 * 1000 * 60, "1", "lucky numbers"));
        notifications.add(new NotificationAdapter("4", "1", new Date().toString() + 60 * 1000 * 60 * 4, "2", "3"));
        View.OnClickListener openProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Profile.class);
                i.putExtra("id", view.getTag().toString());
                startActivity(i);
            }
        };
        View.OnClickListener openEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EventTemplate.class);
                i.putExtra("id", view.getTag().toString());
                startActivity(i);
            }
        };
        int color1 = getResources().getColor(R.color.white),
                color2 = getResources().getColor(R.color.black4);
        boolean IN_UNREAD = true;
        for (int i = 0; i < notifications.size(); i++) {
            final NotificationAdapter NA = notifications.get(i);
            View notification = null, notificationWrapper = null;
            ImageView pic = null;
            if ("0".equals(NA.getType())) {
                notification = getLayoutInflater().inflate(R.layout.notification_follow, null);
                notificationWrapper = notification.findViewById(R.id.notification_wrapper);
                if (NA.getText().length() == 0)
                    ((TextView) notification.findViewById(R.id.notification_follow_text)).setText(NA.getExtra() + " started following you");
                else
                    ((TextView) notification.findViewById(R.id.notification_follow_text)).setText(NA.getText());
                final ImageView profilePic = (ImageView) notification.findViewById(R.id.notification_follow_pic);
                DesignFunctions.setPoggy(profilePic);
                InternetTools IT = new InternetTools(this);
                IT.getImage(profilePic, internet.link + "/users/" + NA.getExtra() + "/profilePic.png",
                        Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                        NA.getExtra() + ".jpg", R.drawable.ic_user_template, true);
                IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                    @Override
                    public void onLoaded(final Bitmap bitmap) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap dbl = bitmap;
                                profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                            }
                        });
                    }

                    @Override
                    public void onError() {

                        Bitmap dbl = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_template);
                        profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                    }
                });
                profilePic.setTag(NA.getExtra());
                profilePic.setOnClickListener(openProfile);
                notificationWrapper.setOnClickListener(openProfile);
            } else if ("1".equals(notifications.get(i).getType())) {
                notification = getLayoutInflater().inflate(R.layout.notification_group, null);
                notificationWrapper = notification.findViewById(R.id.notification_wrapper);
                ((TextView) notification.findViewById(R.id.notification_group_text)).setText("you have been added to the group \'" + NA.getExtra() + "\'");
                pic = (ImageView) notification.findViewById(R.id.notification_group_pic);
                pic.setOnClickListener(openEvent);
                notificationWrapper.setOnClickListener(openEvent);
            } else if ("2".equals(notifications.get(i).getType())) {
                notification = getLayoutInflater().inflate(R.layout.notification_event, null);
                notificationWrapper = notification.findViewById(R.id.notification_wrapper);
                ((TextView) notification.findViewById(R.id.notification_event_text)).setText(NA.getText() + " new people are hangging at your event");
                pic = (ImageView) notification.findViewById(R.id.notification_event_pic);
                pic.setOnClickListener(openEvent);
                notificationWrapper.setOnClickListener(openEvent);
            }
            if (notification != null) {
                if (pic != null) {
                    DesignFunctions.setPoggy(pic);
                    pic.setTag(NA.getExtra());
                }
                if (i == 0) {
                    notification.findViewById(R.id.notification_status).setVisibility(View.VISIBLE);
                    notification.findViewById(R.id.notification_status_indicator).setVisibility(View.VISIBLE);
                    ((TextView) notification.findViewById(R.id.notification_status)).setText("UNREAD NOTIFICATIONS");
                }
                if (IN_UNREAD && "1".equals(NA.getRead())) {
                    IN_UNREAD = false;
                    notification.findViewById(R.id.notification_status).setVisibility(View.VISIBLE);
                    notification.findViewById(R.id.notification_status_indicator).setVisibility(View.VISIBLE);
                    notification.findViewById(R.id.notification_status_indicator).setBackgroundResource(R.color.blue);
                    ((TextView) notification.findViewById(R.id.notification_status)).setText("OLD NOTIFICATIONS");
                    ((TextView) notification.findViewById(R.id.notification_status)).setTextColor(getResources().getColor(R.color.blue));
                }
                if (i == notifications.size() - 1)
                    notification.findViewById(R.id.notification_indicator).setVisibility(View.GONE);
                DesignFunctions.setPuddle(notificationWrapper, color1, color2);
                notificationWrapper.setTag(NA.getExtra());
                DA.add(notification);
                NA.setRead("1");
            }
            NQL.updateNotification(NA);
        }
        NQL.close();
        NQL = null;
        DA.fire();
    }
}
