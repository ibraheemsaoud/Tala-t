package sy.UProject.talat;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.InternetTools;
import sy.UProject.UI.LoaderAdapter;
import sy.UProject.UI.Utility;

@SuppressLint("NewApi")
public class FragmentOptions extends Fragment {
    static String username, password;

    SharedPreferences SP;
    public InternetTools internetTools;
    public DialogAdapter DA;
    public LoaderAdapter LA;
    private Update update;
    private UpdateS updates;
    private UpdateR updater;
    public UpdateM UM;
    private LinearLayout Reset;
    private ImageView profilePic;
    private EditText email, passwordE, password2;
    private View view;
    private int region_count = 0, section_count = 0;
    int[] sectionsNames = {R.id.WR_section0, R.id.WR_section1, R.id.WR_section2, R.id.WR_section3, R.id.WR_section4, R.id.WR_section5, R.id.WR_section6, R.id.WR_section7};
    private int[] regionNames = {R.id.WR_region0, R.id.WR_region1, R.id.WR_region2, R.id.WR_region3},
            colorNames;

    public FragmentOptions() {
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.options, container, false);
        AnimationSet AS = new AnimationSet(false);
        Animation anim;
        anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 2);
        if (MainActivity.defaultActionButton.getVisibility() == View.INVISIBLE)
            anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, -2);
        anim.setStartOffset(200);
        anim.setDuration(250);
        AS.addAnimation(anim);
        anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        anim.setStartOffset(450);
        anim.setDuration(250);
        AS.addAnimation(anim);
        anim = AnimationUtils.loadAnimation(MainActivity.context, R.anim.grow_in);
        anim.setStartOffset(200);
        anim.setDuration(500);
        AS.addAnimation(anim);
        if (MainActivity.defaultActionButton.getVisibility() == View.INVISIBLE)
            MainActivity.extraActionButton.startAnimation(AS);
        else
            MainActivity.defaultActionButton.startAnimation(AS);
        MainActivity.defaultActionButton.setVisibility(View.INVISIBLE);
        MainActivity.defaultActionButton.setTag("1");
        SP = view.getContext().getApplicationContext()
                .getSharedPreferences("UProject-talaat", Context.MODE_PRIVATE);
        colorNames = new int[8];
        colorNames[0] = getResources().getColor(R.color.blue2);
        colorNames[1] = getResources().getColor(R.color.red2);
        colorNames[2] = getResources().getColor(R.color.red2);
        colorNames[3] = getResources().getColor(R.color.green2);
        colorNames[4] = getResources().getColor(R.color.orange2);
        colorNames[5] = getResources().getColor(R.color.dblue2);
        colorNames[6] = getResources().getColor(R.color.yellow2);
        colorNames[7] = getResources().getColor(R.color.silver2);
        final Button Basic, Sections, Regions, Money, Language, Logout, About, Help, Picture;

        Help = (Button) view.findViewById(R.id.options_help);
        Basic = (Button) view.findViewById(R.id.options_basic);
        Regions = (Button) view.findViewById(R.id.options_regions);
        Money = (Button) view.findViewById(R.id.options_add_money);
        Sections = (Button) view.findViewById(R.id.options_sections);
        Language = (Button) view.findViewById(R.id.options_language);
        Reset = (LinearLayout) view.findViewById(R.id.options_reset);
        profilePic = (ImageView) view.findViewById(R.id.options_image);
        Picture = (Button) view.findViewById(R.id.options_update_picture);
        About = (Button) view.findViewById(R.id.options_update_about);
        Logout = (Button) view.findViewById(R.id.options_logout);
        ((TextView) view.findViewById(R.id.options_username)).setText(MainActivity.username);
        ((TextView) view.findViewById(R.id.options_money)).setText(MainActivity.money);
        int color1 = getResources().getColor(R.color.white);
        int color2 = getResources().getColor(R.color.dorange2);

        DesignFunctions.setPoggy(profilePic);
        DesignFunctions.setPuddle(Basic, color1, color2);
        DesignFunctions.setPuddle(Money, color1, color2);
        DesignFunctions.setPuddle(About, color1, color2);
        DesignFunctions.setPuddle(Regions, color1, color2);
        DesignFunctions.setPuddle(Picture, color1, color2);
        DesignFunctions.setPuddle(Sections, color1, color2);
        DesignFunctions.setPuddle(Language, color1, color2);
        DesignFunctions.setPuddle(Reset, color1, color2);
        DesignFunctions.setPuddle(Help, color1, color2);
        DesignFunctions.setPuddle(Logout, getResources().getColor(R.color.red),
                getResources().getColor(R.color.red8));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
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
        Picasso.with(MainActivity.context)
                .load(new File(Environment.getExternalStorageDirectory() + File.separator + ".Talat", MainActivity.username + ".jpg"))
                .placeholder(R.drawable.ic_user_template)
                .into(target);
        profilePic.setTag(target);
        Basic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Context context = view.getContext();
                DA = null;
                DA = new DialogAdapter(context);
                DA.setDestroyable();
                DA.holder.oneButton.setText(R.string.update);
                DA.attach(MainActivity.parent);
                View view = (((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.options_profile,
                                null));
                DA.add(view);
                DA.fire();
                email = (EditText) view.findViewById(R.id.options_email);
                passwordE = (EditText) view.findViewById(R.id.options_password);
                password2 = (EditText) view.findViewById(R.id.options_confirm);
                DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (email.getText().toString().length() == 0 && passwordE.getText().toString().length() == 0) {
                            password2.setError(getString(R.string.E_fill_one));
                            return;
                        }
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(email.getWindowToken(),
                                0);
                        update = new Update();
                        update.execute(MainActivity.username, password2.getText().toString(), email.getText().toString(), passwordE.getText().toString());
                    }
                });
            }
        });

        Language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Context context = view.getContext();
                DA = null;
                DA = new DialogAdapter(context);
                DA.setDestroyable();
                DA.setPadding(0, 10, 0, 10);
                DA.holder.oneButton.setVisibility(View.GONE);
                DA.attach(MainActivity.parent);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 1);
                lp1.setMargins(10, 0, 10, 0);
                // TODO lp.setMargins(10, 0, 10, 0);
                int color1 = getResources().getColor(R.color.white);
                int color2 = getResources().getColor(R.color.blue3);
                OnClickListener changeLanguage = new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        DA.destroy();
                        SP.edit()
                                .putString("UProject-talaat_LANG",
                                        arg0.getTag().toString()).commit();
                        Intent i = new Intent(arg0.getContext(),
                                MainActivity.class);
                        startActivity(i);
                        ((Activity) MainActivity.context).finish();
                    }
                };
                DesignFunctions.setCornered(DA.holder.body, color1, 4, 15);

                Button English = new Button(context);
                Button Arabic = new Button(context);
                View divider = new View(context);

                English.setTextColor(getResources().getColor(R.color.black7));
                English.setBackgroundResource(R.color.white);
                English.setOnClickListener(changeLanguage);
                //       English.setPadding(10, 10, 10, 10);
                English.setLayoutParams(lp);
                English.setText("English");
                English.setTextSize(20);
                English.setTag("en");
                DesignFunctions.setPuddle(English, color1, color2);

                Arabic.setTextColor(getResources().getColor(R.color.black7));
                Arabic.setBackgroundResource(R.color.white);
                Arabic.setOnClickListener(changeLanguage);
                //       Arabic.setPadding(10, 10, 10, 10);
                Arabic.setLayoutParams(lp);
                Arabic.setTextSize(24);
                Arabic.setText("عربي");
                Arabic.setTag("ar");
                DesignFunctions.setPuddle(Arabic, color1, color2);

                divider.setLayoutParams(lp1);
                divider.setBackgroundResource(R.color.black4);

                DA.add(English);
                DA.add(divider);
                DA.add(Arabic);

                DA.fire();
            }
        });

        Sections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                section_count = 0;
                Context context = view.getContext();
                View view = (((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.welcome_reg2,
                                null));
                final CheckBox[] sections = new CheckBox[8];
                DA = null;
                DA = new DialogAdapter(context);
                DA.setDestroyable();
                DA.setPadding(0, 10, 0, 0);
                DA.holder.oneButton.setEnabled(false);
                DA.setText(getString(R.string.update));
                DA.attach(MainActivity.parent);
                DA.add(view);
                for (int i = 0; i < 8; i++) {
                    sections[i] = (CheckBox) view.findViewById(sectionsNames[i]);
                    DesignFunctions.setPuddle(sections[i], getResources().getColor(
                            R.color.white), colorNames[i]);
                    char[] arr = MainActivity.sections.toCharArray();
                    if (arr[i] == '1') {
                        section_count++;
                        sections[i].setChecked(true);
                        DA.holder.oneButton.setEnabled(true);
                    }
                    sections[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View arg0) {
                            if (((CheckBox) arg0).isChecked()) {
                                if (section_count == 0)
                                    DA.holder.oneButton.setEnabled(true);
                                section_count++;
                            } else {
                                section_count--;
                                if (section_count == 0)
                                    DA.holder.oneButton.setEnabled(false);
                            }
                        }
                    });
                }
                DA.fire();
                DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = "";
                        for (int i = 0; i < 8; i++)
                            if (sections[i].isChecked())
                                data += '1';
                            else
                                data += '0';
                        updates = new UpdateS();
                        updates.execute(MainActivity.username, MainActivity.password, data);
                    }
                });
            }
        });

        Regions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Context context = view.getContext();
                final CheckBox[] regions = new CheckBox[4];
                View view = (((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.welcome_reg3,
                                null));
                region_count = 0;
                DA = null;
                DA = new DialogAdapter(context);
                DA.setDestroyable();
                DA.setPadding(0, 10, 0, 0);
                DA.holder.oneButton.setEnabled(false);
                DA.setText(getString(R.string.update));
                DA.attach(MainActivity.parent);
                DA.add(view);
                for (int i = 0; i < 4; i++) {
                    regions[i] = (CheckBox) view.findViewById(regionNames[i]);
                    DesignFunctions.setPuddle(regions[i], getResources().getColor(
                            R.color.white), getResources().getColor(
                            R.color.dorange2));
                    char[] arr = MainActivity.region.toCharArray();
                    if (arr[i] == '1') {
                        region_count++;
                        regions[i].setChecked(true);
                        DA.holder.oneButton.setEnabled(true);
                    }
                    regions[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View arg0) {
                            if (((CheckBox) arg0).isChecked()) {
                                if (region_count == 0)
                                    DA.holder.oneButton.setEnabled(true);
                                region_count++;
                            } else {
                                region_count--;
                                if (region_count == 0)
                                    DA.holder.oneButton.setEnabled(false);
                            }
                        }
                    });
                }
                DA.fire();
                DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = "";
                        for (int i = 0; i < 4; i++)
                            if (regions[i].isChecked())
                                data += '1';
                            else
                                data += '0';
                        updater = new UpdateR();
                        updater.execute(MainActivity.username, MainActivity.password, data);
                    }
                });
            }
        });
        Picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_image();
            }
        });
        profilePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.context, Profile.class);
                i.putExtra("id", MainActivity.username);
                startActivity(i);
            }
        });
        About.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) MainActivity.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final EditText aboutText = (EditText) inflater.inflate(R.layout.edit_text, null);
                aboutText.setLayoutParams(About.getLayoutParams());
                aboutText.setHint("Talk about yourself");

                DA = null;
                DA = new DialogAdapter(context);
                DA.attach(MainActivity.parent);
                DA.add(aboutText);
                DA.fire();

                DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UpdateAbout UA = new UpdateAbout();
                        UA.execute(MainActivity.username, MainActivity.password, aboutText.getText().toString());
                    }
                });
            }
        });
        Money.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Context context = view.getContext();
                View view = (((Activity) MainActivity.context)
                        .getLayoutInflater().inflate(R.layout.options_money,
                                null));
                DA = null;
                DA = new DialogAdapter(context);
                DA.setFancy();
                DA.attach(MainActivity.parent);
                DA.add(view);
                DA.fire();
            }
        });
        Reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sql.clear();
                new File(MainActivity.context.getCacheDir().getPath() + File.separator + "picasso-cache").delete();
                Toast.makeText(MainActivity.context, getString(R.string.done), Toast.LENGTH_SHORT).show();
            }
        });
        Help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LogTest();
            }
        });
        Logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.sql.clear();
                Utility.WriteFile(MainActivity.context, "user_data", "");
                Utility.WriteFile(MainActivity.context, "notifications", "");
                Utility.WriteFile(MainActivity.context, "groups", "");
                Utility.WriteFile(MainActivity.context, "member_hangging", "");
                new File(MainActivity.context.getCacheDir().getPath() + File.separator + "picasso-cache").delete();
                startActivity(new Intent(MainActivity.context,
                        WelcomeActivity.class));
                ((Activity) MainActivity.context).finish();
            }
        });
        view.findViewById(R.id.options_about).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Random r = new Random();
                Intent i = new Intent(MainActivity.context, EventTemplate.class);
                i.putExtra("id", String.valueOf(r.nextInt(100) % 10));
                startActivity(i);
                return false;
            }
        });
        UM = new UpdateM();
        UM.execute(MainActivity.username, MainActivity.password);
        return view;
    }

    private void upload_image() {
        internetTools = null;
        internetTools = new InternetTools(MainActivity.context);
        internetTools.setOnUploadedListener(new InternetTools.onUploadListener() {
            @Override
            public void onLoaded() {
                UM.cancel(true);
                UM = null;
                UM = new UpdateM();
                UM.execute(MainActivity.username, MainActivity.password);
                MainActivity.stopOngoingNotification();
        //        Toast.makeText(MainActivity.context, getString(R.string.done), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                MainActivity.show_error(getString(R.string.E_unknown_uploading));
                MainActivity.stopOngoingNotification();
            }
        });
        internetTools.setURL(internet.link + "/update_pic.php");
        internetTools.UploadImage();
    }

    private void LogTest() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

            String logFilePath = Environment.getExternalStorageDirectory() + File.separator + "TalatLog.txt";

            File logFile = new File(logFilePath);
            if (!logFile.exists())
                logFile.createNewFile();

            FileOutputStream outStream = new FileOutputStream(logFile, true);
            byte[] buffer = log.toString().getBytes();

            outStream.write(buffer);
            outStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class Update extends AsyncTask<String, Integer, Integer> {
        String username, email, password, oldPassword;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            email = params[2];
            oldPassword = params[3];
            if (email == null || email.length() == 0)
                email = MainActivity.email;
            if (password == null || password.length() == 0)
                password = MainActivity.password;
            internet i = new internet();
            String resp = i.updateBasics(username, oldPassword, email, password);
            Log.v("Internet Connection", "Doing - updating_basics - " + resp);
            if ("error".equals(resp))
                return -3707;
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            DA.hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_internet), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 0:
                    DA.fire();
                    ((EditText) ((ViewGroup) DA.holder.body.getChildAt(0)).getChildAt(2))
                            .setError(getString(R.string.E_password_old));
                    break;
                case 1:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.done), Toast.LENGTH_SHORT)
                            .show();
                    MainActivity.password = password;
                    MainActivity.email = email;
                    String[] data = Utility.readFile(MainActivity.context, "user_data").split("\\<SPC\\/\\>");
                    data[1] = password;
                    data[2] = email;
                    String com = "";
                    for (int i = 0; i < data.length; i++)
                        com += data[i] + "<SPC/>";
                    Utility.WriteFile(MainActivity.context, "user_data", com);
                    break;
                case 602:
                    DA.fire();
                    ((EditText) ((ViewGroup) DA.holder.body.getChildAt(0)).getChildAt(1))
                            .setError(getString(R.string.E_password));
                    break;
                case 603:
                    DA.fire();
                    ((EditText) ((ViewGroup) DA.holder.body.getChildAt(0)).getChildAt(0))
                            .setError(getString(R.string.E_email));
                    break;
                default:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    private class UpdateM extends AsyncTask<String, Integer, Integer> {
        String username, password, resp;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            internet i = new internet();
            resp = i.getProfileUpdates(username, password);
            Log.v("Internet Connection", "Doing - updating_user_profile - " + resp);
            if ("error".equals(resp))
                return -3707;
            return 1;
        }

        ProfileQL PQL;

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 1:
                    String[] data = Utility.readFile(MainActivity.context, "user_data").split("\\<SPC\\/\\>");
                    String[] data2 = resp.split("\\<BR \\/\\>");
                    final String money = data2[0], pic = data2[1];
                    data[4] = data2[0];
                    MainActivity.money = money;
                    String com = "";
                    for (int i = 0; i < data.length; i++)
                        com += data[i] + "<SPC/>";
                    Utility.WriteFile(MainActivity.context, "user_data", com);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(
                            new IntEvaluator(), Integer.valueOf(MainActivity.money), Integer.valueOf(money));
                    colorAnimation
                            .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(
                                        ValueAnimator animator) {
                                    ((TextView) view.findViewById(R.id.options_money)).setText(animator.getAnimatedValue() + "");
                                    if (animator.getAnimatedValue().equals(money))
                                        MainActivity.money = money;
                                }
                            });
                    colorAnimation.setDuration(1000 * (Math.abs(Integer.valueOf(MainActivity.money) - Integer.valueOf(money)) / 250));//Math.abs(Integer.valueOf(MainActivity.money) - Integer.valueOf(money))/10);
                    colorAnimation.start();
                    PQL = new ProfileQL(MainActivity.context);
                    String picOld = PQL.getPic(MainActivity.username);
                    if (!PQL.checkProfile(MainActivity.username) || (picOld != null && pic.compareTo(picOld) != 0)) {
                        InternetTools IT = new InternetTools(MainActivity.context);
                        IT.getImage(profilePic, internet.link + "/users/" + MainActivity.username + "/profilePic.png",
                                Environment.getExternalStorageDirectory() + File.separator + ".Talat",
                                MainActivity.username + ".jpg", R.drawable.ic_user_template, true);
                        IT.setOnImageLoadedListener(new InternetTools.OnImageLoadedListener() {
                            @Override
                            public void onLoaded(final Bitmap bitmap) {
                                ((Activity) MainActivity.context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(bitmap, 4));
                                        PQL.addProfile(MainActivity.username, PQL.getAbot(MainActivity.username), pic);
                                        PQL = null;
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                                PQL = null;
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_template);
                                profilePic.setImageBitmap(DesignFunctions.getRoundedBitmapWithWhiteBorder(bitmap, 4));
                            }
                        });
                    }
                    break;
            }
            cancel(false);
        }
    }

    private class UpdateAbout extends AsyncTask<String, Integer, Integer> {
        String username, password, about;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            about = params[2];
            internet i = new internet();
            String resp = i.updateAbout(username, password, about);
            Log.v("Internet Connection", "Doing - updating_money - " + resp);
            if ("error".equals(resp))
                return -3707;
            return 1;
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            DA.hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            DA.setDestroyed();
            switch (result) {
                case -3707:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_internet), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.done), Toast.LENGTH_SHORT)
                            .show();
                    String[] dat = Utility.readFile(MainActivity.context, "user_data").split("\\<SPC\\/\\>");
                    MainActivity.about = about;
                    dat[7] = about;
                    String com = "";
                    for (int i = 0; i < dat.length; i++)
                        com += dat[i] + "<SPC/>";
                    Utility.WriteFile(MainActivity.context, "user_data", com);
                    break;
                default:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    private class UpdateS extends AsyncTask<String, Integer, Integer> {
        String username, password, data;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            data = params[2];
            internet i = new internet();
            String resp = i.updateSections(username, password, data);
            Log.v("Internet Connection", "Doing - updating_sections - " + resp);
            if ("error".equals(resp))
                return -3707;
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            DA.hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_internet), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.done), Toast.LENGTH_SHORT)
                            .show();
                    String[] dat = Utility.readFile(MainActivity.context, "user_data").split("\\<SPC\\/\\>");
                    MainActivity.sections = data;
                    dat[3] = data;
                    String com = "";
                    for (int i = 0; i < dat.length; i++)
                        com += dat[i] + "<SPC/>";
                    Utility.WriteFile(MainActivity.context, "user_data", com);
                    break;
                default:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    private class UpdateR extends AsyncTask<String, Integer, Integer> {
        String username, password, data;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            data = params[2];
            internet i = new internet();
            String resp = i.updateRegions(username, password, data);
            Log.v("Internet Connection", "Doing - updating_regions - " + resp);
            if ("error".equals(resp))
                return -3707;
            return Integer.parseInt(resp);
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(MainActivity.context);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(MainActivity.parent);
            LA.fire();
            DA.hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            switch (result) {
                case -3707:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_internet), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.context,
                            getString(R.string.done), Toast.LENGTH_SHORT)
                            .show();
                    String[] dat = Utility.readFile(MainActivity.context, "user_data").split("\\<SPC\\/\\>");
                    MainActivity.region = data;
                    dat[5] = data;
                    String com = "";
                    for (int i = 0; i < dat.length; i++)
                        com += dat[i] + "<SPC/>";
                    Utility.WriteFile(MainActivity.context, "user_data", com);
                    break;
                default:
                    DA.fire();
                    Toast.makeText(MainActivity.context,
                            getString(R.string.E_unknown), Toast.LENGTH_SHORT)
                            .show();
            }
            cancel(false);
        }
    }

    @Override
    public void onDestroy() {
        if (update != null) update.cancel(true);
        if (updates != null) updates.cancel(true);
        if (updater != null) updater.cancel(true);
        if (UM != null) UM.cancel(true);
        if (DA != null) DA.destroy();
        if (LA != null) LA.destroy();
        super.onDestroy();
    }
}