package sy.UProject.talat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.LoaderAdapter;

public class FragmentSearch extends Fragment {
    SharedPreferences SP;
    public LoaderAdapter LA;
    public ImageView button;
    public EditText text;
    private Search search;
    private ArrayList<EventAdapter> l = new ArrayList<EventAdapter>();
    private Context context;
    private ViewGroup parent;
    private View view, indicator;
    private Rect rect;
    private boolean SearchTextIsEmpty, TextChangeDontPlayAnimation;

    public FragmentSearch() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search, container, false);
        parent = (ViewGroup) view.findViewById(R.id.search_wrapper);
        context = MainActivity.context;

        SearchTextIsEmpty = true;
        TextChangeDontPlayAnimation = false;
        button = (ImageView) view.findViewById(R.id.search_search);
        final ImageView defaultActionButton = MainActivity.defaultActionButton;
        Animation anim;
        AnimationSet AS = new AnimationSet(false);
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
                anim = AnimationUtils.loadAnimation(context,
                        R.anim.grow_out);
                anim.setDuration(500);
                AS.addAnimation(anim);
            }
        }
        view.findViewById(R.id.search_container).startAnimation(AS);
        defaultActionButton.setTag("0");
        defaultActionButton.setBackgroundResource(R.drawable.imagebutton_search);
        MainActivity.extraActionButton.setBackgroundResource(R.drawable.imagebutton_search);
        defaultActionButton.setVisibility(View.INVISIBLE);
        indicator = view.findViewById(R.id.search_text_indecator);
        text = (EditText) view.findViewById(R.id.search_text);
        text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (TextChangeDontPlayAnimation)
                    return;
                if (text.getText().toString().length() == 0) {
                    SearchTextIsEmpty = true;
                    Animation die = AnimationUtils.loadAnimation(
                            context, R.anim.grow_out_collapse);
                    die.setDuration(150);
                    button.startAnimation(die);
                    die.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation live = AnimationUtils.loadAnimation(
                                    context,
                                    R.anim.grow_in_collapse);
                            live.setDuration(150);
                            button.setImageResource(R.drawable.ic_action_search_circle_red);
                            indicator.setBackgroundResource(R.color.red5);
                            button.startAnimation(live);
                        }
                    });
                } else {
                    if (SearchTextIsEmpty) {
                        SearchTextIsEmpty = false;
                        Animation die = AnimationUtils.loadAnimation(
                                context, R.anim.grow_out_collapse);
                        die.setDuration(150);
                        button.startAnimation(die);
                        die.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation live = AnimationUtils.loadAnimation(
                                        context,
                                        R.anim.grow_in_collapse);
                                live.setDuration(150);
                                button.setImageResource(R.drawable.ic_action_search_circle);
                                indicator.setBackgroundResource(R.color.green5);
                                button.startAnimation(live);
                            }
                        });
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (text.getVisibility() == View.VISIBLE)
                    return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation circleInPlace = AnimationUtils.loadAnimation(context,
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
            @Override
            public void onClick(View v) {
                Animation turn;
                if (text.getVisibility() == View.VISIBLE) {
                    if (text.getText().length() != 0) {
                        search = new Search();
                        search.execute(MainActivity.username, MainActivity.password, text.getText().toString());
                    }
                    SearchTextIsEmpty = true;
                    TextChangeDontPlayAnimation = true;
                    text.setVisibility(View.GONE);
                    text.setText("");
                    indicator.setBackgroundResource(R.color.red5);
                    InputMethodManager imm = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    button.setImageResource(R.drawable.imagebutton_search);
                    Animation getOut = AnimationUtils.loadAnimation(
                            context, R.anim.grow_out_right);
                    getOut.setDuration(150);
                    text.startAnimation(getOut);
                    turn = AnimationUtils.loadAnimation(context,
                            R.anim.turnclockwise);
                } else {
                    Animation getIn = AnimationUtils.loadAnimation(
                            context, R.anim.grow_in_right);
                    getIn.setDuration(150);
                    text.startAnimation(getIn);
                    indicator.startAnimation(getIn);
                    text.setVisibility(View.VISIBLE);
                    text.requestFocus();
                    turn = AnimationUtils.loadAnimation(context,
                            R.anim.turnanticlockwise);
                }
                turn.setDuration(350);
                v.startAnimation(turn);
                TextChangeDontPlayAnimation = false;
            }
        });

        text.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    indicator.setBackgroundResource(R.color.dorange5);
                else
                    indicator.setBackgroundResource(R.color.red5);
            }
        });

        DesignFunctions.setCornered(text,
                getResources().getColor(R.color.silver2),
                DesignFunctions.VALUE_DEFAULT_CORNER, 15);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation getIn = AnimationUtils.loadAnimation(
                        context, R.anim.grow_in_right);
                Animation turn = AnimationUtils.loadAnimation(context,
                        R.anim.turnanticlockwise);
                getIn.setDuration(150);
                turn.setDuration(350);
                text.startAnimation(getIn);
                indicator.startAnimation(getIn);
                text.setVisibility(View.VISIBLE);
                text.requestFocus();
                button.startAnimation(turn);
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(text.getWindowToken(), 0);
            }
        }, 750);

        return view;
    }

    public boolean hideSearchButton() {
        if (text.getVisibility() == View.VISIBLE) {
            SearchTextIsEmpty = true;
            TextChangeDontPlayAnimation = true;
            text.setVisibility(View.GONE);
            text.setText("");
            indicator.setBackgroundResource(R.color.red5);
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            button.setImageResource(R.drawable.imagebutton_search);
            Animation getOut = AnimationUtils.loadAnimation(
                    context, R.anim.grow_out_right);
            getOut.setDuration(150);
            text.startAnimation(getOut);
            Animation turn = AnimationUtils.loadAnimation(context,
                    R.anim.turnclockwise);
            turn.setDuration(350);
            button.startAnimation(turn);
            TextChangeDontPlayAnimation = false;
            return true;
        }
        return false;
    }

    private class Search extends AsyncTask<String, Integer, Integer> {
        String username, password, query, result;
        String[] events;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            query = params[2];
            internet i = new internet();
            String resp = i.search(username, password, query);
            Log.v("Internet Connection", "Doing - searching - " + resp);
            if ("error".equals(resp))
                return -3707;
            if ("602".equals(resp))
                return 602;
            events = resp.split("\\<BR \\/\\>");
            int size = events.length;
            for (int j = 0; j < size; j++) {
                String[] splits = events[j].split("\\<SPC\\/\\>");
                String id = splits[0];
                Event event = MainActivity.sql.getEvent(id);
                if (event != null) {
                    event.setHangging(Integer.parseInt(splits[1]));
                    if(splits.length > 1)
                        event.setFeatured(Integer.parseInt(splits[2]));
                    MainActivity.sql.updateEvent(event);
                } else {
                    Event e = i.fetch_event(username, password, id);
                    if (e != null)
                        MainActivity.sql.addEvent(e);
                }
                resp = "1";
            }
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            parent.removeAllViews();
            l.clear();
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
                case 1:
                    int size = events.length;
                    for (int j = 0; j < size; j++) {
                        String id = events[j].split("\\<SPC\\/\\>")[0];
                        Event e = MainActivity.sql.getEvent(id);
                        if (e == null) {
                            Log.e("error", "event not fetched correctly");
                            continue;
                        }
                        l.add(new EventAdapter(MainActivity.context, e));
                        l.get(j).attach(parent);
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
                    parent.setLayoutAnimation(new LayoutAnimationController(set, 0.2f));
                    break;
                case 602:
                    MainActivity.show_error(getString(R.string.E_no_result));
                    break;
           /*         DialogAdapter dialogAdapter = new DialogAdapter(context);
                    dialogAdapter.setFancy();
                    dialogAdapter.setHeadlineTextColor(getResources().getColor(R.color.purple));
                    dialogAdapter.setText(getString(R.string.E_no_result));
                    dialogAdapter.attach(MainActivity.parent);
                    dialogAdapter.fire();
                    break;
            */
                default:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    @Override
    public void onDestroy() {
        if (search != null) search.cancel(true);
        if (LA != null) LA.destroy();
        super.onDestroy();
    }
}