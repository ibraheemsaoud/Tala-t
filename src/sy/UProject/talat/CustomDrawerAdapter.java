package sy.UProject.talat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;

	public CustomDrawerAdapter(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerItemHolder holder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			holder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			holder.item = (TextView) view.findViewById(R.id.drawer_itemName);
			holder.divider = view.findViewById(R.id.drawer_divider);

			view.setTag(holder);

		} else
			holder = (DrawerItemHolder) view.getTag();

		DrawerItem dItem = this.drawerItemList.get(position);
		holder.item.setText(dItem.getItemName());

		if (dItem.isLast() == false)
			holder.divider.setVisibility(View.GONE);
		else
			holder.divider.setVisibility(View.VISIBLE);

		if (dItem.getImgRes() != null) {
			SharedPreferences SP;
			SP = context.getApplicationContext().getSharedPreferences(
					"sy.UProject.hangouts", Context.MODE_PRIVATE);
			Drawable img = context.getResources().getDrawable(
					dItem.getImgRes());
			if ("en".equals(SP.getString("sy.UProject.hangouts.LANG", "en")))
				holder.item.setCompoundDrawablesWithIntrinsicBounds(img, null,
						null, null);
			else
				holder.item.setCompoundDrawablesWithIntrinsicBounds(null, null,
						img, null);
		}
		return view;
	}

	private static class DrawerItemHolder {
		TextView item;
		View divider;
	}
}