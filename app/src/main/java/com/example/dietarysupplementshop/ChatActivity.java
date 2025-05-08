package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietarysupplementshop.adapter.MessageAdapter;
import com.example.dietarysupplementshop.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Thread Thread1 = null;
    private volatile boolean isRunning = true;
    private String SERVER_IP;
    public static final int SERVER_PORT = 8080;

    private RecyclerView rvMessages;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText etMessage;
    private Button btnSend;
    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageList);
        rvMessages.setAdapter(messageAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        try {
            SERVER_IP = getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            // TODO: Show an error message to the user
        }

        btnSend.setOnClickListener(v -> sendMessage());
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        startConnection();
    }

    private void startConnection() {
        isRunning = true;
        Thread1 = new Thread(new Thread1());
        Thread1.start();
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Update UI and message list with the new message
            Message message = new Message(messageText, true);
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            rvMessages.scrollToPosition(messageList.size() - 1);

            // Clear input field
            etMessage.setText("");

            // Send message using the networking thread
            new Thread(new Thread3(messageText)).start();
        }
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }

    class Thread1 implements Runnable {
        public void run() {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(() -> {
                            // Update the RecyclerView with the new message
                            Message receivedMessage = new Message(message, false);
                            messageList.add(receivedMessage);
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            rvMessages.scrollToPosition(messageList.size() - 1);
                        });
                    } else {
                        // Connection was lost, attempt to reconnect
                        isRunning = false;
                        runOnUiThread(() -> {
                            handleDisconnection();
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void handleDisconnection() {
            Toast.makeText(getApplicationContext(), "Disconnection with server!", Toast.LENGTH_SHORT).show();
        }
    }

    class Thread3 implements Runnable {
        private final String message;
        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            if (output != null && !output.checkError()) {
                output.println(message);
            } else {
                runOnUiThread(() -> {
                    // Show an error message to the user or handle sending failure
                    handleSendingError();
                });
            }
        }
        private void handleSendingError() {
            Toast.makeText(getApplicationContext(), "Error to send message with server!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
