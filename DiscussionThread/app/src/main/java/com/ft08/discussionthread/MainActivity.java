package com.ft08.discussionthread;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER =  1;

    private String userName;
    private ListView listView;
    private Button sendButton;
    private List<Post> Discussions;
    private PostAdapter postAdapter;
    private EditText writeMessageEditText;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ChildEventListener childEventListener;


    private void initFirebaseDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("discussion_photos");
    }


    private void initReferences() {
        Discussions = new ArrayList<>();
        postAdapter = new PostAdapter(this, R.layout.message_item, Discussions);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(postAdapter);
        writeMessageEditText = (EditText) findViewById(R.id.writeMessageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
    }


    private void attachTextChangedEventListener() {
        writeMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0) { sendButton.setEnabled(true);}
                else { sendButton.setEnabled(false);}
            }
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
        postAdapter.clear();
    }

    @Override
    protected void onResume() {
        attachDatabaseListener();
        super.onResume();
    }

    private void attachDatabaseListener() {
        if(childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("APP", "New Message Added");
                    Post post = dataSnapshot.getValue(Post.class);
                    postAdapter.add(post);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER) {
            if(resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());
                photoRef.putFile(selectedImage).addOnSuccessListener(this,
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Post post = new Post(null, userName, downloadUrl.toString());
                        databaseReference.push().setValue(post);
                    }
                });
            }
        }
    }


    private void detachDatabaseListener() {
        if(childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }


    public void onClick(View view) {
        if(view.getId() == R.id.sendButton) {
            Post post = new Post(writeMessageEditText.getText().toString(), userName, null);
            databaseReference.push().setValue(post);
            writeMessageEditText.setText("");
        }
        else if(view.getId() == R.id.sendImageButton) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete Action Using:"), RC_PHOTO_PICKER);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = "anonymous";

        initFirebaseDatabase();
        initReferences();
        attachDatabaseListener();
        attachTextChangedEventListener();
    }
}
