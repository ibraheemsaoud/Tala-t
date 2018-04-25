package sy.UProject.talat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sy.UProject.UI.AnimationFunctions;
import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.LoaderAdapter;

public class FragmentEManager extends Fragment {
    static String username, password;
    public ImageView button;
    public DialogAdapter[] NewEvent;
    public LightNotificationAdapter LN;
    public DialogAdapter DA;
    public LoaderAdapter LA;
    private CreateEvent createevent;
    private Stream stream;
    private LinearLayout parent;
    private Rect rect;
    private String section_num, regions_sumed, STIME, ETIME, SDATE, EDATE;
    private String[] regions_sum = new String[4];
    private int SIZE = 6;
    private int[] colorNames;

    public FragmentEManager() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manager, container, false);
        colorNames = new int [8];
        colorNames[0] = getResources().getColor(R.color.blue2);
        colorNames[1] = getResources().getColor(R.color.red2);
        colorNames[2] = getResources().getColor(R.color.red2);
        colorNames[3] = getResources().getColor(R.color.green2);
        colorNames[4] = getResources().getColor(R.color.orange2);
        colorNames[5] = getResources().getColor(R.color.dblue2);
        colorNames[6] = getResources().getColor(R.color.yellow2);
        colorNames[7] = getResources().getColor(R.color.silver2);
        AnimationSet set = new AnimationSet(true);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.context, R.anim.slide_in_bottom_twist);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(1000);
        set.addAnimation(animation);
        parent = (LinearLayout) view.findViewById(R.id.manager_wrapper);
        parent.setLayoutAnimation(new LayoutAnimationController(set, 0.2f));
        stream = new Stream();
        stream.execute(MainActivity.username, MainActivity.password);
        button = (ImageView) view.findViewById(R.id.manager_add);
        final ImageView defaultActionButton = MainActivity.defaultActionButton;
        Animation anim;
        AnimationSet AS = new AnimationSet(true);
        if (defaultActionButton.getVisibility() == View.INVISIBLE && !defaultActionButton.getTag().equals("1")) {
            anim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            AS.addAnimation(anim);
            anim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            anim.setStartOffset(200);
            anim.setDuration(300);
            AS.addAnimation(anim);
        } else {
            anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_SELF, 0);
            anim.setDuration(500);
            AS.addAnimation(anim);
            if (defaultActionButton.getTag().equals("1")) {
                anim = AnimationUtils.loadAnimation(MainActivity.context,
                        R.anim.grow_out);
                anim.setDuration(500);
                AS.addAnimation(anim);
            }
        }
        button.startAnimation(AS);
        defaultActionButton.setTag("0");
        defaultActionButton.setBackgroundResource(R.drawable.imagebutton_add);
        MainActivity.extraActionButton.setBackgroundResource(R.drawable.imagebutton_add);
        //    defaultActionButton.setImageResource(R.drawable.ic_action_add_circle);
        defaultActionButton.setVisibility(View.INVISIBLE);
        DesignFunctions.setPoggy(button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation circleInPlace = AnimationUtils.loadAnimation(MainActivity.context,
                                R.anim.circle_in_place);
                        circleInPlace.setRepeatCount(1000);
                        circleInPlace.setRepeatMode(Animation.REVERSE);
                        circleInPlace.setDuration(550);
                        button.startAnimation(circleInPlace);
                        rect = null;
                        rect = new Rect(button.getLeft(), button.getTop(), button.getRight(), button.getBottom());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!rect.contains(button.getLeft() + (int) motionEvent.getX(), button.getTop() + (int) motionEvent.getY()))
                            button.setAnimation(null);
                        break;
                    default:
                        button.setAnimation(null);
                }
                return false;
            }
        });
        button.setOnClickListener(new OnClickListener() {
            EditText headline
                    ,
                    desc;
            EditText sDate
                    ,
                    eDate
                    ,
                    sTime
                    ,
                    eTime
                    ,
                    price, seats;
            Button[] sections = new Button[8];
            CheckBox[] regions = new CheckBox[4];
            int[] sections_names = {R.id.EM_section0, R.id.EM_section1,
                    R.id.EM_section2, R.id.EM_section3, R.id.EM_section4,
                    R.id.EM_section5, R.id.EM_section6, R.id.EM_section7}
                    ,
                    region_names = {R.id.EM_region0, R.id.EM_region1,
                            R.id.EM_region2, R.id.EM_region3};
            int region_count = 0;

            @Override
            public void onClick(View arg0) {
                final Context context = arg0.getContext();
                Animation die = AnimationUtils.loadAnimation(context,
                        R.anim.circle_clockwise_out);
                die.setDuration(350);
                arg0.startAnimation(die);
                arg0.setVisibility(View.GONE);
                if (NewEvent != null && NewEvent[0].getHolded()) {
                    NewEvent[0].setCurrentSlide(0);
                    NewEvent[0].fire();
                    return;
                }
                final View view1 = ((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.manager_dialogs,
                                null), view2 = ((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.manager_dialogs_time,
                                null), view3 = ((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.manager_dialogs_section,
                                null), view4 = ((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.manager_dialogs_regions,
                                null), view5 = ((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.manager_dialogs_ticket,
                                null);
                NewEvent = new DialogAdapter[SIZE];
                DialogAdapter.initForm(NewEvent, MainActivity.parent);
                NewEvent[0].add(view1);
                NewEvent[1].add(view2);
                NewEvent[2].add(view3);
                NewEvent[3].add(view4);
                NewEvent[4].add(view5);
                headline = (EditText) view1.findViewById(R.id.EM_headline);
                desc = (EditText) view1.findViewById(R.id.EM_desc);
                sDate = (EditText) view2.findViewById(R.id.EM_date_start);
                eDate = (EditText) view2.findViewById(R.id.EM_date_end);
                sTime = (EditText) view2.findViewById(R.id.EM_time_start);
                eTime = (EditText) view2.findViewById(R.id.EM_time_end);
                price = (EditText) view5.findViewById(R.id.EM_ticket_price);
                seats = (EditText) view5.findViewById((R.id.EM_ticket_seats));
                final View Extended = view2.findViewById(R.id.manager_extended_date_end);
                AnimationFunctions.firstExpand(Extended, View.INVISIBLE);
                view2.findViewById(R.id.manager_extender_date_end).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((TextView) view).getText().toString().equals(getString(R.string.more))) {
                            ((TextView) view).setText(getString(R.string.less));
                            Extended.setVisibility(View.VISIBLE);
                            AnimationFunctions.expand(Extended);
                        } else {
                            ((TextView) view).setText(getString(R.string.more));
                            AnimationFunctions.collapse(Extended, View.INVISIBLE);
                            //		Extended.setLayoutParams(view2.findViewById(R.id.manager_date_start).getLayoutParams());
                        }
                    }
                });
                NewEvent[2].holder.next.setVisibility(View.GONE);
                for (int i = 0; i < 8; i++) {
                    sections[i] = (Button) view3
                            .findViewById(sections_names[i]);
                    DesignFunctions.setPuddle(sections[i], getResources().getColor(
                            R.color.white), colorNames[i]);
                    sections[i].setTag(String.valueOf(i+1));
                    sections[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View arg0) {
                            section_num = (String) arg0.getTag();
                            NewEvent[2].onNextPressed(NewEvent, 2);
                        }
                    });
                }
                for (int i = 0; i < 4; i++) {
                    regions[i] = (CheckBox) view4.findViewById(region_names[i]);
                    view4.findViewById(region_names[i]).setTag(i);
                    DesignFunctions.setPuddle(regions[i], getResources().getColor(
                            R.color.white), getResources().getColor(
                            R.color.dorange2));
                    regions_sum[i] = "0";
                    regions[i].setOnClickListener(new OnClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(final View arg0) {
                            Integer i = (Integer) arg0.getTag();
                            if (((CheckBox) arg0).isChecked()) {
                                if (region_count == 0)
                                    NewEvent[3].holder.next.setEnabled(true);
                                region_count++;
                                regions_sum[i] = "1";
                            } else {
                                if (region_count == 0)
                                    NewEvent[3].holder.next.setEnabled(false);
                                region_count--;
                                regions_sum[i] = "0";
                            }
                        }
                    });
                }
                price.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        if (price.getText().toString().length() == 0) {
                            seats.setVisibility(View.INVISIBLE);
                            ((TextView)view5.findViewById(R.id.EM_ticket_explain)).setText(MainActivity.context.getString(R.string.ticket_explaination));
                        } else {
                            seats.setText("");
                            seats.setVisibility(View.VISIBLE);
                            ((TextView)view5.findViewById(R.id.EM_ticket_explain)).setText(MainActivity.context.getString(R.string.seats_explaination));
                        }
                    }

                    public void beforeTextChanged(CharSequence s,
                                                  int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                    }
                });
                NewEvent[0].holder.next
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                boolean ERROR = false;
                                if (headline.getText().toString().length() == 0) {
                                    headline.setError(getString(R.string.E_headline));
                                    ERROR = true;
                                }
                                if (desc.getText().toString().length() == 0) {
                                    desc.setError(getString(R.string.E_desc));
                                    ERROR = true;
                                }
                                if (ERROR)
                                    return;
                                NewEvent[0].onNextPressed(NewEvent, 0);
                            }
                        });
                NewEvent[1].holder.next
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                boolean ERROR = false;
                                if (sDate.getText().toString().length() == 0) {
                                    sDate.setError(getString(R.string.E_date));
                                    ERROR = true;
                                }
                                if (eDate.getText().toString().length() == 0) {
                                    eDate.setError(getString(R.string.E_date));
                                }
                                if (sTime.getText().toString().length() == 0) {
                                    sTime.setError(getString(R.string.E_time));
                                    ERROR = true;
                                }
                                if (eTime.getText().toString().length() == 0) {
                                    eTime.setError(getString(R.string.E_time));
                                    ERROR = true;
                                }
                                if (ERROR)
                                    return;
                                String[] times;
                                DateValidator dv = new DateValidator();
                                sDate.setText(sDate.getText().toString().replaceAll("\\-", "/"));
                                sDate.setText(sDate.getText().toString().replaceAll("\\_", "/"));
                                sDate.setText(sDate.getText().toString().replaceAll("\\.", "/"));
                                eDate.setText(eDate.getText().toString().replaceAll("\\-", "/"));
                                eDate.setText(eDate.getText().toString().replaceAll("\\_", "/"));
                                eDate.setText(eDate.getText().toString().replaceAll("\\.", "/"));
                                if (!dv.validate(sDate.getText().toString())) {
                                    sDate.setError(getString(R.string.E_date_format));
                                    ERROR = true;
                                } else {
                                    times = sDate.getText().toString().split("\\/");
                                //    Time time = new Time();
                                //    time.set(0,0,0,Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
                                    final Calendar cal = Calendar.getInstance();
                                    cal.set(Integer.parseInt(times[2]), Integer.parseInt(times[1]) - 1, Integer.parseInt(times[0]), 0, 0, 0);
                                    long secondsSinceEpoch = cal.getTimeInMillis();
                                    SDATE = secondsSinceEpoch + "";
                                }
                                if (eDate.getText().toString().length() == 0 || !dv.validate(eDate.getText().toString())) {
                                    eDate.setError(getString(R.string.E_date_format));
                                    if (((Button) view2.findViewById(R.id.manager_extender_date_end)).getText() == getResources().getString(R.string.less))
                                        ERROR = true;
                                    EDATE = "0";
                                } else {
                                    times = eDate.getText().toString().split("\\/");
                                    final Calendar cal = Calendar.getInstance();
                                    cal.set(Integer.parseInt(times[2]), Integer.parseInt(times[1]) - 1, Integer.parseInt(times[0]), 0, 0, 0);
                                    long secondsSinceEpoch = cal.getTimeInMillis();
                                    EDATE = secondsSinceEpoch + "";
                                }
                                String result = sTime.getText().toString();
                                times = result.split(":");
                                if (times.length != 2 || !isInteger(times[0]) || Integer.parseInt(times[0]) > 23 || !isInteger(times[1]) || Integer.parseInt(times[1]) > 59) {
                                    sTime.setError(getString(R.string.E_time_format));
                                    ERROR = true;
                                } else {
                                    STIME = String.valueOf(Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]));
                                }
                                result = eTime.getText().toString();
                                times = result.split(":");
                                if (times.length != 2 || !isInteger(times[0]) || Integer.parseInt(times[0]) > 23 || !isInteger(times[1]) || Integer.parseInt(times[1]) > 59) {
                                    eTime.setError(getString(R.string.E_time_format));
                                    ERROR = true;
                                }
                                if (ERROR)
                                    return;
                                ETIME = String.valueOf(Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]));
                                InputMethodManager imm = (InputMethodManager) context
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(
                                        NewEvent[1].holder.body.getChildAt(0)
                                                .getWindowToken(), 0);
                                NewEvent[1].onNextPressed(NewEvent, 1);
                            }
                        });
                NewEvent[4].holder.next
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                InputMethodManager imm = (InputMethodManager) context
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(
                                        NewEvent[4].holder.body.getChildAt(0)
                                                .getWindowToken(), 0);
                                NewEvent[4].onNextPressed(NewEvent, 4);
                            }
                        });
                NewEvent[5].holder.next
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                createevent = new CreateEvent();
                                regions_sumed = "";
                                for (int i = 0; i < 4; i++)
                                    regions_sumed += regions_sum[i];
                                createevent.execute(MainActivity.username, MainActivity.password, headline.getText().toString(), desc.getText().toString()
                                        , section_num, regions_sumed, "public", price.getText().toString(), SDATE, EDATE, STIME, ETIME, seats.getText().toString());
                            }
                        });
                NewEvent[0].setInMovement(DialogAdapter.SLIDE_RIGHT);
                NewEvent[0].setOnDialogDestroyedListener(new DialogAdapter.OnDialogDestroyedListener() {
                    @Override
                    public void onDestroyed() {
                        button.setVisibility(View.VISIBLE);
                        Animation live = AnimationUtils.loadAnimation(context,
                                R.anim.circle_anticlockwise_in);
                        live.setDuration(250);
                        button.startAnimation(live);
                    }
                });
                NewEvent[0].fire();
            }
        });

        return view;
    }

    public class DateValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String DATE_PATTERN =
                "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

        public DateValidator() {
            pattern = Pattern.compile(DATE_PATTERN);
        }
        public boolean validate(final String date) {

            matcher = pattern.matcher(date);

            if (matcher.matches()) {

                matcher.reset();

                if (matcher.find()) {

                    String day = matcher.group(1);
                    String month = matcher.group(2);
                    int year = Integer.parseInt(matcher.group(3));

                    if (day.equals("31") &&
                            (month.equals("4") || month.equals("6") || month.equals("9") ||
                                    month.equals("11") || month.equals("04") || month.equals("06") ||
                                    month.equals("09"))) {
                        return false; // only 1,3,5,7,8,10,12 has 31 days
                    } else if (month.equals("2") || month.equals("02")) {
                        //leap year
                        if (year % 4 == 0) {
                            if (day.equals("30") || day.equals("31")) {
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            if (day.equals("29") || day.equals("30") || day.equals("31")) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private class Stream extends AsyncTask<String, Integer, Integer> {
        String username, password, result;
        String[] events;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];

            internet i = new internet();
            String resp = i.manager(username, password);
            Log.v("Internet Connection", "Doing - streaming - " + resp);
            if ("error".equals(resp))
                return -3707;
            if (" ".equals(resp) || "".equals(resp) || resp.length() == 0)
                return 406;
            events = resp.split("\\<BR \\/\\>");
            int size = events.length;
            for (int j = 0; j < size; j++) {
                String id = events[j].split("\\<SPC\\/\\>")[0];
                Event event = MainActivity.sql.getEvent(id);
                if (event != null) {
                    event.setHangging(Integer.parseInt(events[j].split("\\<SPC\\/\\>")[1]));
                    MainActivity.sql.updateEvent(event);
                    result += id + "<BR />";
                } else {
                    Event e = i.fetch_event(username, password, id);
                    if (e != null) {
                        MainActivity.sql.addEvent(e);
                        result += id + "<BR />";
                    }
                }
                resp = "1";
            }
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    MainActivity.show_error(getString(R.string.E_internet));
                    break;
                case 406:
                    MainActivity.show_error(getString(R.string.E_no_manager));
                    break;
                case 1:
                    int size = events.length;
                    for (int j = 0; j < size; j++) {
                        String id = events[j].split("\\<SPC\\/\\>")[0];
                        Event e = MainActivity.sql.getEvent(id);
                        if (e == null) {
                            Log.e("error", "event not fetched correctly");
                            continue;
                        }
                        new EventAdapter(MainActivity.context, e).attach(parent);
                    }
                    AnimationSet set = new AnimationSet(true);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.context, R.anim.slide_in_bottom_twist);
                    set.addAnimation(animation);
                    animation = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
                    );
                    animation.setDuration(1000);
                    set.addAnimation(animation);
                    parent.setLayoutAnimation(new LayoutAnimationController(set, 0.2f));
                    break;
                default:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    private class CreateEvent extends AsyncTask<String, Integer, Integer> {
        String username, password, headline, desc, section, regions, group, cost, sDate, eDate, sTime, eTime, id, seats;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];

            headline = params[2];
            desc = params[3];
            section = params[4];
            regions = params[5];
            group = params[6];
            cost = params[7];
            sDate = params[8];
            eDate = params[9];
            sTime = params[10];
            eTime = params[11];
            seats = params[12];

            internet i = new internet();
            String resp = i.createEvent(username, password, headline, desc, section, regions, group, cost, sDate, eDate, sTime, eTime, seats);
            Log.v("Internet Connection", "Doing - streaming - " + resp);
            if ("error".equals(resp))
                return -3707;
            if (resp.startsWith("<id>")) {
                id = resp.replaceFirst("\\<id\\>", "");
                resp = "1";
            }
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            NewEvent[0].hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    MainActivity.show_error(getString(R.string.E_internet));
                    NewEvent[0].fire();
                    break;
                case 231:
                    MainActivity.show_error(getString(R.string.E_cost));
                    NewEvent[0].fire();
                    break;
                case 1:
                    Intent i = new Intent(MainActivity.context, EventTemplate.class);
                    i.putExtra("id", id);
                    startActivity(i);
                    NewEvent[0].destroy();
                    break;
                default:
                    NewEvent[0].fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        if (stream != null) stream.cancel(true);
        if (createevent != null) createevent.cancel(true);
        if (LA != null) LA.destroy();
        if (NewEvent != null && NewEvent[0] != null) NewEvent[0].destroy();
        super.onDestroy();
    }
}