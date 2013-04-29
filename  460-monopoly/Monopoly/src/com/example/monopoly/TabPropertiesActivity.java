package com.example.monopoly;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TabPropertiesActivity extends Activity { 

	public static TabPropertiesActivity activity = null;

	private ExpandableListView mListView;

	private ArrayList<TileProperty> mGroudList = null;
	private ArrayList<ArrayList<String>> mChildList = null;
	private ArrayList<String> mChildListContent = null;

	BaseExpandableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_properties);
		activity = this;
		mListView = (ExpandableListView) findViewById(R.id.property_listview);
		mGroudList = new ArrayList<TileProperty>();
		mChildList = new ArrayList<ArrayList<String>>();
		
		/** 
		 * TileProperty item generate > mGroudList.add(item)
		 * */
		TileProperty item = new TileProperty();
		item.setName("Freshman Dams");
		item.setOwned("owned : true");
		item.setValue("$1000");
		
		mGroudList.add(item);

		setChildList();
		
		adapter = new BaseExpandableAdapter(this, mGroudList, mChildList);
		mListView.setAdapter(adapter);

		// Group Click
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				//Toast.makeText(getApplicationContext(), "group click = " + groupPosition, Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Child Click
		mListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				//Toast.makeText(getApplicationContext(), "child click = " + childPosition, Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Group Close
		mListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				//Toast.makeText(getApplicationContext(), "group Collapse = " + groupPosition, Toast.LENGTH_SHORT).show();
			}
		});

		// Group Open
		mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				//Toast.makeText(getApplicationContext(), "group Expand = " + groupPosition, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setChildList(){
		mChildList.clear();
		for (int i = 0; i < mGroudList.size(); i++) {
			mChildListContent = new ArrayList<String>();
			mChildListContent.add(mGroudList.get(i).getOwned());
			mChildListContent.add(mGroudList.get(i).getValue());
			mChildList.add(mChildListContent);
		}
	}
	
	// mGroudList add Item
	public void addItem(TileProperty item) {
		mGroudList.add(item);
		setChildList();
	}

	// mGroudList remove Item by name
	public void removeItem(String name) {
		TileProperty item = new TileProperty();
		for (int i = 0; i < mGroudList.size(); i++) {
			if(mGroudList.get(i).getName().equals(name))
				item = mGroudList.get(i);
		}
		
		mGroudList.remove(item);
		setChildList();
	}

	// mGroudList Clear
	public void clear() {
		mGroudList.clear();
	}
	
	

	
	public class BaseExpandableAdapter extends BaseExpandableListAdapter{
	     
	    private ArrayList<TileProperty> groupList = null;
	    private ArrayList<ArrayList<String>> childList = null;
	    private LayoutInflater inflater = null;
	    private ViewHolder viewHolder = null;
	     
	    public BaseExpandableAdapter(Context c, ArrayList<TileProperty> gList, ArrayList<ArrayList<String>> cList){
	        super();
	        this.inflater = LayoutInflater.from(c);
	        this.groupList = gList;
	        this.childList = cList;
	    }
	     
	    // return group position
	    @Override
	    public TileProperty getGroup(int groupPosition) {
	        return groupList.get(groupPosition);
	    }
	 
	    // return group size
	    @Override
	    public int getGroupCount() {
	        return groupList.size();
	    }
	 
	    // return group id
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	 
	    // group view row
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	         
	        View v = convertView;
	         
	        if(v == null){
	            viewHolder = new ViewHolder();
	            v = inflater.inflate(R.layout.listrow_properties_parent, parent, false);
	            viewHolder.groupName = (TextView) v.findViewById(R.id.name);
	            viewHolder.iv_arrow = (ImageView) v.findViewById(R.id.image);
	            v.setTag(viewHolder);
	        }else{
	            viewHolder = (ViewHolder)v.getTag();
	        }
	         
	        // tap expanded
	        if(isExpanded){
	        	viewHolder.iv_arrow.setImageResource(R.drawable.upper_arrow);
	        }else{
	        	viewHolder.iv_arrow.setImageResource(R.drawable.under_arrow);
	        }
	         
	        viewHolder.groupName.setText(getGroup(groupPosition).getName());
	         
	        return v;
	    }
	     
	    // return child view
	    @Override
	    public String getChild(int groupPosition, int childPosition) {
	        return childList.get(groupPosition).get(childPosition);
	    }
	     
	    // return child size
	    @Override
	    public int getChildrenCount(int groupPosition) {
	        return childList.get(groupPosition).size();
	    }
	 
	    // return child id
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	 
	    // child view row
	    @Override
	    public View getChildView(int groupPosition, int childPosition,
	            boolean isLastChild, View convertView, ViewGroup parent) {
	         
	        View v = convertView;
	         
	        if(v == null){
	            viewHolder = new ViewHolder();
	            v = inflater.inflate(R.layout.listrow_properties_child, null);
	            viewHolder.tv_childName = (TextView) v.findViewById(R.id.name);
	            v.setTag(viewHolder);
	        }else{
	            viewHolder = (ViewHolder)v.getTag();
	        }
	         
	        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
	         
	        return v;
	    }
	 
	    @Override
	    public boolean hasStableIds() { return true; }
	 
	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
	     
	    class ViewHolder {
	        public ImageView iv_arrow;
	        public TextView groupName;
	        public TextView tv_childName;
	    }
	 
	}
	public class TileProperty implements Serializable {

		public static final long serialVersionUID = 1L;

		public String itemName; 
		public String itemOwned; 
		public String itemValue; 
		
		public TileProperty() {
		}

		public String getName() {
			return itemName;
		}

		public void setName(String name) {
			this.itemName = name;
		}

		public String getOwned() {
			return itemOwned;
		}

		public void setOwned(String data) {
			this.itemOwned = data;
		}
		public String getValue() {
			return itemValue;
		}
		
		public void setValue(String data) {
			this.itemValue = data;
		}


	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
