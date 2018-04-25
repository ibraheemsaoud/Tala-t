package sy.UProject.talat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.Date;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.InternetTools;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.LoaderAdapter;
import sy.UProject.UI.Utility;

public class EventTemplate extends ActionBarActivity {

    private UiLifecycleHelper uiHelper;
    boolean pendingShare;
    Session session;

    public LightNotificationAdapter LN;
    public DialogAdapter DA;
    public LoaderAdapter LA;
    private HANG hang;
    private Event event;
    private GETHANGGERS GH;
    private Toolbar toolbar;
    private RelativeLayout parent;
    private ImageView profile, toolbarProfile, cover;
    private ImageView Hang, Share;
    private TextView hanggers, timing;
    private View upload, feature, ticketMake, statistics;
    private String id, username, password;
    static public Integer UPLOAD;
    private InternetTools internetTools;
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
    private int[] backgrounds = {R.drawable.concert_gif, R.drawable.family_gif, R.drawable.family_gif,
            R.drawable.sport_gif, R.drawable.orange_gif, R.drawable.education_gif,
            R.drawable.yellow_gif, R.drawable.other_gif};
    private int[] backgroundsStatic = {R.drawable.concert, R.drawable.family, R.drawable.family,
            R.drawable.sport, R.drawable.orange_gif, R.drawable.education,
            R.drawable.yellow_gif, R.drawable.other, R.drawable.ultimate};
    private int colorPrimary, colorPrimaryDark, colorPrimaryDarker, color2, colorWhite,
            MAX_SCROLL_ALLOWED = -1, oldS = -9999, newS = -9999, NoldS = -9999, NnewS = -9999, ZERO = 0;
    private boolean changedPView, NOT_INITIALIZED = true, ongoing = false, STOP_HANG_ANIMATION = false, HIDE_MENU = true;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_template);

        FBSTUFF();
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            uiHelper = new UiLifecycleHelper(this, null);
            uiHelper.onCreate(savedInstanceState);
        }

        if (savedInstanceState != null) {
            pendingShare = savedInstanceState.getBoolean("pendingShare");
            session = Session.restoreSession(this, null, new SessionCallback(), savedInstanceState);
        }

        // Typeface type =
        // Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf");
        changedPView = false;
        Bundle bundle = getIntent().getExtras();
        if (!bundle.containsKey("id"))
            finish();

        String[] data = Utility.readFile(this, "user_data").split("\\<SPC\\/\\>");
        if (data.length > 7) {
            username = data[0];
            password = data[1];
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parent = (RelativeLayout) findViewById(R.id.ET_parent);
        profile = (ImageView) findViewById(R.id.ET_profile);
        toolbarProfile = (ImageView) findViewById(R.id.ET_toolbar_profile);
        hanggers = (TextView) findViewById(R.id.ET_hangging_count);
        timing = (TextView) findViewById(R.id.ET_time);
        Hang = (ImageView) findViewById(R.id.ET_hang);
        Share = (ImageView) findViewById(R.id.ET_share);
        cover = (ImageView) findViewById(R.id.ET_cover);

        id = bundle.getString("id");
        SQlite sql = new SQlite(EventTemplate.this);
        event = sql.getEvent(id);
        sql.close();
        if (event == null) {
            Fetch fetch = new Fetch();
            fetch.execute(username, password, id);
            return;
        }
        colorPrimary = getResources().getColor(colors[event.getSection()]);
        colorPrimaryDark = getResources().getColor(colors2[event.getSection()]);
        colorPrimaryDarker = getResources().getColor(colors3[event.getSection()]);
        colorWhite = getResources().getColor(R.color.white);
        color2 = getResources().getColor(R.color.black4);

    /*    if (android.os.Build.VERSION.SDK_INT >= 16)
            toolbar.setBackground(new ColorDrawable(Color.parseColor("#00"
                    + colorsInStr[event.getSection()])));
        else
            toolbar.setBackgroundDrawable(new ColorDrawable(Color
                    .parseColor("#00" + colorsInStr[event.getSection()])));
    */
        parent.setBackgroundColor(colorPrimaryDarker);
        Share.setBackgroundColor(colorPrimaryDark);
        Hang.setBackgroundColor(colorPrimaryDark);
        cover.setBackgroundColor(colorPrimaryDark);

        toolbar.setTitle(event.getHeadline());
        ((TextView) findViewById(R.id.ET_desc)).setText(event.getDesc());
        ((TextView) findViewById(R.id.ET_headline)).setText(event.getHeadline());

        DesignFunctions.setPuddle(Hang, colorPrimaryDark, color2);
        DesignFunctions.setPuddle(Share, colorPrimaryDark, color2);
        DesignFunctions.setPoggy(profile);

        if (event.getCost() > 0)
            Hang.setImageResource(R.drawable.imagebutton_hang_paid);

        setBackGround(event.getFeatured(), false);

        profile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventTemplate.this, Profile.class);
                i.putExtra("id", event.getHost());
                startActivity(i);
            }
        });
        toolbarProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventTemplate.this, Profile.class);
                i.putExtra("id", event.getHost());
                startActivity(i);
            }
        });

        final ScrollView scrollView = (ScrollView) findViewById(R.id.ET_container);
        findViewById(R.id.ET_dump_scroll).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int num = profile.getBottom() - profile.getHeight()
                        - scrollView.getScrollY();
                ZERO = toolbar.getHeight();
                NnewS = v.getScrollY();
                if (NoldS <= NnewS && v.getScrollY() == 0)
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                else if (num > ZERO)
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                else
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                NoldS = NnewS;
                return true;
            }
        });
        scrollView.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                //    findViewById(R.id.ET_dump_scroll).getParent().requestDisallowInterceptTouchEvent(false);
                arg0.getParent().requestDisallowInterceptTouchEvent(true);
                int num = profile.getBottom() - profile.getHeight()
                        - scrollView.getScrollY();
                ZERO = toolbar.getHeight();
                newS = scrollView.getScrollY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_EXIT:
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                        if (num > ZERO && changedPView) {
                            changedPView = false;
                            Animation in = AnimationUtils.loadAnimation(
                                    EventTemplate.this, R.anim.slide_out_right);
                            in.setDuration(200);
                            toolbarProfile.startAnimation(in);
                            toolbarProfile.setVisibility(View.GONE);
                            if (EventTemplate.this.event.getHost().equals(username))
                                findViewById(R.id.ET_more_options).setVisibility(View.VISIBLE);
                            in.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation in = AnimationUtils.loadAnimation(
                                            EventTemplate.this,
                                            R.anim.slide_in_right);
                                    in.setDuration(200);
                                    profile.startAnimation(in);
                                    profile.setVisibility(View.VISIBLE);
                                }
                            });
                        } else if (num <= ZERO && !changedPView) {
                            changedPView = true;
                            Animation out = AnimationUtils.loadAnimation(
                                    EventTemplate.this, R.anim.slide_out_right);
                            out.setDuration(200);
                            profile.startAnimation(out);
                            profile.setVisibility(View.INVISIBLE);
                            findViewById(R.id.ET_more_options).setVisibility(View.GONE);
                            out.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation in = AnimationUtils.loadAnimation(
                                            EventTemplate.this,
                                            R.anim.slide_in_right);
                                    in.setDuration(200);
                                    toolbarProfile.startAnimation(in);
                                    toolbarProfile.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        if (num > ZERO)
                            cover.scrollTo(0, scrollView.getScrollY());
                        else
                            findViewById(R.id.ET_dump_scroll)
                                    .scrollBy(0, newS - oldS);
                        break;
                    default:
                        if (NOT_INITIALIZED) {
                            NOT_INITIALIZED = false;
                            int height;
                            Display display = getWindowManager()
                                    .getDefaultDisplay();
                            if (android.os.Build.VERSION.SDK_INT >= 13) {
                                Point size = new Point();
                                display.getSize(size);
                                height = size.y;
                            } else
                                height = display.getHeight();
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) findViewById(
                                    R.id.ET_dump_scroll).getLayoutParams();
                            /*    if (height - 10// - toolbar.getHeight()
                                        - findViewById(R.id.ET_loader).getHeight()
                                        - profile.getHeight() < findViewById(
                                        R.id.ET_dump_scroll).getHeight())
                            */
                            if (height - findViewById(R.id.ET_dump_profile).getBottom()
                                    < findViewById(R.id.ET_dump_scroll).getHeight())
                                lp.height = height - findViewById(R.id.ET_dump_profile).getBottom() + findViewById(R.id.ET_dump_profile).getTop() - toolbar.getBottom();
                            findViewById(R.id.ET_dump_scroll).setLayoutParams(lp);
                        }
                        if (num == ZERO || (MAX_SCROLL_ALLOWED == -1 && num < 0))
                            MAX_SCROLL_ALLOWED = scrollView.getScrollY();
                        if (num > ZERO)
                            cover.scrollTo(0, scrollView.getScrollY());
                        else
                            findViewById(R.id.ET_dump_scroll)
                                    .scrollBy(0, newS - oldS);
                        oldS = newS;
                        if (num > ZERO && changedPView) {
                            changedPView = false;
                            Animation in = AnimationUtils.loadAnimation(
                                    EventTemplate.this, R.anim.slide_out_right);
                            in.setDuration(200);
                            toolbarProfile.startAnimation(in);
                            toolbarProfile.setVisibility(View.GONE);
                            in.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation in = AnimationUtils.loadAnimation(
                                            EventTemplate.this,
                                            R.anim.slide_in_right);
                                    in.setDuration(200);
                                    profile.startAnimation(in);
                                    profile.setVisibility(View.VISIBLE);
                                    if (EventTemplate.this.event.getHost().equals(username))
                                        findViewById(R.id.ET_more_options).setVisibility(View.VISIBLE);
                                }
                            });
                        } else if (num <= ZERO && !changedPView) {
                            changedPView = true;
                            Animation out = AnimationUtils.loadAnimation(
                                    EventTemplate.this, R.anim.slide_out_right);
                            out.setDuration(200);
                            profile.startAnimation(out);
                            profile.setVisibility(View.INVISIBLE);
                            findViewById(R.id.ET_more_options).setVisibility(View.GONE);
                            out.setAnimationListener(new AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation in = AnimationUtils.loadAnimation(
                                            EventTemplate.this,
                                            R.anim.slide_in_right);
                                    in.setDuration(200);
                                    toolbarProfile.startAnimation(in);
                                    toolbarProfile.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        // }
                }
                return false;
            }
        });
        if (event.getHost().equals(username)) {
            findViewById(R.id.ET_more_options).setVisibility(View.VISIBLE);
            findViewById(R.id.ET_menu_back).setVisibility(View.VISIBLE);
            upload = findViewById(R.id.ET_more_upload);
            feature = findViewById(R.id.ET_more_feature);
            ticketMake = findViewById(R.id.ET_more_ticket);
            //        statistics = findViewById(R.id.ET_more_statistics);
            DesignFunctions.setPuddle(upload, colorWhite, colorPrimary, 4, DesignFunctions.TOP_LEFT, 1, getResources().getColor(R.color.silver));
            DesignFunctions.setPuddle(feature, colorWhite, colorPrimary, 0, 0, 1, getResources().getColor(R.color.silver));
            DesignFunctions.setPuddle(ticketMake, colorWhite, colorPrimary, 4, DesignFunctions.BOTTOM_LEFT, 1, getResources().getColor(R.color.silver));
            //        DesignFunctions.setPuddle(statistics, colorWhite, colorPrimary, 4, DesignFunctions.BOTTOM_LEFT, 1, getResources().getColor(R.color.silver));
            findViewById(R.id.ET_more_options).setOnClickListener(new OnClickListener() {
                public int ANIMATION_NUM;

                @Override
                public void onClick(View view) {
                    LinearLayout pics, texts;
                    pics = (LinearLayout) findViewById(R.id.ET_more_options_con);
                    texts = (LinearLayout) findViewById(R.id.ET_more_options_text);
                    if (upload.getVisibility() == View.VISIBLE) {
                        HIDE_MENU = true;
                        upload.setVisibility(View.INVISIBLE);
                        feature.setVisibility(View.INVISIBLE);
                        ticketMake.setVisibility(View.INVISIBLE);
                        //            statistics.setVisibility(View.INVISIBLE);

                        upload.setVisibility(View.VISIBLE);
                        feature.setVisibility(View.VISIBLE);
                        ticketMake.setVisibility(View.VISIBLE);
                        //            statistics.setVisibility(View.VISIBLE);

                        Animation slideRight = AnimationUtils.loadAnimation(
                                EventTemplate.this, R.anim.slide_out_right);
                        slideRight.setDuration(500);
                        texts.setLayoutAnimation(new LayoutAnimationController(slideRight, -0.2f));
                        ANIMATION_NUM = 0;
                        slideRight.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                switch (++ANIMATION_NUM) {
                                    //                    case 1:
                                    //                        statistics.setVisibility(View.GONE);
                                    //                        break;
                                    case 2 - 1:
                                        ticketMake.setVisibility(View.GONE);
                                        break;
                                    case 3 - 1:
                                        feature.setVisibility(View.GONE);
                                        break;
                                    case 4 - 1:
                                        findViewById(R.id.ET_more_options_con).setVisibility(View.GONE);
                                        upload.setVisibility(View.GONE);
                                        break;
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    } else {
                        HIDE_MENU = false;
                        //        pics.setVisibility(View.VISIBLE);
                        upload.setVisibility(View.VISIBLE);
                        feature.setVisibility(View.VISIBLE);
                        ticketMake.setVisibility(View.VISIBLE);
                        //            statistics.setVisibility(View.VISIBLE);
                        Animation slidOut = AnimationUtils.loadAnimation(
                                EventTemplate.this, R.anim.abc_slide_in_top);
                        slidOut.setDuration(500);
                        pics.setAnimation(slidOut);
                        slidOut.start();
                        Animation slideRight = AnimationUtils.loadAnimation(
                                EventTemplate.this, R.anim.slide_in_right);
                        slideRight.setDuration(500);
                        texts.setLayoutAnimation(new LayoutAnimationController(slideRight, 0.2f));
                    }
                }
            });
            findViewById(R.id.ET_menu_back).setOnTouchListener(new OnTouchListener() {
                public int ANIMATION_NUM;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (HIDE_MENU) return false;
                        HIDE_MENU = true;
                        upload.setVisibility(View.INVISIBLE);
                        feature.setVisibility(View.INVISIBLE);
                        ticketMake.setVisibility(View.INVISIBLE);
                        //            statistics.setVisibility(View.INVISIBLE);

                        upload.setVisibility(View.VISIBLE);
                        feature.setVisibility(View.VISIBLE);
                        ticketMake.setVisibility(View.VISIBLE);
                        //           statistics.setVisibility(View.VISIBLE);

                        Animation slidIn = AnimationUtils.loadAnimation(
                                EventTemplate.this, R.anim.slide_out_up);
                        slidIn.setDuration(600);
                        slidIn.start();
                        Animation slideRight = AnimationUtils.loadAnimation(
                                EventTemplate.this, R.anim.slide_out_right);
                        slideRight.setDuration(500);
                        ((LinearLayout) findViewById(R.id.ET_more_options_text)).setLayoutAnimation(new LayoutAnimationController(slideRight, -0.2f));
                        ANIMATION_NUM = 0;
                        slideRight.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                switch (++ANIMATION_NUM) {
                                    //                   case 1:
                                    //                       statistics.setVisibility(View.GONE);
                                    //                       break;
                                    case 2 - 1:
                                        ticketMake.setVisibility(View.GONE);
                                        break;
                                    case 3 - 1:
                                        feature.setVisibility(View.GONE);
                                        break;
                                    case 4 - 1:
                                        findViewById(R.id.ET_more_options_con).setVisibility(View.GONE);
                                        upload.setVisibility(View.GONE);
                                        break;
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }
                    return false;
                }
            });
            feature.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSmallMenu();
                    LayoutInflater inflater = (LayoutInflater) EventTemplate.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View featureView = inflater.inflate(R.layout.event_template_dialog_feature, null);
                    DA = null;
                    DA = new DialogAdapter(EventTemplate.this).attach(parent);
                    DA.setFancy().setDestroyable();
                    DA.showHeadline().setHeadlineText("Reach more people").setHeadlineTextColor(colorPrimary).setHeadlineDividerColor(colorPrimary);
                    DA.holder.fancy.setVisibility(View.GONE);
                    DA.setPadding(0, 0, 0, 0);
                    DA.add(featureView).fire();
                    Button op1 = (Button) featureView.findViewById(R.id.ET_feature_op1);
                    Button op2 = (Button) featureView.findViewById(R.id.ET_feature_op2);
                    Button op3 = (Button) featureView.findViewById(R.id.ET_feature_op3);
                    DesignFunctions.setPuddle(op1, colorWhite, getResources().getColor(R.color.black5));
                    DesignFunctions.setPuddle(op2, colorWhite, getResources().getColor(R.color.black5));
                    DesignFunctions.setPuddle(op3, colorWhite, getResources().getColor(R.color.black5));
                    OnClickListener OCL = new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogAdapter.KillLast(EventTemplate.this);
                            DA = null;
                            DA = new DialogAdapter(EventTemplate.this);
                            DA.setFancy().attach(parent);
                            DA.holder.ok.setTextColor(getResources().getColor(R.color.red));
                            DA.fire();
                        }
                    };
                }
            });
            upload.setOnClickListener(new OnClickListener() {
                public int ticked = -1
                        ,
                        count = 0;

                @Override
                public void onClick(View view) {
                    hideSmallMenu();
                    DA = new DialogAdapter(EventTemplate.this);
                    DA.setDestroyable();
                    DA.holder.oneButton.setText("Choose");
                    DA.showHeadline();
                    DA.setHeadlineText("Choose a Cover photo");
                    DA.setHeadlineTextColor(getResources().getColor(R.color.purple));
                    DA.attach(parent);
                    DA.setPadding(4, 4, 4, 4);
                    DA.fire();
                    DA.holder.oneButton.setEnabled(false);
                    DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ticked == -1)
                                DA.destroy();
                            updateCover UC = new updateCover();
                            UC.execute(username, password, id, String.valueOf(ticked));
                        }
                    });
                    final ImageView[] Ticks;
                    Ticks = new ImageView[8];

                    OnClickListener OCL = new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int curr = Integer.parseInt(view.getTag().toString());
                            if (ticked == curr) {
                                Picasso.with(EventTemplate.this).load(R.drawable.ic_action_unticked).into(Ticks[ticked]);
                                DA.holder.oneButton.setEnabled(false);
                                ticked = -1;
                            } else {
                                if (ticked != -1)
                                    Picasso.with(EventTemplate.this).load(R.drawable.ic_action_unticked).into(Ticks[ticked]);
                                ticked = curr;
                                Picasso.with(EventTemplate.this).load(R.drawable.ic_action_ticked).into(Ticks[ticked]);
                                DA.holder.oneButton.setEnabled(true);
                            }
                        }
                    };
                    LayoutInflater inflater = (LayoutInflater) EventTemplate.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    Button yourOwn = new Button(EventTemplate.this);
                    yourOwn.setLayoutParams(new LinearLayout.LayoutParams(DA.holder.oneButton.getLayoutParams()));
                    yourOwn.setText("or Upload?");
                    yourOwn.setBackgroundResource(R.drawable.button_default_dorange);
                    yourOwn.setTextColor(getResources().getColor(R.color.dorange));
                    ((LinearLayout.LayoutParams) yourOwn.getLayoutParams()).topMargin = 6;
                    for (int i = 0; i < 8; i++) {
                        View CustomImage = inflater.inflate(R.layout.custom_image, null);
                        ImageView img = (ImageView) CustomImage.findViewById(R.id.custom_image_image);
                        ((RelativeLayout.LayoutParams) img.getLayoutParams()).topMargin = 2;
                        ((RelativeLayout.LayoutParams) img.getLayoutParams()).bottomMargin = 2;
                        img.setTag(i);
                        img.setOnClickListener(OCL);
                        img.setBackgroundResource(colors[i]);
                        Picasso.with(EventTemplate.this).load(backgroundsStatic[i]).into(img);
                        Ticks[i] = (ImageView) CustomImage.findViewById(R.id.custom_image_tick);
                        Ticks[i].setTag(i);
                        Ticks[i].setOnClickListener(OCL);
                        DA.add(CustomImage);
                    }
                    DA.add(yourOwn);
                    yourOwn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            internetTools = new InternetTools(EventTemplate.this).setURL(internet.link + "/upload_cover.php");
                            internetTools.UploadImage();
                            LA = null;
                            LA = new LoaderAdapter(EventTemplate.this);
                            LA.setTextColor(getResources().getColor(R.color.dorange));
                            LA.attach(parent);
                            LA.fire();
                            DA.destroy();
                        }
                    });
                }
            });
        }
        {
            String[] seasonFromMonth = {"winter", "winter", "spring", "spring", "spring", "summer", "summer", "summer", "autumn", "autumn", "autumn", "winter"};
            Time tim = new Time();
            tim.set(new Date(event.getS_date()).getTime());
            long eventTime = new Date(event.getS_date()).getTime() / 1000;
            long now = new Date().getTime() / 1000;
            if (eventTime < now)
                timing.setText("passed");
            else if (eventTime < now + 24 * 3600)
                timing.setText("Today");
            else if (eventTime < now + 48 * 3600)
                timing.setText("Tomorrow");
            else if (eventTime < now + 24 * 3600 * 7)
                timing.setText("in " + (eventTime - now) / 24 / 3600 + " days");
            else if (eventTime < now + 24 * 3600 * 14)
                timing.setText("next week");
            else if (eventTime < now + 24 * 3600 * 31)
                timing.setText("in " + (eventTime - now) / 24 / 3600 / 7 + " weeks");
            else
                timing.setText("in " + seasonFromMonth[tim.month]);
        }
        timing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) EventTemplate.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View times = inflater.inflate(R.layout.event_template_dialog_time, null);
                Date date = new Date(event.getS_date());
                Time time = new Time();
                time.set(date.getTime());
                ((TextView) times.findViewById(R.id.ET_start_date)).setText(time.format("%d/%m/%Y"));
                if (event.getE_date() == 0) {
                    times.findViewById(R.id.ET_ends_layout).setVisibility(View.GONE);
                } else {
                    time.set(new Date(event.getE_date()).getTime());
                    ((TextView) times.findViewById(R.id.ET_end_date)).setText(time.format("%d/%m/%Y"));
                }
                ((TextView) times.findViewById(R.id.ET_start_time)).setText(event.getS_time() / 60 + ":" + ((event.getS_time() % 60 > 9) ? event.getS_time() % 60 : "0" + (event.getS_time() % 60)));
                ((TextView) times.findViewById(R.id.ET_end_time)).setText(event.getE_time() / 60 + ":" + ((event.getE_time() % 60 > 9) ? event.getE_time() % 60 : ("0" + event.getE_time() % 60)));
                DA = new DialogAdapter(EventTemplate.this);
                DA.setFancy();
                DA.add(times);
                DA.attach(parent);
                DA.setDestroyable();
                DA.fire();
            }
        });
        if (event.getHangging() > 0) {
            hanggers.setText(event.getHangging() + " " + getString(R.string.are_hanggnig));
            hanggers.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    GH = new GETHANGGERS();
                    GH.execute(username, password, id);
                }
            });
        } else
            hanggers.setText(getString(R.string.hanggers_0));
        if (event.getIsHangging() == 1)
            Hang.setImageResource(R.drawable.imagebutton_cancel);
        Hang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ongoing) return;
                final AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(R.drawable.hangging);
                if (event.getCost() == 0 || event.getIsHangging() == 1) {
                    Hang.setImageDrawable(anim);
                    hang = new HANG();
                    hang.execute(username, password, id);
                    anim.start();
                } else {
                    DA = new DialogAdapter(EventTemplate.this);
                    DA.setFancy();
                    DA.holder.cancel.setVisibility(View.VISIBLE);
                    DA.holder.ok.setTextColor(getResources().getColor(R.color.red));
                    DA.attach(parent);
                    DA.setDestroyable();
                    TextView tv = new TextView(EventTemplate.this);
                    tv.setTextColor(getResources().getColor(R.color.purple));
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setPadding(10, 0, 10, 0);
                    tv.setTextSize(20);
                    tv.setText("this ticket costs " + event.getCost() + " S.P. are you sure you want to get it?");
                    DA.add(tv);
                    DA.fire();
                    DA.holder.ok.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Hang.setImageDrawable(anim);
                            hang = new HANG();
                            hang.execute(username, password, id);
                            anim.start();
                            DA.destroy();
                        }
                    });
                }
            }
        });

        Share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DA = new DialogAdapter(EventTemplate.this);
                DA.setDestroyable();
                DA.showHeadline();
                DA.setHeadlineText("Share via");
                DA.setHeadlineTextColor(colorPrimaryDark);
                DA.setHeadlineDividerColor(color2);
                DA.holder.oneButton.setVisibility(View.GONE);
                DA.setPadding(0, 0, 0, 3);
                DA.attach(parent);

                DA.setText(DialogAdapter.TEXT_CANCEL);

                LayoutInflater inflater = (LayoutInflater) EventTemplate.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.event_template_dialog_share, null);

                Button shareOther = (Button) view1.findViewById(R.id.ET_share_other);
                Button shareGroup = (Button) view1.findViewById(R.id.ET_share_group);
                Button shareFacebook = (Button) view1.findViewById(R.id.ET_share_facebook);

                DesignFunctions.setPuddle(shareOther, colorWhite, colorPrimaryDark);
                DesignFunctions.setPuddle(shareGroup, colorWhite, colorPrimaryDark);
                DesignFunctions.setPuddle(shareFacebook, colorWhite, colorPrimaryDark);

                DA.add(view1);

                DA.fire();
                DA.setDestroyable();

                shareFacebook.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DA.destroy();
                        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
                                    EventTemplate.this)
                                    .setLink(internet.link + "/e.php?id=" + id)
                                    .setCaption(event.getDesc())
                                    .setDescription(event.getDesc())
                                    .setDataErrorsFatal(true)
                                    .setName(event.getHeadline())
                                    .setApplicationName("talat").build();
                            uiHelper.trackPendingDialogCall(shareDialog
                                    .present());
                        } else {
                            if (LN != null)
                                LN.destroy();
                            LN = new LightNotificationAdapter(EventTemplate.this);
                            LN.setTextColor(EventTemplate.this.getResources()
                                    .getColor(R.color.silver1))
                                    .setText(getString(R.string.E_facebook));
                            LN.attach(parent).fire();
                            /*
                            if (session != null && session.isOpened()) {
                                publishFeedDialog();
                            } else {
                                Session.openActiveSession(EventTemplate.this, true, new Session.StatusCallback() {
                                    @Override
                                    public void call(Session session, SessionState state, Exception exception) {
                                        if (session != null && state.isOpened())
                                            publishFeedDialog();
                                    }
                                });
                            }
                            */
                        }
                    }
                });
                shareOther.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DA.destroy();
                        String shareBody = getResources().getString(R.string.check_out) + " " + getResources().getString(R.string.event_link) + event.getId();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, event.getHeadline());
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                    }
                });
            }
        });
        GetNewUpdates GNU = new GetNewUpdates();
        GNU.execute(username, password, event.getId());
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EventTemplate.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap dbl = bitmap;
                                profile.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                                toolbarProfile.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
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
        Picasso.with(EventTemplate.this)
                .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", event.getHost() + ".jpg"))
                .placeholder(R.drawable.ic_user_template)
                .into(target);
        profile.setTag(target);
    }

    void FBSTUFF() {

    }

    private void publishFeedDialog() {

        Bundle params = new Bundle();
        params.putString("name", getString(R.string.app_name));
        params.putString("caption", event.getDesc());
        params.putString("description", event.getDesc());
        params.putString("link", internet.link + "/e.php?id=" + id);
        /*
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, internet.link + "/e.php?id=" + id);
        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fu\\-talat\\.asuscomm\\.com%2Ft%2Fe.php\\%3Did=" + id;
        String sharerUro = "http://www.facebook.com/dialog/feed?app_id=704233486315749"+
        //        "&link=http://u-talat.asuscomm.com/t/e.php?id=" + id +
                "&name=" + event.getHeadline() +
                "&description=" + event.getDesc() +
           //     "&redirect_uri=" + "" +
                "&display=popup";
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUro));
        startActivity(intent);
        */


      /*  session = new Session(EventTemplate.this);
        Session.openActiveSession(EventTemplate.this, true, null);
        session.openForRead(new Session.OpenRequest(EventTemplate.this).setCallback(new SessionCallback()));
      */

        session = Session.getActiveSession();
        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(EventTemplate.this,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {
                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(EventTemplate.this,
                                        "Posted story, id: " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(EventTemplate.this.getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(EventTemplate.this.getApplicationContext(),
                                    "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(EventTemplate.this.getApplicationContext(),
                                    "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LA != null)
            LA.destroy();
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            uiHelper.onActivityResult(requestCode, resultCode, data,
                    new FacebookDialog.Callback() {
                        @Override
                        public void onError(FacebookDialog.PendingCall pendingCall,
                                            Exception error, Bundle data) {
                            Log.e("Activity",
                                    String.format("Error: %s", error.toString()));
                        }

                        @Override
                        public void onComplete(
                                FacebookDialog.PendingCall pendingCall, Bundle data) {
                            Toast.makeText(EventTemplate.this,
                                    R.string.done, Toast.LENGTH_SHORT).show();
                            Log.i("Activity", "Success!");
                        }
                    });
        } else {
            if (internetTools != null && resultCode != 0) {
                ongoingNotification("uploading");
                internetTools.onResult(requestCode, resultCode, data, username, password, id);
                internetTools.setOnUploadedListener(new InternetTools.onUploadListener() {
                    @Override
                    public void onLoaded() {
                        stopOngoingNotification();
                        Intent i = new Intent(EventTemplate.this, EventTemplate.class);
                        i.putExtra("id", id);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError() {
                        if (LN != null)
                            LN.destroy();
                        LN = new LightNotificationAdapter(EventTemplate.this);
                        LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                        LN.attach(parent).fire();
                        stopOngoingNotification();
                    }
                });
            }
        }
        if (session != null)
            session.onActivityResult(this, requestCode, resultCode, data);

    }

    public int ANIMATION_NUM;

    public boolean hideSmallMenu() {
        if (HIDE_MENU) return false;
        HIDE_MENU = true;
        upload.setVisibility(View.INVISIBLE);
        feature.setVisibility(View.INVISIBLE);
        ticketMake.setVisibility(View.INVISIBLE);
        //    statistics.setVisibility(View.INVISIBLE);

        upload.setVisibility(View.VISIBLE);
        feature.setVisibility(View.VISIBLE);
        ticketMake.setVisibility(View.VISIBLE);
        //    statistics.setVisibility(View.VISIBLE);

        Animation slidIn = AnimationUtils.loadAnimation(
                EventTemplate.this, R.anim.slide_out_up);
        slidIn.setDuration(600);
        slidIn.start();
        Animation slideRight = AnimationUtils.loadAnimation(
                EventTemplate.this, R.anim.slide_out_right);
        slideRight.setDuration(500);
        ((LinearLayout) findViewById(R.id.ET_more_options_text)).setLayoutAnimation(new LayoutAnimationController(slideRight, -0.2f));
        ANIMATION_NUM = 0;
        slideRight.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (++ANIMATION_NUM) {
                    //            case 1:
                    //                statistics.setVisibility(View.GONE);
                    //                break;
                    case 2 - 1:
                        ticketMake.setVisibility(View.GONE);
                        break;
                    case 3 - 1:
                        feature.setVisibility(View.GONE);
                        break;
                    case 4 - 1:
                        findViewById(R.id.ET_more_options_con).setVisibility(View.GONE);
                        upload.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return true;
    }

    void setBackGround(int coverID, boolean FORCE) {
        Log.d("eventTemplate", "settingBackGround, new coverID:" + coverID + ", force:" + FORCE);
        if (coverID < 0) {
            InternetTools IT = new InternetTools(this);
            IT.getImage(cover, internet.link + "/events/" + event.getId() + "/cover.png", Environment.getExternalStorageDirectory()
                    + File.separator + ".Talat", "event_" + event.getId() + ".jpg", R.drawable.concert_0, FORCE);
            Log.d("eventTemaplate", "loadingstarted with Force:" + FORCE);
            IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                @Override
                public void onLoaded(Bitmap bitmap) {
                    Log.d("eventTemaplate", "loaded a new");
                    EventTemplate.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(EventTemplate.this).load(new File(Environment.getExternalStorageDirectory()
                                    + File.separator + ".Talat", "event_" + event.getId() + ".jpg")).into(cover);
                        }
                    });

                }

                @Override
                public void onError() {
                    Log.d("eventTemaplate", "error");
                }
            });
            return;
        }
        final AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(backgrounds[coverID]);
        Runnable r = new Runnable() {
            public void run() {
                cover.setImageDrawable(anim);
                anim.start();
            }
        };
        final Handler h = new Handler();
        h.post(r);
    }

    private void share(Session session) {
        Facebook facebook = new Facebook(session.getApplicationId());
        facebook.setSession(session);
        facebook.dialog(this, "feed", null);
        pendingShare = false;
    }

    private class GetNewUpdates extends AsyncTask<String, Integer, Integer> {
        String username, password, id, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            id = params[2];
            internet i = new internet();
            resp = i.getUpdates(username, password, id);
            if (resp == null)
                return -3707;
            return 1;
        }

        int userIndexIterator;
        ProfileQL PQL;

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 1:
                    final String[] bits = resp.split("\\<BR \\/\\>");
                    for (int i = 0; i < bits.length; i += 2) {
                        if ("cover".equals(bits[i])) {
                            int coverID = Integer.parseInt(bits[i + 1]);
                            Log.d("eventTemplate", "updateCover, new coverID:" + coverID + ", old coverID:" + event.getFeatured());
                            if (event.getFeatured() == coverID) continue;
                            event.setFeatured(coverID);
                            SQlite sql = new SQlite(EventTemplate.this);
                            sql.updateEvent(event);
                            sql.close();
                            setBackGround(coverID, true);
                        }
                        if ("user".equals(bits[i])) {
                            userIndexIterator = i + 1;
                            PQL = new ProfileQL(EventTemplate.this);
                            String pic = PQL.getPic(event.getHost());
                            Log.d("eventTemplate", "updatePic, new PicID:" + bits[i + 1] + ", old ID:" + pic);
                            if (!PQL.checkProfile(event.getHost()) || (pic != null && bits[i + 1].compareTo(pic) != 0)) {
                                InternetTools IT = new InternetTools(EventTemplate.this);
                                IT.getImage(profile, internet.link + "/users/" + event.getHost() + "/profilePic.png",
                                        Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                                        event.getHost() + ".jpg", R.drawable.ic_user_template, true);
                                IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                                    @Override
                                    public void onLoaded(Bitmap bitmap) {
                                        EventTemplate.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Target target = new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ((Activity) EventTemplate.this).runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Bitmap dbl = bitmap;
                                                                        profile.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
                                                                        toolbarProfile.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(dbl, 4));
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
                                                Picasso.with(EventTemplate.this)
                                                        .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", event.getHost() + ".jpg"))
                                                        .placeholder(R.drawable.ic_user_template)
                                                        .into(target);
                                                profile.setTag(target);
                                                PQL = new ProfileQL(EventTemplate.this);
                                                PQL.addProfile(event.getHost(), PQL.getAbot(event.getHost()), bits[userIndexIterator]);
                                                PQL = null;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                            }
                        }
                    }
                    PQL = null;
                    break;
            }
            cancel(false);
        }
    }

    private class Fetch extends AsyncTask<String, Integer, Integer> {
        String username, password, id;
        Event resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            id = params[2];
            internet i = new internet();
            resp = i.fetch_event(username, password, id);
            if (resp == null)
                return -3707;
            return 1;
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(EventTemplate.this);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(parent);
            LA.fire();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case 1:
                    SQlite sql = new SQlite(EventTemplate.this);
                    sql.addEvent(resp);
                    Intent in = new Intent(EventTemplate.this, EventTemplate.class);
                    in.putExtra("id", id);
                    startActivity(in);
                    finish();
                    break;
                default://TODO
                    DialogAdapter dialogAdapter = new DialogAdapter(EventTemplate.this);
                    dialogAdapter.setFancy();
                    TextView errorView = new TextView(EventTemplate.this);
                    errorView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    errorView.setGravity(Gravity.CENTER);
                    errorView.setText(getString(R.string.E_unknown));
                    dialogAdapter.setOnDialogDestroyedListener(new DialogAdapter.OnDialogDestroyedListener() {
                        @Override
                        public void onDestroyed() {
                            finish();
                        }
                    });
                    dialogAdapter.add(errorView);
                    dialogAdapter.attach(parent);
                    dialogAdapter.fire();
            }
            cancel(false);
        }
    }

    private class HANG extends AsyncTask<String, Integer, Integer> {
        String username, password, id, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            id = params[2];
            internet i = new internet();
            resp = i.hang(username, password, id);
            if (resp.equals("error"))
                return -3707;
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPostExecute(final Integer result) {
            ongoing = false;
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    break;
                case 1:
                    SQlite sql = new SQlite(EventTemplate.this);
                    if (event.getIsHangging() == 1)
                        event.setIsHangging(0);
                    else
                        event.setIsHangging(1);
                    sql.updateEvent(event);
                    break;
                case 201:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_cost_hang));
                    LN.attach(parent).fire();
                    break;
                case 202:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_no_backs));
                    LN.attach(parent).fire();
                    break;
                case 606:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_not_logged));
                    LN.attach(parent).fire();
                    break;
                case 612:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_no_tickets));
                    LN.attach(parent).fire();
                    break;
                default://TODO
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_unknown));
                    LN.attach(parent).fire();
            }
            if (event.getIsHangging() == 0) {
                if (event.getCost() == 0)
                    Hang.setImageResource(R.drawable.imagebutton_hang);
                else
                    Hang.setImageResource(R.drawable.imagebutton_hang_paid);
            } else {
                Hang.setImageResource(R.drawable.imagebutton_cancel);
            }
            cancel(false);
        }
    }

    private class updateCover extends AsyncTask<String, Integer, Integer> {
        String username, password, id, cover, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            id = params[2];
            cover = params[3];
            internet i = new internet();
            resp = i.updateCover(username, password, id, cover);
            if (resp.equals("error"))
                return -3707;
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(EventTemplate.this);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(parent);
            LA.fire();
            DA.destroy();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    break;
                case 1:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.done));
                    LN.attach(parent).fire();
                    SQlite sql = new SQlite(EventTemplate.this);
                    event.setFeatured(Integer.parseInt(cover));
                    sql.updateEvent(event);
                    Picasso.with(EventTemplate.this).load(backgroundsStatic[Integer.parseInt(cover)]).into(EventTemplate.this.cover);
                    break;
                default://TODO
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_unknown));
                    LN.attach(parent).fire();
            }
            cancel(false);
        }
    }

    private class GETHANGGERS extends AsyncTask<String, Integer, Integer> {
        String username, password, eventID, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            eventID = params[2];
            internet i = new internet();
            resp = i.getHanggers(username, password, id);
            if (resp.equals("error"))
                return -3707;
            return 1;
        }

        @Override
        protected void onPreExecute() {
            DA = new DialogAdapter(EventTemplate.this);
            DA.holder.oneButton.setVisibility(View.GONE);
            DA.attach(parent);
            DA.setDestroyable();
            DA.showHeadline();
            DA.setHeadlineText(getString(R.string.loading));
            DA.setHeadlineTextColor(colorPrimaryDark);
            DA.setPadding(0, 0, 0, 8);

            TextView tvv = new TextView(EventTemplate.this);
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
            ongoing = false;
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(EventTemplate.this);
                    LN.setTextColor(EventTemplate.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    DA.destroy();
                    break;
                case 1:
                    DA.holder.body.removeAllViews();
                    final String[] people = resp.split("\\<BR \\/\\>");
                    DA.setHeadlineText(getString(R.string.hanggers));

                    LayoutInflater inflater = (LayoutInflater) EventTemplate.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    for (int i = 0; i < people.length; i++) {
                        View container = inflater.inflate(R.layout.profile_mini, null);
                        final ImageView pic = (ImageView) container.findViewById(R.id.profile_mini_pic);
                        ((TextView) container.findViewById(R.id.profile_mini_username)).setText(people[i]);
                        if (i == people.length - 1)
                            container.findViewById(R.id.profile_mini_indecator).setVisibility(View.GONE);
                        if (username.equals(people[i]))
                            container.findViewById(R.id.profile_mini_follow).setVisibility(View.GONE);
                        container.setTag(people[i]);
                        pic.setTag(people[i]);
                        DesignFunctions.setPuddle(container, colorWhite, colorPrimaryDark);
                        container.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(EventTemplate.this, Profile.class);
                                i.putExtra("id", view.getTag().toString());
                                startActivity(i);
                            }
                        });
                        DA.add(container);
                        InternetTools IT = new InternetTools(EventTemplate.this);
                        IT.getImage(pic, internet.link + "/users/" + people[i] + "/profilePic.png",
                                Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                                people[i] + ".jpg", R.drawable.ic_user_template, false);
                        IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                            @Override
                            public void onLoaded(final Bitmap bitmap) {
                                Log.d("S", "onLoaded");
                                EventTemplate.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(bitmap, 4));
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                                EventTemplate.this.runOnUiThread(new Runnable() {
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

    void ongoingNotification(String title) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "Talat";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, title, "", null);
        mNotificationManager.notify(11235813, notification);
    }

    void stopOngoingNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        mNotificationManager.cancel(11235813);
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
            if (!hideSmallMenu())
                super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            uiHelper.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("pendingShare", pendingShare);
        Session.saveSession(session, outState);
    }


    class SessionCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened() && pendingShare) {
                share(session);
            }
        }
    }
}