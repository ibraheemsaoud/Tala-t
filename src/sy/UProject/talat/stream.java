package sy.UProject.talat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sy.UProject.UI.AnimationFunctions;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.LoaderAdapter;

public class stream {
    public LightNotificationAdapter LN;
    public LoaderAdapter LA;
    public LinearLayout linearlinearParent;
    public String username, password, extra1 = "", extra2 = "";
    public Context context;
    public RelativeLayout parent;

    private View view, updater;//, lastChild;
    private Stream stream;
    private static String FORCE;
    private static int START = 0, END = 7, STEP = 7;
    private List<EventAdapter> Events;
    private boolean ongoing = false;

    public stream() {
    }

    public void show_error(String text) {
        LN = new LightNotificationAdapter(context);
        LN.setTextColor(context.getResources().getColor(R.color.silver1)).setText(text);
        LN.attach(parent).fire();
    }

    public void doAll(Context context, RelativeLayout parent, String link) {
        START = 0;
        END = STEP;
        FORCE = "1";
        this.context = context;
        this.parent = parent;
        Events = new ArrayList<EventAdapter>();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_stream, null, false);
        updater = view.findViewById(R.id.stream_loader);
        linearlinearParent = (LinearLayout) view.findViewById(R.id.stream_wrapper);
        final ScrollView scroll = (ScrollView) view.findViewById(R.id.stream_scroll);
        final TextView updaterText = (TextView) view.findViewById(R.id.stream_loader_text);

        AnimationSet set = new AnimationSet(true);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom_twist);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(1000);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.2f);
        //    controller.
        linearlinearParent.setLayoutAnimation(controller);

        Animation up = AnimationUtils.loadAnimation(
                context, R.anim.slide_in_bottom_twist);
        up.setDuration(1000);

        //  AnimationFunctions.firstExpand(updater);
        stream = new Stream();
        stream.execute(username, password, link);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view1, MotionEvent motionEvent) {
                if (ongoing)
                    return false;
                Integer diff = (view.getBottom() - (view.findViewById(
                        R.id.stream_parent).getHeight() - scroll.getScrollY()));
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_HOVER_EXIT:
                        ;
                    case MotionEvent.ACTION_OUTSIDE:
                        AnimationFunctions.collapse(updater, View.GONE);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (diff != 0 && updater.getVisibility() == View.VISIBLE) {
                            AnimationFunctions.collapse(updater, View.GONE);
                        } else if (diff == 0
                                && updater.getVisibility() == View.VISIBLE) {
                            updaterText.setText(stream.this.context.getString(R.string.fetching));
                            stream = new Stream();
                            stream.execute(username, password);
                            ongoing = true;
                        }
                        break;
                    default:
                        if (diff == 0 && (updater.getVisibility() == View.GONE || updater.getVisibility() == View.INVISIBLE)) {
                            updater.setVisibility(View.VISIBLE);
                        } else if (diff == 0)
                            updaterText.setText(stream.this.context.getResources().getString(R.string.release));
                        else if (updater.getVisibility() != View.GONE && updater.getVisibility() != View.INVISIBLE)
                            updaterText.setText(stream.this.context.getResources().getString(R.string.pull));
                }
                return false;
            }
        });
        parent.addView(view);
    }

    private class Stream extends AsyncTask<String, Integer, Integer> {
        String username, password, result, link;
        String[] events;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            link = params[2];

            internet i = new internet();
            String resp = i.stream(username, password, String.valueOf(START), String.valueOf(END), FORCE, link, extra1, extra2);
            if ("error".equals(resp))
                return -3707;
            if (" ".equals(resp) || "".equals(resp) || resp.length() == 0)
                return 406;
            if ("602".equals(resp))
                return 602;
            if (!resp.contains("<BR />"))
                return Integer.parseInt(resp);
            events = resp.split("\\<BR \\/\\>");
            int size = events.length;
            if (size == 0)
                resp = "406";
            for (int j = 0; j < size; j++) {
                String[] splits = events[j].split("\\<SPC\\/\\>");
                String id = splits[0];
                SQlite sql = new SQlite(context);
                Event event = sql.getEvent(id);
                if (event != null) {
                    if (splits.length > 1)
                        event.setHangging(Integer.parseInt(splits[1]));
                    if (splits.length > 2)
                        event.setFeatured(Integer.parseInt(splits[2]));
                    sql.updateEvent(event);
                    result += id + "<BR />";
                } else {
                    Event e = i.fetch_event(username, password, id);
                    if (e != null) {
                        sql.addEvent(e);
                        result += id + "<BR />";
                    }
                }
                resp = "1";
            }
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            ongoing = true;
            if ("1".equals(FORCE)) {
                linearlinearParent.removeAllViews();
                LA = null;
                LA = new LoaderAdapter(context);
                LA.setAnimated();
                LA.setAnimatedBackgroundColor(context.getResources().getColor(R.color.dorange));
                LA.attach(parent);
                LA.fire();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            if (!"1".equals(FORCE))
                AnimationFunctions.collapse(updater, View.GONE);
            switch (result) {
                case -3707:
                    show_error(context.getString(R.string.E_internet));
                    break;
                case 602:
                    show_error(context.getString(R.string.E_hangouts));
                    break;
                case 406:
                    if ("1".equals(FORCE))
                        show_error(context.getString(R.string.E_no_stream));
                    break;
                case 1:
                    int size = events.length;
                    for (int j = 0; j < size; j++) {
                        String id = events[j].split("\\<SPC\\/\\>")[0];
                        SQlite sql = new SQlite(context);
                        Event e = sql.getEvent(id);
                        if (e == null) {
                            Log.e("error", "event not fetched correctly" + id);
                            continue;
                        }
                        Events.add(new EventAdapter(context, e).attach(linearlinearParent));
                    }
                    AnimationSet set = new AnimationSet(true);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom_twist);
                    set.addAnimation(animation);
                    animation = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
                    );
                    animation.setDuration(1000);
                    set.addAnimation(animation);
                    if ("1".equals(FORCE))
                        linearlinearParent.setLayoutAnimation(new LayoutAnimationController(set, 0.2f));
                    START += STEP;
                    END += STEP;
                    FORCE = "";
                    break;
                default:
                    show_error(context.getString(R.string.E_unknown));
            }
            ongoing = false;
            cancel(false);
        }
    }
}