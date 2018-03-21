package com.ft08.trailblazelearn.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ft08.trailblazelearn.Manifest;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChooseContributedItemActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView fileSelector;
    private EditText singleLineTextEditor;
    private EditText multiLineTextEditor;
    private Button mRecordButton;
    private Button mDocumentButton;
    private MediaRecorder mRecorder;
    private ProgressDialog mProgressDialog;
    private Button takePictureButton;
    private Button choosePdfButton;
    private Button recordAudioButton;
    private Button chooseImageButton;

    private Uri fileUri;
    private  String userName;
    private String mFileName =null;
    private static final int RC_IMAGE_PICKER=1;
    private static final int RC_DOCUMENT_PICKER=2;
    private static final int RC_PHOTO_PICKER=3;
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


        singleLineTextEditor = (EditText)findViewById(R.id.editTitleText);
        multiLineTextEditor = (EditText)findViewById(R.id.editBodyText);
        mRecordButton = (Button)findViewById(R.id.audioButton);
        mProgressDialog = new ProgressDialog(this);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName = mFileName + "/recorded_audio.3gp";
        userName = App.participant.getName();
        System.out.println(userName);
        Log.d("LIFECYCLE","initreferences completed");
        mRecordButton.setOnTouchListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contributed_item);
        initFireBaseDatabase();
        initReferences();
        takePictureButton = (Button) findViewById(R.id.takePhotoButton);
        choosePdfButton = (Button) findViewById(R.id.pdfButton);
        recordAudioButton =(Button) findViewById(R.id.audioButton);
        chooseImageButton = (Button) findViewById(R.id.selectButton);
        fileSelector = (ImageView)findViewById(R.id.imageView);
        if (ContextCompat.checkSelfPermission(ChooseContributedItemActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(ChooseContributedItemActivity.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile());
                System.out.println(fileUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, RC_PHOTO_PICKER);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 0) {

            if (grantResults.length > 0 ){

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePictureButton.setEnabled(true);

                } else {

                    takePictureButton.setEnabled(false);
                }
                if (grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    recordAudioButton.setEnabled(true);

                } else {

                    recordAudioButton.setEnabled(false);

                }
                if (grantResults[3] == PackageManager.PERMISSION_GRANTED){

                    chooseImageButton.setEnabled(true);
                    choosePdfButton.setEnabled(true);

                } else{

                    chooseImageButton.setEnabled(false);
                    choosePdfButton.setEnabled(false);

                }
            }
        }
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

    /*public void buttonCapturePhoto(View view){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile());
        System.out.println(fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RC_PHOTO_PICKER);


    }*/

    private static  File getOutputMediaFile(){
        File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CameraDemo");
        if (!mediaStorageDirectory.exists()){
            if(!mediaStorageDirectory.mkdirs()){
                return null;
            }
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return  new File(mediaStorageDirectory.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
    }

    public void buttonAttachDocumentClick(View view){

        String[] mimeTypes = {"application/pdf","text/plain"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"Select a pdf for upload."),RC_DOCUMENT_PICKER);
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

        } else if (requestCode == RC_DOCUMENT_PICKER && resultCode == RESULT_OK && data !=null && data.getData() != null){
            fileUri = data.getData();

            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                fileSelector.setImageBitmap(bm);
                fileSelector.setImageResource(R.drawable.ic_action_document_icon);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){

                fileSelector.setImageURI(fileUri);

        }
    }


    public String getImageExt (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void buttonUploadImageClick(final View view){

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

    public void buttonUploadDocumentClick(View view){
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
                    Toast.makeText(ChooseContributedItemActivity.this,"Upload Successful.", Toast.LENGTH_LONG).show();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    System.out.println(downloadUri.toString());
                    System.out.println(ContributedItem.DOCUMENT_TYPE);
                    ContributedItem item =new ContributedItem(ContributedItem.DOCUMENT_TYPE,userName,downloadUri.toString(),singleLineTextEditor.getText().toString(),multiLineTextEditor.getText().toString());
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startRecording();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mRecordButton.setTooltipText("Recording started...");
                Toast.makeText(ChooseContributedItemActivity.this,"Touch and hold the audio record button to start recording",Toast.LENGTH_LONG).show();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            stopRecording();

        }
        return false;
    }

    private void stopRecording() {

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadRecordedAudio();

    }

    private void uploadRecordedAudio() {

        mProgressDialog.setMessage("Uploading Audio...");
        mProgressDialog.show();
        StorageReference audioFilePath = storageReference.child("new_audio.3gp");
        Uri uri = Uri.fromFile(new File(mFileName));

        audioFilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();
                Toast.makeText(ChooseContributedItemActivity.this,"Upload Successful.", Toast.LENGTH_LONG).show();
                Uri downloadUri = taskSnapshot.getDownloadUrl();

                System.out.println(downloadUri.toString());
                System.out.println(ContributedItem.AUDIO_TYPE);

                ContributedItem item =new ContributedItem(ContributedItem.AUDIO_TYPE,userName,downloadUri.toString(),singleLineTextEditor.getText().toString(),multiLineTextEditor.getText().toString());
                databaseReference.push().setValue(item);



            }
        });

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();

    }
}
