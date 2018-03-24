package com.ft08.trailblazelearn.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.Toast;


import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.activities.SwipeTabsActivity;
import com.ft08.trailblazelearn.adapters.PostAdapter;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.ft08.trailblazelearn.models.Post;
import com.ft08.trailblazelearn.models.Trainer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.app.Activity.RESULT_OK;

public class FragmentB extends Fragment implements View.OnClickListener {

    private static final int RC_PHOTO_PICKER =  1;

    private String userName;
    private ListView listView;
    private Button sendButton;
    private List<Post> Discussions;
    private PostAdapter postAdapter;
    private EditText writeMessageEditText;
    private View fragmentView;
    private String currentTrailKey;
    private String currentStationId;
    private ImageButton sendImageButton;

    private DatabaseReference firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ChildEventListener childEventListener;
    private FirebaseUser user;

    public FragmentB() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFirebaseDatabase();
        fragmentView = inflater.inflate(R.layout.fragment_b, container, false);
        return fragmentView;
    }


    private void initFirebaseDatabase() {
        currentStationId = ((SwipeTabsActivity)getActivity()).getCalledStationId();
        currentTrailKey = ((SwipeTabsActivity)getActivity()).getCalledTrailKey();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.child(currentTrailKey).child("Stations").child(currentStationId).child("posts");
        storageReference = firebaseStorage.getReference().child("discussion_photos");
        user = FirebaseAuth.getInstance().getCurrentUser();
    }


    private void initReferences() {
        if(App.user instanceof Trainer) { userName = App.trainer.getName(); }
        else { userName = App.participant.getName(); }
        Discussions = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), R.layout.message_item, Discussions);
        listView = (ListView) fragmentView.findViewById(R.id.listView);
        listView.setAdapter(postAdapter);
        writeMessageEditText = (EditText) fragmentView.findViewById(R.id.writeMessageEditText);
        sendButton = (Button) fragmentView.findViewById(R.id.sendButton);
        sendImageButton = (ImageButton) fragmentView.findViewById(R.id.sendImageButton);
        sendButton.setOnClickListener(this);
        sendImageButton.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initReferences();
        attachTextChangedEventListener();
    }


    private void attachTextChangedEventListener() {
        writeMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(writeMessageEditText.getText().toString().trim().length() > 0) { sendButton.setEnabled(true);}
                else {
                    sendButton.setEnabled(false);
                    sendButton.setTextColor(Color.BLACK);
                }
            }
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}
        });
    }


    private void detachDatabaseListener() {
        if(childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }


    public void onClick(View view) {
        if(view.getId() == R.id.sendButton) {
            Post post = new Post(writeMessageEditText.getText().toString(), userName, null, user.getPhotoUrl().toString());
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
    public void onPause() {
        super.onPause();
        detachDatabaseListener();
        postAdapter.clear();
    }


    @Override
    public void onResume() {
        attachDatabaseListener();
        super.onResume();
    }


    private void attachDatabaseListener() {
        if(childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postAdapter.add(post);
                    postAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getAdapter().getCount() - 1);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER) {
            if(resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());

                photoRef.putFile(selectedImage)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText((SwipeTabsActivity)getContext(), "Uploaded Failed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnSuccessListener((SwipeTabsActivity)getContext(),
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    Post post = new Post(null, userName, downloadUrl.toString(), user.getPhotoUrl().toString());
                                    databaseReference.push().setValue(post);
                                }
                            });
            }
        }
    }
}