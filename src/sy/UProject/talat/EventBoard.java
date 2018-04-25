package sy.UProject.talat;

import sy.UProject.UI.DesignFunctions;
import sy.UProject.UI.DialogAdapter;
import sy.UProject.UI.LoaderAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

public class EventBoard extends ActionBarActivity {

	public DialogAdapter DA, reg;
	public LoaderAdapter LA;
	private Toolbar toolbar;
	private RelativeLayout parent;
	private Button Add, Share;
	private String id;
	private int[] colors = { R.color.blue, R.color.red, R.color.green,
			R.color.orange, R.color.dblue, R.color.yellow6, R.color.silver },
			colors2 = { R.color.blue7, R.color.red7, R.color.green7,
					R.color.orange7, R.color.dblue7, R.color.yellow7,
					R.color.silver7 };
	private int colorPrimary, colorPrimaryDark, color2;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_board);

		// Typeface type =
		// Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf");
		Bundle bundle = getIntent().getExtras();
		if (!bundle.containsKey("id"))
			finish();
		id = bundle.getString("id");
		SQlite sql = new SQlite(EventBoard.this);
		Event event = sql.getEvent(id);
		sql.close();
		sql = null;

		if (event == null) {
			// fetch from net.
			finish();
		}

		colorPrimary = getResources().getColor(colors[event.getSection()]);
		colorPrimaryDark = getResources().getColor(colors2[event.getSection()]);
		color2 = getResources().getColor(R.color.black4);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null)
			setSupportActionBar(toolbar);
		toolbar.setTitle(event.getHeadline());
		toolbar.setBackgroundColor(colorPrimary);

		parent = (RelativeLayout) findViewById(R.id.ET_parent);

		Add = (Button) findViewById(R.id.ET_hang);
		Share = (Button) findViewById(R.id.ET_share);

		Add.setBackgroundColor(colorPrimaryDark);
		Share.setBackgroundColor(colorPrimaryDark);

		DesignFunctions.setPuddle(Add, colorPrimaryDark, color2);
		DesignFunctions.setPuddle(Share, colorPrimaryDark, color2);

		Add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DA = new DialogAdapter(EventBoard.this);
				DA.setDestroyable();
				DA.showHeadline();
				DA.setHeadlineText("Share via");
				DA.setHeadlineTextColor(colorPrimaryDark);
				DA.setHeadlineDividerColor(color2);
				DA.holder.oneButton.setVisibility(View.GONE);
				DA.setPadding(0, 0, 0, 3);
				DA.attach(parent);

				DA.setText(DialogAdapter.TEXT_CANCEL);

				Button group = new Button(EventBoard.this);
				Button facebook = new Button(EventBoard.this);
				Button email = new Button(EventBoard.this);

				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);

				group.setLayoutParams(lp);
				facebook.setLayoutParams(lp);
				email.setLayoutParams(lp);

				int color3 = getResources().getColor(R.color.white);

				DesignFunctions.setPuddle(group, color3, colorPrimaryDark);
				DesignFunctions.setPuddle(facebook, color3, colorPrimaryDark);
				DesignFunctions.setPuddle(email, color3, colorPrimaryDark);

				group.setBackgroundColor(color3);
				facebook.setBackgroundColor(color3);
				email.setBackgroundColor(color3);

				group.setTextColor(getResources().getColor(R.color.black7));
				facebook.setTextColor(getResources().getColor(R.color.black7));
				email.setTextColor(getResources().getColor(R.color.black7));

				group.setText("group");
				facebook.setText("facebook");
				email.setText("email");

				DA.add(group);
				DA.add(facebook);
				DA.add(email);

				DA.fire();

				DA.holder.dimmer.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						DA.destroy();
					}
				});
			}

		});
	}

	@Override
	protected void onResume() {
		// onCreate(null);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.welcome, menu);
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
		if (DA != null)
			if (DA.destroy())
				;
			else
				super.onBackPressed();
		else
			super.onBackPressed();
	}
}
