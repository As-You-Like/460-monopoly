package com.example.monopoly;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.Game;
import com.example.controllers.HostDevice;
import com.example.model.PlayerPiece;
import com.example.model.Tile;
import com.example.model.Unit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TabInteractActivity extends Activity {
	
	static TabInteractActivity activity;

	ArrayList<ArrayList<ChatMessage>> messageLists = new ArrayList<ArrayList<ChatMessage>>(4);
	ArrayList<ChatMessage> messageList;
	ChatAdapter chatAdapter;
	EditText mEdittext;
	Spinner spnPlayerNumber;
	ListView lv;
	int targetPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_interact);
		
		activity = this;
		
		spnPlayerNumber = (Spinner)findViewById(R.id.spnPlayerNumber);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int p=0; p<Device.player.length; p++){
			if (Device.player[p] != null){
				adapter.add("Interact with " + Device.player[p].name);
			} else {
				adapter.add("");
			}
		}
		spnPlayerNumber.setAdapter(adapter);
		spnPlayerNumber.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				targetPlayer = position;
				//messageList = messageLists.get(position);
				//chatAdapter = new ChatAdapter(activity, messageList);
				//lv.setAdapter(chatAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
			
		});

		mEdittext = (EditText) this.findViewById(R.id.edittext);
		lv = (ListView) this.findViewById(R.id.list_chat);

		// send a message list
		messageList = new ArrayList<ChatMessage>();
		messageList.add(new ChatMessage("Hello", false));
		messageList.add(new ChatMessage("Hi!", true));
		messageList.add(new ChatMessage("Wassup??", false));
		
		chatAdapter = new ChatAdapter(this, messageList);
		lv.setAdapter(chatAdapter);
		
		// send message
		addNewMessage(new ChatMessage("mmm, well...", true));
		
		//Trade Inform
		
		
		
	}
	
	public void clickEventGoTrade(View v){
		//if it's the current player's turn
		if (CommandCardActivity.turn == true){ //if it's your turn
			HostDevice.host.sendMessage(Message.TRADE_START, ""+this.targetPlayer);
			Intent intent = new Intent(this, TradeActivity.class);
			startActivity(intent);
		} else {
			CommandCardActivity.activity.createAlert("You can initiate a trade only during your turn");
		}
	}

	public void clickEventSendMessage(View v) {
		String newMessage = mEdittext.getText().toString().trim();
		if (newMessage.length() > 0) {
			mEdittext.setText("");
			addNewMessage(new ChatMessage(newMessage, true));
			new SendMessage().execute();
		}
	}

	private class SendMessage extends AsyncTask<Void, String, String> {
		@Override
		protected String doInBackground(Void... params) {
			
			String playerName = "Mike";
			String playerText = "Trade with me...";
			
			try {
				Thread.sleep(2000); // simulate a network call
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.publishProgress(String.format("%s started writing", playerName));
			try {
				Thread.sleep(2000); // simulate a network call
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return playerText;
		}

		@Override
		public void onProgressUpdate(String... v) {

			if (messageList.get(messageList.size() - 1).isStatusMessage)// check wether we have already added a status message
			{
				messageList.get(messageList.size() - 1).setMessage(v[0]); // update the status for that
				chatAdapter.notifyDataSetChanged();
				lv.setSelection(messageList.size() - 1);
			} else {
				addNewMessage(new ChatMessage(true, v[0])); // add new message, if there is noexisting status message
			}
		}

		@Override
		protected void onPostExecute(String text) {
			if (messageList.get(messageList.size() - 1).isStatusMessage)// check if there is any statusmessage, now remove it.
			{
				messageList.remove(messageList.size() - 1);
			}

			addNewMessage(new ChatMessage(text, false)); // add the orignal message from server.
		}

	}

	// host send to message..
	public void addNewMessage(ChatMessage m) {
		messageList.add(m);
		chatAdapter.notifyDataSetChanged();
		lv.setSelection(messageList.size() - 1);
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class
											// onBackPressed()
	}

	public class ChatAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<ChatMessage> mMessages;

		public ChatAdapter(Context context, ArrayList<ChatMessage> messages) {
			super();
			this.mContext = context;
			this.mMessages = messages;
		}

		@Override
		public int getCount() {
			return mMessages.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessages.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatMessage message = (ChatMessage) this.getItem(position);

			VHolder holder;
			if (convertView == null) {
				holder = new VHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.listrow_chat, parent, false);
				holder.message = (TextView) convertView
						.findViewById(R.id.message_text);
				convertView.setTag(holder);
			} else
				holder = (VHolder) convertView.getTag();

			holder.message.setText(message.getMessage());

			LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
			// check if it is a status message then remove background, and
			// change text color.
			if (message.isStatusMessage()) {
				holder.message.setBackgroundDrawable(null);
				lp.gravity = Gravity.LEFT;
				holder.message.setTextColor(0xffafdb5b);
			} else {
				// Check whether message is mine to show green background and
				// align to right
				if (message.isMine()) {
					holder.message
							.setBackgroundResource(R.drawable.chat_blue);
					lp.gravity = Gravity.RIGHT;
				}
				// If not mine then it is from sender to show orange background
				// and align to left
				else {
					holder.message
							.setBackgroundResource(R.drawable.chat_green);
					lp.gravity = Gravity.LEFT;
				}
				holder.message.setLayoutParams(lp);
				holder.message.setTextColor(0xff000000);
			}
			return convertView;
		}

		private class VHolder {
			TextView message;
		}

		@Override
		public long getItemId(int position) {
			// Unimplemented, because we aren't using Sqlite.
			return 0;
		}
	}
	
	public class ChatMessage {
		/**
		 * The content of the message
		 */
		String message;
		/**
		 * boolean to determine, who is sender of this message
		 */
		boolean isMine;
		/**
		 * boolean to determine, whether the message is a status message or
		 * not. it reflects the changes/updates about the sender is writing,
		 * have entered text etc
		 */
		boolean isStatusMessage;

		/**
		 * Constructor to make a Message object
		 */
		public ChatMessage(String message, boolean isMine) {
			super();
			this.message = message;
			this.isMine = isMine;
			this.isStatusMessage = false;
		}

		/**
		 * Constructor to make a status Message object consider the
		 * parameters are swaped from default Message constructor, not a
		 * good approach but have to go with it.
		 */
		public ChatMessage(boolean status, String message) {
			super();
			this.message = message;
			this.isMine = false;
			this.isStatusMessage = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isMine() {
			return isMine;
		}

		public void setMine(boolean isMine) {
			this.isMine = isMine;
		}

		public boolean isStatusMessage() {
			return isStatusMessage;
		}

		public void setStatusMessage(boolean isStatusMessage) {
			this.isStatusMessage = isStatusMessage;
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
