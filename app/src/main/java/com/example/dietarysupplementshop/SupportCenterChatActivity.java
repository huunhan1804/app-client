package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.ChatAdapter;
import com.example.dietarysupplementshop.model.ChatMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SupportCenterChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private ImageButton btnSend;

    private List<ChatMessage> messageList;
    private ChatAdapter chatAdapter;

    private String currentUserId = "user_123";     // giả lập user đăng nhập
    private String receiverId = "admin_1";         // admin nhận
    private String conversationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_support_center);
        conversationId = (currentUserId.compareTo(receiverId) < 0)
                ? currentUserId + "__" + receiverId
                : receiverId + "__" + currentUserId;

        recyclerView = findViewById(R.id.recycler_chat);
        edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Gọi phương thức để lắng nghe tin nhắn
        listenForMessages();

        btnSend.setOnClickListener(v -> {
            String messageText = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText);
                edtMessage.setText("");
            }
        });

        android.util.Log.d("CHAT_DEBUG", "👤 currentUserId = " + currentUserId);
        android.util.Log.d("CHAT_DEBUG", "👤 adminId = " + receiverId);

    }


private void sendMessage(String messageText) {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats").child(conversationId);
    String messageId = ref.push().getKey();

    ChatMessage message = new ChatMessage(
            messageId,
            currentUserId,
            receiverId,
            messageText,
            System.currentTimeMillis()
    );

    ref.child(messageId).setValue(message).addOnSuccessListener(aVoid -> {
        Log.d("CHAT", "Gửi thành công: " + messageText);
    }).addOnFailureListener(e -> {
        Log.e("CHAT", "Gửi thất bại: " + e.getMessage());
    });
}



    private void listenForMessages() {
        FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(conversationId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            ChatMessage message = child.getValue(ChatMessage.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        android.util.Log.e("CHAT_DEBUG", "❌ Lỗi Firebase: " + error.getMessage());
                    }
                });
    }
}
