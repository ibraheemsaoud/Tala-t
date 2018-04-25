package sy.UProject.talat;

public class DrawerItem {

	String ItemName, title;
	Integer imgResID;
	boolean isLast;

	public DrawerItem(String itemName) {
		super();
		ItemName = itemName;
		this.imgResID = null;
	}

	public DrawerItem(String itemName, Integer image) {
		super();
		this.ItemName = itemName;
		this.imgResID = image;
	}

	public DrawerItem(String itemName, Integer image, boolean isLast) {
		super();
		ItemName = itemName;
		this.isLast = isLast;
		this.imgResID = image;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public boolean isLast() {
		return isLast;
	}

	public Integer getImgRes() {
		return imgResID;
	}
}
