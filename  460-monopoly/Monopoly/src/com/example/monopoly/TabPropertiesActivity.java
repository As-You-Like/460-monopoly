package com.example.monopoly;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.HostDevice;
import com.example.model.Tile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
		
		HostDevice.host.sendMessage(Message.REQUEST_PROPERTIES_DATA, "");
		
		/*Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.REQUEST_PROPERTIES_DATA_ACCEPT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				JSONArray jsonArr = null;
				activity.mGroudList.clear();
				try {
					jsonArr = new JSONArray(message);
					
					for (int i=0; i<jsonArr.length(); i++){
						String tileJSON = jsonArr.getString(0);
						JSONObject jsonObj = new JSONObject(tileJSON);
						TileProperty item = new TileProperty();
						item.setId(jsonObj.getString("id"));
						item.setName(jsonObj.getString("n"));
						item.setRegion(jsonObj.getString("r"));
						item.setValue(jsonObj.getString("v"));
						
						activity.mGroudList.add(item);
						activity.setChildList();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		});*/
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.REQUEST_TILE_DATA_ACCEPT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		/** 
		 * TileProperty item generate > mGroudList.add(item)
		 * */
		/*TileProperty item = new TileProperty();
		item.setName("Miller");
		item.setRegion("Freshman Dorms");
		item.setValue("$1000");
		
		mGroudList.add(item);*/

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
				HostDevice.host.sendMessage(Message.REQUEST_TILE_DATA, mGroudList.get(groupPosition).getId());
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
			mChildListContent.add(mGroudList.get(i).getRegion());
			mChildListContent.add(mGroudList.get(i).getValue());
			mChildList.add(mChildListContent);
		}
	}
	
	//private void setChildList(){
		
	//}
	
	// mGroudList add Item
	public void addItem(TileProperty item) {
		mGroudList.add(item);
		//setChildList();
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

		public String itemId;
		public String itemName; 
		public String itemRegion;
		public String itemValue; 
		
		public TileProperty() {
		}
		
		public String getId(){
			return itemId;
		}
		
		public void setId(String id){
			this.itemId = id;
		}

		public String getName() {
			return itemName;
		}

		public void setName(String name) {
			this.itemName = name;
		}

		public String getRegion() {
			return itemRegion;
		}

		public void setRegion(String data) {
			this.itemRegion = data;
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

	public static void populate() {
		TabPropertiesActivity.activity.clear();
		TabPropertiesActivity.activity.mChildList.clear();
		
		for (int i=0; i<CommandCardActivity.activity.propertyRegions.size(); i++){
			int region = CommandCardActivity.activity.propertyRegions.get(i);
			TileProperty property = TabPropertiesActivity.activity.new TileProperty();
			property.setName(Tile.REGION_NAMES[region]);
			TabPropertiesActivity.activity.addItem(property);
			
			TabPropertiesActivity.activity.mChildListContent = new ArrayList<String>();
			for (Tile t: CommandCardActivity.activity.properties.get(i)){
				TabPropertiesActivity.activity.mChildListContent.add(t.getName());
			}
			TabPropertiesActivity.activity.mChildList.add(TabPropertiesActivity.activity.mChildListContent);
		}
	}
	
	public void onResume()
	{
		super.onResume();
		if(SplashActivity.activity.cascadeQuitBool == true){
			activity.terminate();
		}

	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}


}
