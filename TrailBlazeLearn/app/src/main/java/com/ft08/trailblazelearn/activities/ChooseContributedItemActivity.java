package com.ft08.trailblazelearn.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ChooseContributedItemActivity extends AppCompatActivity {

    private ImageView fileSelector;
    private EditText singleLineTextEditor;
    private EditText multiLineTextEditor;
    private Button mRecordButton;
    private Button mDocumentButton;
    private MediaRecorder mRecorder;
    private ProgressDialog mProgressDialog;

    private Uri fileUri;
    private  String userName;
    private String mFileName =null;
    private static final int RC_IMAGE_PICKER=1;
    private static final int RC_PDF_PICKER=2;
    private static final String LOG_TAG = "Record_log";

    private DatabaseReference firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private String currentTrailId;
    private String currentStationId;

    private void initFireBaseDatabase() {

        currentStationId = SwipeTabsActivity.getCalledStationId();
        currentTrailId = SwipeTabsActivity.getCalledTrailId();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        databaseReference = firebaseDatabase.child(currentTrailId).child("Stations").child(currentStationId).child("contributedItems");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("contributed_items");
        Log.d("LIFECYCLE","+ initfirebase completed");
    }

    private void initReferences() {

        fileSelector = (ImageView)findViewById(R.id.imageView);
        singleLineTextEditor = (EditText)findViewById(R.id.editTitleText);
        multiLineTextEditor = (EditText)findViewById(R.id.editBodyText);
        mRecordButton = (Button)findViewById(R.id.audioButton);
        mProgressDialog = new ProgressDialog(this);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName = mFileName + "/recorded_audio.3gp";
        userName = App.participant.getName();
        System.out.println(userName);
        Log.d("LIFECYCLE","initreferences completed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contributed_item);

        initFireBaseDatabase();

        initReferences();

    }

    public void buttonAttachImageClick(View view) {
        /*if (mRecordLabel.getVisibility() == View.VISIBLE){
            mRecordLabel.setVisibility(View.GONE);
        }*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file for upload."), RC_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                fileSelector.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getImageExt (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void buttonUploadClick(final View view){

        //System.out.println("i am inside button upload method!");
        if (fileUri != null) {
            //System.out.println("image uri is not null!");

            mProgressDialog.setTitle("Uploading File.");
            mProgressDialog.show();
            //System.out.println("Progress dialog over!");
            StorageReference filepath = storageReference.child(fileUri.getLastPathSegment());
            filepath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Log.d("LIFECYCLE","success listener triggered");
                    Toast.makeText(ChooseContributedItemActivity.this,"Upload Successful.", Toast.LENGTH_LONG).show();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    System.out.println(downloadUri.toString());
                    System.out.println(ContributedItem.IMAGE_TYPE);
                    ContributedItem item =new ContributedItem(ContributedItem.IMAGE_TYPE,userName,downloadUri.toString(),singleLineTextEditor.getText().toString(),multiLineTextEditor.getText().toString());
                    databaseReference.push().setValue(item);

                }
            });
        }

        else{
            Toast.makeText(getApplicationContext(),"Please select a file to upload!",Toast.LENGTH_SHORT).show();
        }

    }

    public void returnHome(View view){

        super.onBackPressed();
    }


}
