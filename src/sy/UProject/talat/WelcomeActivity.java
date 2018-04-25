package sy.UProject.talat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.LightNotificationAdapter;
import sy.UProject.UI.LoaderAdapter;
import sy.UProject.UI.Utility;

public class WelcomeActivity extends ActionBarActivity {

    public LightNotificationAdapter LN;
    public DialogAdapter DA, reg[];
    public LoaderAdapter LA;
    RelativeLayout parent;
    public int section_count = 0, region_count = 0;
    private int[] colorNames;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    super.setTheme(R.style.AppTheme);
        if (!"".equals(Utility.readFile(this, "user_data"))) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return;
        }
        colorNames = new int[8];
        colorNames[0] = getResources().getColor(R.color.blue2);
        colorNames[1] = getResources().getColor(R.color.red2);
        colorNames[2] = getResources().getColor(R.color.red2);
        colorNames[3] = getResources().getColor(R.color.green2);
        colorNames[4] = getResources().getColor(R.color.orange2);
        colorNames[5] = getResources().getColor(R.color.dblue2);
        colorNames[6] = getResources().getColor(R.color.yellow2);
        colorNames[7] = getResources().getColor(R.color.silver2);

        setContentView(R.layout.activity_welcome);
        parent = (RelativeLayout) findViewById(R.id.welcome_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setVisibility(View.GONE);

        Typeface type = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_light.ttf");
        ((TextView) findViewById(R.id.welcome_logo)).setTypeface(type);

        Button login = (Button) findViewById(R.id.welcome_login);
        Button register = (Button) findViewById(R.id.welcome_register);

        reg = new DialogAdapter[5];
        login.setOnClickListener(new OnClickListener() {
            EditText username
                    ,
                    password;

            @Override
            public void onClick(View arg0) {
                if(DA == null)
                    DA = new DialogAdapter(WelcomeActivity.this).attach(parent).setHoldable();
                if (!DA.getHolded()) {
                    View view1 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_login,
                                    null));
                    DA.add(view1);
                    username = (EditText) view1.findViewById(R.id.welcome_login_username);
                    password = (EditText) view1.findViewById(R.id.welcome_login_password);
                }
                DA.fire();
                DA.holder.oneButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(username.getWindowToken(),
                                0);
                        Login login = new Login();
                        login.execute(username.getText().toString(), password
                                .getText().toString());
                    }
                });
                DA.setOnDialogDestroyedListener(new DialogAdapter.OnDialogDestroyedListener() {
                    @Override
                    public void onDestroyed() {
                        if(!DA.getHolded())
                            DA = null;
                    }
                });
            }
        });
        register.setOnClickListener(new OnClickListener() {
            EditText username = null
                    ,
                    email = null
                    ,
                    password = null
                    ,
                    password2 = null
                    ,
                    phone = null;
            private View view1
                    ,
                    view2
                    ,
                    view3
                    ,
                    view4
                    ,
                    view5;
            private final int[] sectionsNames = {R.id.WR_section0, R.id.WR_section1, R.id.WR_section2, R.id.WR_section3, R.id.WR_section4, R.id.WR_section5, R.id.WR_section6, R.id.WR_section7};
            private final int[] regionNames = {R.id.WR_region0, R.id.WR_region1, R.id.WR_region2, R.id.WR_region3};

            @Override
            public void onClick(View arg0) {
                if (reg[0] == null || !reg[0].getHolded()) {
                    region_count = 0;
                    section_count = 0;
                    DialogAdapter.initForm(reg, parent);
                    view1 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_reg0,
                                    null));
                    view2 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_reg1,
                                    null));
                    view3 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_reg2,
                                    null));
                    view4 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_reg3,
                                    null));
                    view5 = (WelcomeActivity.this
                            .getLayoutInflater().inflate(R.layout.welcome_reg4,
                                    null));
                    username = (EditText) view1.findViewById(R.id.welcome_reg_username);
                    email = (EditText) view1.findViewById(R.id.welcome_reg_email);
                    password = (EditText) view1.findViewById(R.id.welcome_reg_password);
                    password2 = (EditText) view1.findViewById(R.id.welcome_reg_confirm);
                    phone = (EditText) view2.findViewById(R.id.welcome_reg_phone);

                    final CheckBox[] sections = new CheckBox[8];
                    final CheckBox[] regions = new CheckBox[4];

                    reg[0].add(view1);
                    reg[1].add(view2);
                    reg[2].add(view3);
                    reg[3].add(view4);
                    reg[4].add(view5);

                    for (int i = 0; i < 8; i++) {
                        sections[i] = (CheckBox) view3.findViewById(sectionsNames[i]);
                        DesignFunctions.setPuddle(sections[i], getResources().getColor(
                                R.color.white), colorNames[i]);
                        sections[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View arg0) {
                                if (((CheckBox) arg0).isChecked()) {
                                    if (section_count == 0)
                                        reg[2].holder.next.setEnabled(true);
                                    section_count++;
                                } else {
                                    section_count--;
                                    if (section_count == 0)
                                        reg[2].holder.next.setEnabled(false);
                                }
                            }
                        });
                    }
                    for (int i = 0; i < 4; i++) {
                        regions[i] = (CheckBox) view4.findViewById(regionNames[i]);
                        DesignFunctions.setPuddle(regions[i], getResources().getColor(
                                R.color.white), getResources().getColor(
                                R.color.orange2));
                        regions[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View arg0) {
                                if (((CheckBox) arg0).isChecked()) {
                                    if (region_count == 0)
                                        reg[3].holder.next.setEnabled(true);
                                    region_count++;
                                } else {
                                    region_count--;
                                    if (region_count == 0)
                                        reg[3].holder.next.setEnabled(false);
                                }
                            }
                        });
                    }
                    reg[0].holder.next
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    boolean check = true;
                                    Locale locale = new Locale("en");
                                    if (!username.getText().toString()
                                            .toLowerCase(locale)
                                            .matches("[a-z\\d_]{3,20}")) {
                                        username.setError(getString(R.string.E_username));
                                        check = false;
                                    }
                                    if (!password.getText().toString()
                                            .matches("[A-Za-z0-9]{6,50}")) {
                                        password.setError(getString(R.string.E_password));
                                        check = false;
                                    }
                                    if (!password2
                                            .getText()
                                            .toString()
                                            .equals(password.getText()
                                                    .toString())) {
                                        password2
                                                .setError(getString(R.string.E_password_confirm));
                                        check = false;
                                    }
                                    if (!email
                                            .getText()
                                            .toString()
                                            .matches(
                                                    "\\S+@[\\w\\d.-]{2,}\\.[\\w]{2,6}")) {
                                        check = false;
                                        email.setError(getString(R.string.E_email));
                                    }
                                    if (check) {
                                        reg[0].onNextPressed(reg, 0);
                                        username.setError(null);
                                        email.setError(null);
                                        password.setError(null);
                                        password2.setError(null);
                                        if (phone.getText().length() == 0)
                                            reg[1].holder.next
                                                    .setText(getString(R.string.skip));
                                    }
                                }
                            });
                    phone.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {
                            if (phone.getText().toString().length() == 0)
                                reg[1].holder.next
                                        .setText(getString(R.string.skip));
                            else
                                reg[1].holder.next
                                        .setText(getString(R.string.next));
                        }

                        public void beforeTextChanged(CharSequence s,
                                                      int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                        }
                    });
                    reg[1].holder.next
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(
                                            reg[1].holder.body.getChildAt(0)
                                                    .getWindowToken(), 0);
                                    reg[1].onNextPressed(reg, 1);
                                }
                            });
                    reg[2].holder.next.setEnabled(false);
                    reg[3].holder.next.setEnabled(false);
                    reg[4].holder.next
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    Register reg = new Register();
                                    String str1 = "", str2 = "";
                                    for (int i = 0; i < 8; i++)
                                        if (sections[i].isChecked())
                                            str1 += 1;
                                        else
                                            str1 += 0;
                                    for (int i = 0; i < 4; i++)
                                        if (regions[i].isChecked())
                                            str2 += 1;
                                        else
                                            str2 += 0;
                                    reg.execute(username.getText().toString(),
                                            password.getText().toString(),
                                            email.getText().toString(), phone
                                                    .getText().toString(),
                                            str1, str2);
                                }
                            });
                }
                reg[0].setCurrentSlide(0);
                reg[0].fire();
                username.requestFocus();
            }
        });
        findViewById(R.id.welcome_guest).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Login lo = new Login();
                lo.execute("guest", "password");
            }
        });
    }

    private class Login extends AsyncTask<String, Integer, Integer> {
        String username, password, data;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            internet i = new internet();
            String resp;
            if (username.equals("guest"))
                resp = "1<BR />guest<SPC/>password<SPC/>saoudibraheem@gmail.com<SPC/>11111111<SPC/>0<SPC/>1111<SPC/>0994272587<SPC/> <SPC/>";
            else
                resp = i.login(username, password);
            Log.v("Internet Connection", "Doing - Logging_in - " + resp);
            if ("error".equals(resp)) {
                return -3707;
            } else {
                String[] spl = resp.split("\\<BR \\/\\>");
                if (spl.length > 1)
                    data = spl[1];
                return Integer.parseInt(spl[0]);
            }
        }

        @Override
        protected void onPreExecute() {
            LA = null;
            LA = new LoaderAdapter(WelcomeActivity.this);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(parent);
            LA.fire();
            if (DA != null)
                DA.hold();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            if (DA != null)
                DA.setInMovement(DialogAdapter.SLIDE_RIGHT);
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(WelcomeActivity.this);
                    LN.setTextColor(WelcomeActivity.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    DA.fire();
                    break;
                case 1:
                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                    Utility.WriteFile(WelcomeActivity.this, "user_data", data);
                    i.putExtra("firstTime", "login");
                    startActivity(i);
                    finish();
                    break;
                case 0:
                    DA.fire();
                    ((EditText) ((ViewGroup) DA.holder.body.getChildAt(0)).getChildAt(0))
                            .setError(getString(R.string.E_username_password));
                    break;
                default:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(WelcomeActivity.this);
                    LN.setTextColor(WelcomeActivity.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_unknown));
                    LN.attach(parent).fire();
                    DA.fire();
            }
            cancel(false);
        }
    }

    private class Register extends AsyncTask<String, Integer, Integer> {
        String username, password, email, phone, sections, region, data;

        @Override
        public Integer doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            username = params[0];
            password = params[1];
            email = params[2];
            phone = params[3];
            sections = params[4];
            region = params[5];
            internet i = new internet();
            String resp = i.register(username, password, email, phone,
                    sections, region);
            Log.v("Internet Connection", "Doing - Registring - " + resp);
            if ("error".equals(resp)) {
                return -3707;
            } else {
                String[] spl = resp.split("\\<BR \\/\\>");
                if (spl.length > 1)
                    data = spl[1];
                return Integer.parseInt(spl[0]);
            }
        }

        @Override
        protected void onPreExecute() {
            LA = new LoaderAdapter(WelcomeActivity.this);
            LA.setTextColor(getResources().getColor(R.color.dorange));
            LA.attach(parent);
            LA.fire();
            reg[4].onNextPressed(reg, 4);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            LA.destroy();
            reg[0].setCurrentSlide(0);
            switch (result) {
                case -3707:
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(WelcomeActivity.this);
                    LN.setTextColor(WelcomeActivity.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_internet));
                    LN.attach(parent).fire();
                    reg[0].setCurrentSlide(4);
                    reg[3].onNextPressed(reg, 3);
                    break;
                case 1:
                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                    Utility.WriteFile(WelcomeActivity.this, "user_data", data);
                    i.putExtra("firstTime", "register");
                    startActivity(i);
                    finish();
                    break;
                case 601:
                    ((EditText) ((ViewGroup) reg[0].holder.body.getChildAt(0)).getChildAt(0))
                            .setError(getString(R.string.E_username));
                    reg[0].fire();
                    break;
                case 602:
                    ((EditText) ((ViewGroup) reg[0].holder.body.getChildAt(1)).getChildAt(0))
                            .setError(getString(R.string.E_password));
                    reg[0].fire();
                    break;
                case 603:
                    ((EditText) ((ViewGroup) reg[0].holder.body.getChildAt(3)).getChildAt(0))
                            .setError(getString(R.string.E_email));//previously the number was at reg['i']
                    reg[0].fire();
                    break;
                case 606:
                    ((EditText) ((ViewGroup) reg[0].holder.body.getChildAt(0)).getChildAt(0))
                            .setError(getString(R.string.E_username_exists));
                    reg[0].fire();
                    break;
                default:
                    reg[0].fire();
                    if (LN != null)
                        LN.destroy();
                    LN = new LightNotificationAdapter(WelcomeActivity.this);
                    LN.setTextColor(WelcomeActivity.this.getResources().getColor(R.color.silver1)).setText(getString(R.string.E_unknown));
                    LN.attach(parent).fire();
            }
            cancel(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (!DialogAdapter.KillLast(this))
            super.onBackPressed();
    }
}
