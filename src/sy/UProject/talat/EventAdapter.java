package sy.UProject.talat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import sy.UProject.UI.AnimationFunctions;
import sy.UProject.UI.InternetTools;
import sy.UProject.UI.pinch;

public class EventAdapter {

    private View view;
    private Event event;
    private static final int[] colors = {R.color.blue4, R.color.red4, R.color.red4, R.color.green4,
            R.color.orange4, R.color.dblue4, R.color.yellow5, R.color.silver4},
            colors2 = {R.color.blue, R.color.red, R.color.red, R.color.green,
                    R.color.orange, R.color.dblue, R.color.yellow6,
                    R.color.silver},
            colors3 = {R.color.blue6, R.color.red6, R.color.red6, R.color.green6,
                    R.color.orange6, R.color.dblue6, R.color.yellow7,
                    R.color.silver6}, colors4 = {R.color.blue1, R.color.red1,
            R.color.red1, R.color.green1, R.color.orange1, R.color.dblue1,
            R.color.yellow1, R.color.silver1};
    private static final int[] backgrounds = {R.drawable.concert, R.drawable.family, R.drawable.family,
            R.drawable.sport, R.drawable.ultimate, R.drawable.education,
            R.drawable.ultimate, R.drawable.other};
    public EventHolder holder;


    public static class EventHolder {
        public RelativeLayout it;
        //    private LinearLayout linearLayout;
        public ImageView cover;//, profile;
        public TextView date, hangging, headline, desc;
        public View indicator;
    }

    public EventAdapter(final Context context, final Event event) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_event_item, null);
        holder = new EventHolder();
        this.event = event;


        holder.it = (RelativeLayout) view.findViewById(R.id.CE_wrapper);
        //    holder.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout2);
        //    holder.profile = (ImageView) view.findViewById(R.id.CE_profile);
        holder.cover = (ImageView) view.findViewById(R.id.CE_cover);
        holder.date = (TextView) view.findViewById(R.id.CE_date);
        holder.desc = (TextView) view.findViewById(R.id.CE_desc);
        holder.hangging = (TextView) view.findViewById(R.id.CE_hangging);
        holder.headline = (TextView) view.findViewById(R.id.CE_headline);
        holder.indicator = view.findViewById(R.id.CE_headline_indecator);

        holder.cover.setBackgroundResource(colors[event.getSection()]);
        if(event.getFeatured() > 0)
            Picasso.with(context).load(backgrounds[event.getFeatured()]).into(holder.cover);
        else {
            InternetTools IT = new InternetTools(context);
                    IT.getImage(holder.cover, internet.link + "/events/" + event.getId() + "/cover.png", Environment.getExternalStorageDirectory()
                            + File.separator + ".Talat", "event_" + event.getId() + ".jpg", R.drawable.concert_0, false);
            IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                @Override
                public void onLoaded(Bitmap bitmap) {
                    ((Activity)context ).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(context).load(new File(Environment.getExternalStorageDirectory()
                                    + File.separator + ".Talat", "event_" + event.getId() + ".jpg")).into(holder.cover);
                        }
                    });
                }
                @Override
                public void onError() {
                }
            });
        }
        //    Bitmap bMap = BitmapFactory.decodeResource(MainActivity.context.getResources(), backgrounds[event.getSection()]);
        //    Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 250, (bMap.getHeight()* 250)/bMap.getWidth(), true);
        //    holder.cover.setImageBitmap(bMapScaled);
        holder.indicator.setBackgroundResource(colors3[event.getSection()]);
        holder.headline.setBackgroundResource(colors2[event.getSection()]);
        holder.date.setBackgroundResource(colors2[event.getSection()]);
        holder.desc.setBackgroundResource(colors4[event.getSection()]);
        holder.hangging.setText(event.getHangging().toString());
        holder.headline.setText(event.getHeadline());
        holder.desc.setText(event.getDesc());
        if (event.getIsHangging() == 1) {
            holder.hangging.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_hangging_true, 0);
            holder.hangging.setTextColor(context.getResources().getColor(R.color.purple));
        }
        long eventTime = new Date(event.getS_date()).getTime() / 1000;
        long now = new Date().getTime() / 1000;
        if (eventTime < now)
            holder.date.setText("passed");
        else if (eventTime < now + 24 * 3600)
            holder.date.setText("Today");
        else if (eventTime < now + 24 * 3600 * 7)
            holder.date.setText("in " + (eventTime - now) / 24 / 3600 + " days");
        else if (eventTime < now + 24 * 3600 * 31)
            holder.date.setText("in " + (eventTime - now) / 24 / 3600 / 7 + " weeks");
        else
            holder.date.setText("not this month");

        holder.it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ar) {
            }
        });

        final pinch p = new pinch();
        p.hasPuddle = true;
        p.minimumDistanceBetweenFingers = 20;
        p.minimumDistanceForEachFinger = 13;
        p.setStartPinchListener(new pinch.OnStartPinchListener() {
            @Override
            public void onStartPinch() {
                holder.it.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
        p.setPinchListener(new pinch.OnPinchListener() {
            @Override
            public void onPinch() {
                if (holder.date.getVisibility() == View.VISIBLE) {
                    AnimationFunctions.expand(holder.desc);
                    AnimationFunctions.collapse(holder.date, View.GONE);
                    p.minimumDistanceBetweenFingers = -30;
                    p.minimumDistanceForEachFinger = -16;
                } else {
                    AnimationFunctions.expand(holder.date);
                    AnimationFunctions.collapse(holder.desc, View.GONE);
                    p.minimumDistanceBetweenFingers = 30;
                    p.minimumDistanceForEachFinger = 16;
                }
            }
        });
        p.setClickListener(new pinch.OnClickListener() {
            @Override
            public void onClick() {
                Intent i = new Intent(context, EventTemplate.class);
                i.putExtra("id", event.getId());
                context.startActivity(i);
                holder.it.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            }
        });
        holder.it.setSoundEffectsEnabled(false);
        holder.it.setOnTouchListener(p.pinchy);
    }

    public EventAdapter attach(ViewGroup parent) {
        parent.addView(view);
        return this;
    }

    public void update() {
        holder.hangging.setText(event.getHangging().toString());
        if (event.getIsHangging() == 1) {
            holder.hangging.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_hangging_true, 0);
            holder.hangging.setTextColor(holder.it.getContext().getResources().getColor(R.color.purple));
        }
        long eventTime = new Date(event.getS_date()).getTime() / 1000;
        long now = new Date().getTime() / 1000;
        if (eventTime < now)
            holder.date.setText("passed");
        else if (eventTime < now + 24 * 3600)
            holder.date.setText("Today");
        else if (eventTime < now + 24 * 3600 * 7)
            holder.date.setText("in " + (eventTime - now) / 24 / 3600 + " days");
        else if (eventTime < now + 24 * 3600 * 31)
            holder.date.setText("in " + (eventTime - now) / 24 / 3600 / 7 + " weeks");
        else
            holder.date.setText("not this month");
    }
}