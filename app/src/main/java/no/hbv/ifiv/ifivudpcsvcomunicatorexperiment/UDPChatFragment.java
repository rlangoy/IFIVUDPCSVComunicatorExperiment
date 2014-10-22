package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import java.util.ArrayList;
import android.content.Context;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;


/**
 * Message is a Custom Object to encapsulate message information/fields
 *
 * @author Adil Soomro - https://github.com/AdilSoomro/Android-Speech-Bubble
 *
 */
class Message {
    /**
     * The content of the message
     */
    String message;
    /**
     * boolean to determine, who is sender of this message
     */
    boolean isMine;
    /**
     * boolean to determine, whether the message is a status message or not.
     * it reflects the changes/updates about the sender is writing, have entered text etc
     */
    boolean isStatusMessage;
    /**
     * Constructor to make a Message object
     */
    public Message(String message, boolean isMine) {
        super();
        this.message = message;
        this.isMine = isMine;
        this.isStatusMessage = false;
    }
    /**
     * Constructor to make a status Message object
     * consider the parameters are swaped from default Message constructor,
     * not a good approach but have to go with it.
     */
    public Message(boolean status, String message) {
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

/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 *
 * @author Adil Soomro  - https://github.com/AdilSoomro/Android-Speech-Bubble
 *
 */
class AwesomeAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Message> mMessages;
    public AwesomeAdapter(Context context, ArrayList<Message> messages) {
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
        Message message = (Message) this.getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.udp_chat_row, parent, false);
            holder.message = (TextView) convertView.findViewById(R.id.message_text);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.message.setText(message.getMessage());
        LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
//check if it is a status message then remove background, and change text color.
        if(message.isStatusMessage())
        {
            holder.message.setBackgroundDrawable(null);
            lp.gravity = Gravity.LEFT;
            holder.message.setTextColor(R.color.textFieldColor);
        }
        else
        {
//Check whether message is mine to show green background and align to right
            if(message.isMine())
            {
                holder.message.setBackgroundResource(R.drawable.speech_bubble_green);
                lp.gravity = Gravity.RIGHT;
            }
//If not mine then it is from sender to show orange background and align to left
            else
            {
                holder.message.setBackgroundResource(R.drawable.speech_bubble_orange);
                lp.gravity = Gravity.LEFT;
            }
            holder.message.setLayoutParams(lp);
            holder.message.setTextColor(R.color.textColor);
        }
        return convertView;
    }
    private static class ViewHolder
    {
        TextView message;
    }
    @Override
    public long getItemId(int position) {
//Unimplemented, because we aren't using Sqlite.
        return position;
    }
}



/**
 * Created by rune on 20.10.2014.
 */
public class UDPChatFragment extends SherlockFragment
{
    private UDPCom mUDPCom=null;
    private View mRootView;

    ArrayList<Message> messages= new ArrayList<Message>();;
    AwesomeAdapter adapter=null;

    private ListView mListView=null;

    public UDPChatFragment(UDPCom udpCom)
    {
        mUDPCom=udpCom;
    }

    //Called if port/ip address has changed
    public void setUdpCom(UDPCom udpCom)
    {
        if(mUDPCom!=null)
            mUDPCom.finalize();
        mUDPCom=udpCom;
    }

    void addNewMessage(Message m)
    {
        if(adapter==null)
            messages.add(m);
        else
        {
           if (messages.isEmpty()) {
               //Hide no text recieved
               TextView tex = (TextView) mRootView.findViewById(android.R.id.empty);
               tex.setVisibility(View.GONE);
           }

           messages.add(m);
           adapter.notifyDataSetChanged();
           mListView.setSelection(messages.size() - 1);
       }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_udp_chat, container, false);


        //init Array for Messages
        //messages = new ArrayList<Message>();

        //Attatch new listView
        mListView = (ListView) mRootView.findViewById(android.R.id.list);
        adapter = new AwesomeAdapter(mRootView.getContext(), messages);
        mListView.setAdapter(adapter);

        final Button button = (Button) mRootView.findViewById(R.id.btnSendMessage);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            { //Init UDP Communication class if not already initiated
                if(mUDPCom!=null)
                {
                    //Get entered text
                    TextView textUDPMessage=(TextView) mRootView.findViewById(R.id.textUDPMessage);
                    String strUDPMessage=textUDPMessage.getText().toString();
                    addNewMessage(new Message(strUDPMessage, true));
                    //Send text
                    mUDPCom.sendMessage(strUDPMessage);
                }
            }
        });

        return mRootView;
    }




}