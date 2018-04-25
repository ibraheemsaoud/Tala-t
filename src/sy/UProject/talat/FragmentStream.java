package sy.UProject.talat;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sy.UProject.UI.AnimationFunctions;
import sy.UProject.UI.LoaderAdapter;

public class FragmentStream extends Fragment {
    public LoaderAdapter LA;
    public LinearLayout parent;

    private View view, updater;//, lastChild;
    private Stream stream;
    private static String FORCE;
    private static int CARD_COUNT, START = 0, END = 7, STEP = 7;
    private List<EventAdapter> Events;
    private boolean ongoing = false;

    public FragmentStream() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        START = 0;
        END = STEP;
        FORCE = "1";
        Events = new ArrayList<EventAdapter>();

        view = inflater.inflate(R.layout.stream, container, false);
        updater = view.findViewById(R.id.stream_loader);
        parent = (LinearLayout) view.findViewById(R.id.stream_wrapper);
        final ScrollView scroll = (ScrollView) view.findViewById(R.id.mainScroll);
        final TextView updaterText = (TextView) view.findViewById(R.id.stream_loader_text);
        if (MainActivity.defaultActionButton.getVisibility() == View.INVISIBLE) {
            if (MainActivity.defaultActionButton.getTag().equals("1")) {
                AnimationSet AS = new AnimationSet(false);
                Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
                anim.setDuration(250);
                AS.addAnimation(anim);
                anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                anim.setStartOffset(250);
                anim.setDuration(250);
                AS.addAnimation(anim);
                if (MainActivity.defaultActionButton.getTag().equals("1")) {
                    anim = AnimationUtils.loadAnimation(MainActivity.context,
                            R.anim.grow_out);
                    anim.setDuration(500);
                    AS.addAnimation(anim);
                }
                MainActivity.defaultActionButton.startAnimation(AS);
            } else {
                AnimationSet AS = new AnimationSet(false);
                Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_SELF, 0);
                anim.setDuration(500);
                AS.addAnimation(anim);
                anim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(200);
                //    AS.addAnimation(anim);
                anim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                anim.setStartOffset(200);
                anim.setDuration(300);
                //    AS.addAnimation(anim);
                MainActivity.defaultActionButton.startAnimation(AS);
            }
        }
        MainActivity.defaultActionButton.setVisibility(View.VISIBLE);
        MainActivity.defaultActionButton.setTag("0");
        MainActivity.defaultActionButton.setBackgroundResource(R.drawable.imagebutton_logo);
        MainActivity.defaultActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ongoing) return;
                final AnimationDrawable anim = (AnimationDrawable) getResources().getDrawable(R.drawable.walking);
                MainActivity.defaultActionButton.setImageDrawable(anim);
                parent.removeAllViews();
                START = 0;
                END = STEP;
                FORCE = "1";
                AnimationSet set = new AnimationSet(true);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.context, R.anim.slide_in_bottom_twist);
                set.addAnimation(animation);
                animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
                );
                animation.setDuration(1000);
                set.addAnimation(animation);
                LayoutAnimationController controller = new LayoutAnimationController(set, 0.2f);
                parent.setLayoutAnimation(controller);

                stream = new Stream();
                stream.execute(MainActivity.username, MainActivity.password);
                anim.start();
            }
        });
/*    //fpr API16 TODO
        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0,
                0, view.getWidth(), view.getHeight());
        Intent intent = new Intent(MainActivity.context, MainActivity.class);
        ((Activity)MainActivity.context).startActivity(intent, options.toBundle());
*/
        AnimationSet set = new AnimationSet(true);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.context, R.anim.slide_in_bottom_twist);
        set.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(1000);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.2f);
        //    controller.
        parent.setLayoutAnimation(controller);

        Animation up = AnimationUtils.loadAnimation(
                MainActivity.context, R.anim.slide_in_bottom_twist);
        up.setDuration(1000);

        //  AnimationFunctions.firstExpand(updater);
        stream = new Stream();
        stream.execute(MainActivity.username, MainActivity.password);
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
                            updaterText.setText(getString(R.string.fetching));
                            stream = new Stream();
                            stream.execute(MainActivity.username, MainActivity.password);
                            ongoing = true;
                        }
                        break;
                    default:
                        if (diff == 0 && (updater.getVisibility() == View.GONE || updater.getVisibility() == View.INVISIBLE)) {
                            updater.setVisibility(View.VISIBLE);
                        } else if (diff == 0)
                            updaterText.setText(getResources().getString(R.string.release));
                        else if (updater.getVisibility() != View.GONE && updater.getVisibility() != View.INVISIBLE)
                            updaterText.setText(getResources().getString(R.string.pull));
                }
                return false;
            }
        });
        return view;
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
            String resp = i.stream(username, password, String.valueOf(START), String.valueOf(END), FORCE);
            if ("error".equals(resp))
                return -3707;
            if (" ".equals(resp) || "".equals(resp) || resp.length() == 0)
                return 406;
            events = resp.split("\\<BR \\/\\>");
            int size = events.length;
            if (size == 0)
                resp = "406";
            for (int j = 0; j < size; j++) {
                String[] splits = events[j].split("\\<SPC\\/\\>");
                String id = splits[0];
                Event event = MainActivity.sql.getEvent(id);
                if (event != null) {
                    event.setHangging(Integer.parseInt(splits[1]));
                    if(splits.length > 1)
                        event.setFeatured(Integer.parseInt(splits[2]));
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
            ongoing = true;
            if ("1".equals(FORCE)) {
                parent.removeAllViews();
                LA = null;
                LA = new LoaderAdapter(MainActivity.context);
                LA.setTextColor(getResources().getColor(R.color.dorange));
                LA.attach(MainActivity.parent);
                LA.setProgressLoadingImage((AnimationDrawable)getResources().getDrawable(R.drawable.loading));
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
                    MainActivity.show_error(getString(R.string.E_internet));
                    break;
                case 406:
                    if ("1".equals(FORCE))
                        MainActivity.show_error(getString(R.string.E_no_stream));
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
                        EventAdapter EA = new EventAdapter(MainActivity.context, e).attach(parent);
                        MainActivity.getImage(e.getHost());
                        Events.add(EA);
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
                    if ("1".equals(FORCE))
                        parent.setLayoutAnimation(new LayoutAnimationController(set, 0.2f));
                    START += STEP;
                    END += STEP;
                    FORCE = "";
                    break;
                default:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            //        MainActivity.defaultActionButton.setImageResource(0);
            MainActivity.defaultActionButton.setImageResource(R.drawable.imagebutton_logo);
            ongoing = false;
            cancel(false);
        }
    }

    @Override
    public void onDestroy() {
        if (stream != null) stream.cancel(true);
        if (LA != null) LA.destroy();
        //    MainActivity.defaultActionButton.setImageResource(0);
        MainActivity.defaultActionButton.setImageResource(R.drawable.imagebutton_logo);
        super.onDestroy();
    }

}