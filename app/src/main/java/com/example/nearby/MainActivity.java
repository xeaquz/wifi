package com.example.nearby;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class MainActivity extends AppCompatActivity {

    MessageListener mMessageListener;
    Message mMessage;
    Message mActiveMessage;

    Button btnTag, btnStart, btnStop;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Context context = this;

        btnTag = findViewById(R.id.btn_tag);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(view -> {
            publish("start");
        });


        mMessageListener = new MessageListener() {
            @Override

            public void onFound(Message message) {
                String msg = new String(message.getContent());
                Log.d("main", "Found message: " + new String(message.getContent()));
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(Message message) {
                String msg = new String(message.getContent());
                Log.d("main", "Lost sight of message: " + new String(message.getContent()));
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };

        mMessage = new Message("Hello World".getBytes());
    }

    @Override
    public void onStart() {
        super.onStart();

        Nearby.getMessagesClient(this).publish(mMessage);
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    public void onStop() {
        Nearby.getMessagesClient(this).unpublish(mMessage);
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);

        super.onStop();
    }

    private void publish(String message) {
        Log.i("main", "Publishing message: " + message);
        mActiveMessage = new Message(message.getBytes());
        Nearby.getMessagesClient(this).publish(mActiveMessage);
    }

    private void unpublish() {
        Log.i("main", "Unpublishing.");
        if (mActiveMessage != null) {
            Nearby.getMessagesClient(this).unpublish(mActiveMessage);
            mActiveMessage = null;
        }
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i("main", "Subscribing.");
        SubscribeOptions options = null;
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);

    }

    private void unsubscribe() {
        Log.i("main", "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
    }
}