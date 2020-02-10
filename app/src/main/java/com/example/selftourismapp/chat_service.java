package com.example.selftourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.Random;

public class chat_service extends AppCompatActivity implements RoomListener{

    private String channelID = "QVhpdSxPzG8esYq8";
    private String roomName = "observeable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private ListView messageView;
    private usser_message_adapter messageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);
        editText = (EditText)findViewById(R.id.editText);

        messageAdapter = new usser_message_adapter(this);
        messageView= (ListView)findViewById(R.id.message_view);
        messageView.setAdapter(messageAdapter);



        MemberData memberData = new MemberData (getRandomName(), getRandomColor());

        scaledrone = new Scaledrone(channelID, memberData);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone conenction on");
                scaledrone.subscribe(roomName, chat_service.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);

            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);

            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);

            }
        });



    }
    public void sendMessage(View view){
        String message = editText.getText().toString();
        if(message.length() > 0){
            scaledrone.publish("observeable-room", message);
            editText.getText().clear();
        }
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to the server");

    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);

    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage){

        final ObjectMapper mapper = new ObjectMapper();
        try{
            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
            boolean currentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
            final user_message message= new user_message (receivedMessage.getData().asText(), data, currentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    messageView.setSelection(messageView.getCount() - 1);

                }
            });
        } catch (JsonProcessingException e){

        }

    }

    private String getRandomName(){
        String[] adjs = {"jackson", "joy", "Reynald", "Royce"};
        String[] nouns = {"Dorothy", "Ronald", "Bloys", "Jimmy"};
        return (
                adjs[(int)Math.floor(Math.random()*adjs.length)]+ "-" + nouns[(int)Math.floor(Math.random()*nouns.length)]
                );
    }

    private String getRandomColor(){
        Random c = new Random();
        StringBuffer stringBuffer = new StringBuffer("#");
        while(stringBuffer.length() < 7){
            stringBuffer.append(Integer.toHexString(c.nextInt()));
        }
        return stringBuffer.toString().substring(0,7);

    }
}

class MemberData{
    private String Name;
    private String Color;

    public MemberData(String Name, String Color){
        this.Name = Name;
        this.Color = Color;
    }

    public MemberData(){

    }

    public String getName(){
        return Name;

    }

    public String getColor(){
        return Color;

    }
}
