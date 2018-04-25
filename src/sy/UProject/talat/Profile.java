package sy.UProject.talat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.Random;

import sy.UProject.UI.AnimationFunctions;
import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.InternetTools;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.LoaderAdapter;
import sy.UProject.UI.Utility;

public class Profile extends ActionBarActivity {

    public LightNotificationAdapter LN;
    public DialogAdapter DA;
    public LoaderAdapter LA;
    private Toolbar toolbar;
    private RelativeLayout parent;
    private TextView Hangs, Hangouts, Followers, about, aboutDub, user;
    private ImageView profilePic;
    private Button Follow, more;
    private String id, username, password;
    private boolean followed = false;

    private int colorPrimary, colorPrimaryDark, colorPrimaryDarker, colorWhite, colorBlack1, c;
    private int[] colors = {R.color.blue4, R.color.red4, R.color.red4, R.color.green4,
            R.color.orange4, R.color.dblue4, R.color.yellow5, R.color.silver4},
            colors2 = {R.color.blue, R.color.red, R.color.red, R.color.green,
                    R.color.orange, R.color.dblue, R.color.yellow6,
                    R.color.silver},
            colors3 = {R.color.blue6, R.color.red6, R.color.red6, R.color.green6,
                    R.color.orange6, R.color.dblue6, R.color.yellow7,
                    R.color.silver6};
    private String[] colorsInStr = {"2196f3", "f44336", "f44336", "00c853", "ff9800",
            "3f51b5", "fdd835", "607d8b"};
    private View.OnClickListener clickHangs, clickHangouts, clickFollowers;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("id"))
            finish();

        String[] data = Utility.readFile(this, "user_data").split("\\<SPC\\/\\>");
        if (data.length > 7) {
            username = data[0];
            password = data[1];
        }
        id = bundle.getString("id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parent = (RelativeLayout) findViewById(R.id.profile_parent);
        about = (TextView) findViewById(R.id.profile_about);
        Hangs = (TextView) findViewById(R.id.profile_hangs);
        user = (TextView) findViewById(R.id.profile_username);
        aboutDub = (TextView) findViewById(R.id.profile_about2);
        Hangouts = (TextView) findViewById(R.id.profile_hangouts);
        Followers = (TextView) findViewById(R.id.profile_followers);
        profilePic = (ImageView) findViewById(R.id.profile_picture);

        Follow = (Button) findViewById(R.id.profile_follow);
        more = (Button) findViewById(R.id.profile_more);

        ProfileQL PQL = new ProfileQL(this);
        if ("1".equals(PQL.getFollow(id)))
            followed = true;
        PQL = null;

        if (followed)
            Follow.setText(getString(R.string.following));
        if (username.equals(id)) {
            Follow.setText(getString(R.string.EDIT));
        }

        user.setText(id);
        construct();

        Fetch fetch = new Fetch();
        fetch.execute(username, password, id);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity) Profile.this).runOnUiThread(new Runnable() {
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
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(Profile.this)
                .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", id + ".jpg"))
                .placeholder(R.drawable.ic_user_template)
                .into(target);
        profilePic.setTag(target);
    }

    void construct() {
        clickFollowers = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetFollowers GF = new GetFollowers();
                GF.execute(username, password, id);
            }
        };

        clickHangs = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.profile_events_wrapper)).removeAllViews();
                stream STREAM = new stream();
                STREAM.username = username;
                STREAM.password = password;
                STREAM.extra1 = id;
                STREAM.doAll(Profile.this, (RelativeLayout) findViewById(R.id.profile_events_wrapper), "get_hangs");
            }
        };
        clickHangouts = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.profile_events_wrapper)).removeAllViews();
                stream STREAM = new stream();
                STREAM.username = username;
                STREAM.password = password;
                STREAM.extra1 = id;
                STREAM.doAll(Profile.this, (RelativeLayout) findViewById(R.id.profile_events_wrapper), "get_hangouts");
            }
        };
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RelativeLayout) findViewById(R.id.profile_events_wrapper)).removeAllViews();
                setUpPictures();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().toString().equals(getString(R.string.more))) {
                    ((TextView) view).setText(getString(R.string.less));
                    about.setVisibility(View.VISIBLE);
                    AnimationFunctions.expand(about);
                } else {
                    ((TextView) view).setText(getString(R.string.more));
                    AnimationFunctions.collapse(about, View.INVISIBLE);
                }
            }
        });
        //    AnimationFunctions.firstExpand(about, View.GONE);

        Random r = new Random();
        c = r.nextInt(9) % 8;
        colorWhite = getResources().getColor(R.color.white);
        colorBlack1 = getResources().getColor(R.color.black1);
        colorPrimary = getResources().getColor(colors[c]);
        colorPrimaryDark = getResources().getColor(colors2[c]);
        colorPrimaryDarker = getResources().getColor(colors3[c]);
        about.setTextColor(colors[c]);
        aboutDub.setTextColor(colors[c]);
        parent.setBackgroundColor(colorPrimaryDark);
        findViewById(R.id.profile_indecator).setBackgroundColor(colorPrimaryDarker);
    //    findViewById(R.id.profile_linearlayout).setBackgroundColor(colorPrimaryDark);
        DesignFunctions.setPuddle(more, colorPrimaryDark, colorPrimary);
        DesignFunctions.setPoggy(profilePic);
        if (!followed)
            DesignFunctions.setPuddle(Follow, colorWhite, colorPrimary);
        else
            DesignFunctions.setPuddle(Follow, colorPrimary, colorBlack1);
        int[][] states = new int[3][];
        int[] colors = new int[3];

        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[0];
        if (!followed) {
            colors[2] = colorPrimaryDarker;
            colors[1] = colorWhite;
            colors[0] = colorWhite;
        } else {
            colors[2] = colorWhite;
            colors[1] = colorWhite;
            colors[0] = colorPrimaryDarker;
        }

        Follow.setTextColor(new ColorStateList(states, colors));
    }

    void setUpPictures() {
        LayoutInflater inflater = (LayoutInflater) Profile.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.profile_random_pics, null);
        int width;
        Display display = getWindowManager().getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else
            width = display.getWidth();
        ImageView[] pic = new ImageView[16];
        int[] background = {0, R.drawable.concert_0, R.drawable.dubarah_ad_n, R.drawable.dubarah_ad_n_1,
                R.drawable.education, R.drawable.family, R.drawable.dubarah_ad_n_3, R.drawable.dubarah_ad_n_2,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

        for (int i = 1; i <= 15; i++) {
            int id = this.getResources().getIdentifier("profile_pic_" + i, "id", this.getPackageName());
            pic[i] = (ImageView) view1.findViewById(id);
            if (i == 3 || i == 4 || i == 7) {
                view1.findViewById(this.getResources().getIdentifier("profile_pic_con_" + i, "id", this.getPackageName())).getLayoutParams().width = width / 2;
                view1.findViewById(this.getResources().getIdentifier("profile_pic_con_" + i, "id", this.getPackageName())).getLayoutParams().height = width / 2;
            } else {
                view1.findViewById(this.getResources().getIdentifier("profile_pic_con_" + i, "id", this.getPackageName())).getLayoutParams().width = width / 4;
                view1.findViewById(this.getResources().getIdentifier("profile_pic_con_" + i, "id", this.getPackageName())).getLayoutParams().height = width / 4;
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), background[i]);
            pic[i].setImageBitmap(DesignFunctions.getRoundedCornerBitmap(bitmap, 2));
            DesignFunctions.setPoggy(pic[i]);
        }

        ((RelativeLayout) findViewById(R.id.profile_events_wrapper)).addView(view1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!DialogAdapter.KillLast(this))
            super.onBackPressed();
    }

    private class Fetch extends AsyncTask<String, Integer, Integer> {
        String username, password, id;
        String resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            id = params[2];
            internet i = new internet();
            resp = i.fetch_user(username, password, id);
            if (resp.equals("error"))
                return -3707;
            return 1;
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(Profile.this);
            LA.setTextColor(getResources().getColor(R.color.purple));
            LA.attach(parent);
            //    LA.fire();
            super.onPreExecute();
        }

        String data1, data2, data3;
        ProfileQL PQL;

        @Override
        protected void onPostExecute(Integer result) {
            //    LA.destroy();
            DialogAdapter dialogAdapter = new DialogAdapter(Profile.this);
            dialogAdapter.setFancy();
            TextView errorView = new TextView(Profile.this);
            errorView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            errorView.setTextColor(getResources().getColor(R.color.purple));
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            dialogAdapter.setOnDialogDestroyedListener(new DialogAdapter.OnDialogDestroyedListener() {
                @Override
                public void onDestroyed() {
                    finish();
                }
            });
            switch (result) {
                case 413:
                    errorView.setText(getString(R.string.E_user_not_found));
                    dialogAdapter.add(errorView);
                    dialogAdapter.attach(parent);
                    dialogAdapter.fire();
                    break;
                case -3707:
                    errorView.setText(getString(R.string.E_internet));
                    dialogAdapter.add(errorView);
                    dialogAdapter.attach(parent);
                    dialogAdapter.fire();
                    break;
                case 1:
                    String[] data = resp.split("\\<BR \\/\\>");
                    about.setText(data[0]);
                    Hangs.setText(data[1]);
                    aboutDub.setText(data[0]);
                    Hangouts.setText(data[2]);
                    Followers.setText(data[3]);
                    if (!"0".equals(data[1]))
                        Hangs.setOnClickListener(clickHangs);
                    if (!"0".equals(data[2]))
                        Hangouts.setOnClickListener(clickHangouts);
                    if (!"0".equals(data[3]))
                        Followers.setOnClickListener(clickFollowers);
                    PQL = new ProfileQL(Profile.this);
                    String pic = PQL.getPic(id), aboutt = PQL.getAbot(id);
                    data1 = data[0];
                    data2 = data[4];
                    data3 = data[5];
                    if (PQL.checkProfile(id) && (aboutt == null || aboutt.compareTo(data[0]) != 0))
                        PQL.updateProfile(id, data[0], pic);
                    if (!PQL.checkProfile(id) || (pic != null && data[4].compareTo(pic) != 0)) {
                        InternetTools IT = new InternetTools(Profile.this);
                        IT.getImage(profilePic, internet.link + "/users/" + id + "/profilePic.png",
                                Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                                id + ".jpg", R.drawable.ic_user_template, true);
                        IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                            @Override
                            public void onLoaded(Bitmap bitmap) {
                                Profile.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(Profile.this)
                                                .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", id + ".jpg"))
                                                .placeholder(R.drawable.ic_user_template)
                                                .priority(Picasso.Priority.HIGH)
                                                .into(profilePic);
                                        PQL = new ProfileQL(Profile.this);
                                        PQL.addProfile(id, data1, data2, data3);
                                        PQL = null;
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    }
                    PQL.updateFollow(id, data3);
                    PQL = null;
                    if (about.getLineCount() > 2) {
                        about.setVisibility(View.GONE);
                        more.setVisibility(View.VISIBLE);
                    }
                    int[][] states = new int[3][];
                    int[] colors = new int[3];
                    states[0] = new int[]{android.R.attr.state_pressed};
                    states[1] = new int[]{android.R.attr.state_selected};
                    states[2] = new int[0];
                    if("1".equals(data3)) {
                        followed = true;
                        Follow.setText(getString(R.string.following));
                        DesignFunctions.setPuddle(Follow, colorPrimary, colorBlack1);
                        colors[2] = colorWhite;
                        colors[1] = colorWhite;
                        colors[0] = colorPrimaryDarker;
                    } else {
                        followed = false;
                        Follow.setText(getString(R.string.follow));
                        DesignFunctions.setPuddle(Follow, colorWhite, colorPrimary);
                        colors[2] = colorPrimaryDarker;
                        colors[1] = colorWhite;
                        colors[0] = colorWhite;
                    }
                    Follow.setTextColor(new ColorStateList(states, colors));
                    Follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!username.equals(id)) {
                                FoLlow fol = new FoLlow();
                                fol.execute(username, password, id);
                            }
                        }
                    });
                    break;
                default:
                    errorView.setText(getString(R.string.E_unknown));
                    dialogAdapter.add(errorView);
                    dialogAdapter.attach(parent);
                    dialogAdapter.fire();
            }
            cancel(false);
        }
    }

    private class GetFollowers extends AsyncTask<String, Integer, Integer> {
        String username, password, userID, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            userID = params[2];
            internet i = new internet();
            resp = i.getFollowers(username, password, userID);
            if (resp.equals("error"))
                return -3707;
            if (resp.equals("403"))
                return 403;
            return 1;
        }

        @Override
        protected void onPreExecute() {
            DA = new DialogAdapter(Profile.this);
            DA.holder.oneButton.setVisibility(View.GONE);
            DA.attach(parent);
            DA.setDestroyable();
            DA.showHeadline();
            DA.setHeadlineText(getString(R.string.loading));
            DA.setHeadlineTextColor(colorPrimaryDark);
            DA.setPadding(0, 0, 0, 8);

            TextView tvv = new TextView(Profile.this);
            tvv.setTextColor(getResources().getColor(R.color.black7));
            tvv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvv.setGravity(Gravity.CENTER);
            tvv.setPadding(8, 8, 8, 8);
            tvv.setTextSize(20);
            tvv.setText(getString(R.string.loading));
            DA.add(tvv);

            DA.fire();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Integer result) {
            DA.holder.body.removeAllViews();
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(Profile.this);
                    LN.setTextColor(Profile.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    break;
                case 403:
                    DA.setHeadlineText(getString(R.string.error));
                    TextView tvv = new TextView(Profile.this);
                    tvv.setTextColor(getResources().getColor(R.color.black7));
                    tvv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tvv.setGravity(Gravity.CENTER);
                    tvv.setPadding(8, 8, 8, 8);
                    tvv.setTextSize(20);
                    tvv.setText("no followers found");
                    DA.add(tvv);
                    break;
                case 1:
                    final String[] people = resp.split("\\<BR \\/\\>");
                    DA.setHeadlineText(getString(R.string.followers));

                    DA.holder.body.removeAllViews();

                    LayoutInflater inflater = (LayoutInflater) Profile.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    for (int i = 0; i < people.length; i++) {
                        View container = inflater.inflate(R.layout.profile_mini, null);
                        final ImageView pic = (ImageView) container.findViewById(R.id.profile_mini_pic);
                        ((TextView) container.findViewById(R.id.profile_mini_username)).setText(people[i]);
                        if (i == people.length - 1)
                            container.findViewById(R.id.profile_mini_indecator).setVisibility(View.GONE);
                        container.setTag(people[i]);
                        pic.setTag(people[i]);
                        DesignFunctions.setPuddle(container, colorWhite, colorPrimaryDark);
                        container.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Profile.this, Profile.class);
                                i.putExtra("id", view.getTag().toString());
                                startActivity(i);
                            }
                        });
                        DA.add(container);
                        InternetTools IT = new InternetTools(Profile.this);
                        IT.getImage(pic, internet.link + "/users/" + people[i] + "/profilePic.png",
                                Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                                people[i] + ".jpg", R.drawable.ic_user_template, false);
                        IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                            @Override
                            public void onLoaded(final Bitmap bitmap) {
                                Profile.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(bitmap, 4));
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                                Profile.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap dbl = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_template);
                                        pic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                                    }
                                });
                            }
                        });
                    }
            }
            cancel(false);
        }
    }

    private class FoLlow extends AsyncTask<String, Integer, Integer> {
        String username, password, userID, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            userID = params[2];
            internet i = new internet();
            resp = i.FoLlow(username, password, userID);
            if (resp.equals("error") || "0".equals(resp))
                return -3707;
            if ("606".equals(resp))
                return 606;
            return 1;
        }

        @Override
        protected void onPostExecute(final Integer result) {
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(Profile.this);
                    LN.setTextColor(Profile.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    break;
                case 606:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(Profile.this);
                    LN.setTextColor(Profile.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_not_logged));
                    LN.attach(parent).fire();
                    break;
                case 1:
                    ProfileQL PQL = new ProfileQL(Profile.this);
                    followed = !followed;

                    int[][] states = new int[2][];
                    int[] colorss = new int[2];

                    states[0] = new int[]{android.R.attr.state_pressed};
                    states[1] = new int[0];

                    if (!followed) {
                        PQL.updateFollow(id, "0");
                        Follow.setText(getString(R.string.follow));
                        DesignFunctions.setPuddle(Follow, colorWhite, colorPrimary);
                        colorss[1] = colorPrimaryDarker;
                        colorss[0] = colorWhite;
                    } else {
                        PQL.updateFollow(id, "1");
                        Follow.setText(getString(R.string.following));
                        DesignFunctions.setPuddle(Follow, colorPrimary, colorBlack1);
                        colorss[1] = colorWhite;
                        colorss[0] = colorPrimaryDarker;
                    }
                    PQL = null;
                    Follow.setTextColor(new ColorStateList(states, colorss));
            }
            cancel(false);
        }
    }
}
