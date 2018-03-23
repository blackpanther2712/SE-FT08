package com.ft08.trailblazelearn.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class ChooseContributedItemActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView fileSelector;
    private EditText singleLineTextEditor;
    private EditText multiLineTextEditor;
    private MediaRecorder mRecorder;
    private ProgressDialog mProgressDialog;
    private ImageButton takePictureButton;
    private ImageButton choosePdfButton;
    private ImageButton recordAudioButton;
    private ImageButton chooseImageButton;
    private ImageButton recordVideoButton;
    private Button uploadPdfButton;
    private Button uploadImageButton;
    private FirebaseUser user;
    private String currentDate;
    private Uri fileUri;
    private Uri videoUri;
    private String timestamp;
    private String userName;
    private String mFileName = null;
    private static final int RC_IMAGE_PICKER = 1;
    private static final int RC_DOCUMENT_PICKER = 2;
    private static final int RC_PHOTO_PICKER = 3;
    private static final int RC_VIDEO_PICKER = 4;
    private static final String LOG_TAG = "Record_log";
    private DatabaseReference firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private String currentTrailId;
    private String currentStationId;
    float startX, startY, endX, endY, differenceX, differenceY;
    boolean buttonClick;
    Timer timerClick;


    // initializing Firebase database, fetching user's information along with the the current station and trail ids
    private void initFireBaseDatabase() {

        currentStationId = SwipeTabsActivity.getCalledStationId();
        currentTrailId = SwipeTabsActivity.getCalledTrailKey();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Trails");
        databaseReference = firebaseDatabase.child(currentTrailId).child("Stations").child(currentStationId).child("contributedItems");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("contributed_items");
        user = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void initReferences() {

        fileSelector = findViewById(R.id.imageView);
        singleLineTextEditor = findViewById(R.id.editTitleText);
        multiLineTextEditor = findViewById(R.id.editBodyText);
        chooseImageButton = findViewById(R.id.imageGalleryButton);
        takePictureButton = findViewById(R.id.takePhotoButton);
        recordAudioButton = findViewById(R.id.audioButton);
        choosePdfButton = findViewById(R.id.pdfButton);
        recordVideoButton = findViewById(R.id.videoButton);
        uploadImageButton = findViewById(R.id.uploadButton);
        uploadPdfButton = findViewById(R.id.uploadPdfButton);
        mProgressDialog = new ProgressDialog(this);
        timestamp = dateConverter();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName = mFileName + "/AUD_" + timestamp + ".3gp";
        userName = App.participant.getName();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateToday = Calendar.getInstance().getTime();
        currentDate = df.format(dateToday);
        recordAudioButton.setOnTouchListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contributed_item);
        initFireBaseDatabase();
        initReferences();

        if (ContextCompat.checkSelfPermission(ChooseContributedItemActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(ChooseContributedItemActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                uploadPdfButton.setEnabled(false);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, RC_PHOTO_PICKER);

            }
        });

        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                uploadPdfButton.setEnabled(false);
                uploadImageButton.setEnabled(false);
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File video_file = getOutputFilepath();
                videoUri = Uri.fromFile(video_file);
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(videoIntent, RC_VIDEO_PICKER);


            }
        });
    }

    //handle runtime user permissions for modifying app permission settings

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 0) {

            if (grantResults.length > 0) {

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
                if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    chooseImageButton.setEnabled(true);
                    choosePdfButton.setEnabled(true);

                } else {

                    chooseImageButton.setEnabled(false);
                    choosePdfButton.setEnabled(false);

                }
            }
        }
    }

    //this method is called to choose an image from the gallery
    public void buttonAttachImageClick(View view) {

        uploadPdfButton.setEnabled(false);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file for upload."), RC_IMAGE_PICKER);
    }

    // this method writes the image file to the local storage
    private static File getOutputMediaFile() {

        File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDirectory.exists()) {
            mediaStorageDirectory.mkdir();
            if (!mediaStorageDirectory.mkdirs()) {
                return null;
            }
        }

        String timestamp = dateConverter();
        return new File(mediaStorageDirectory.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
    }

    //this method writes a video file to the local storage
    private static File getOutputFilepath() {

        String file_name = Environment.getExternalStorageDirectory().getAbsolutePath();
        File folder = new File(file_name + "/video_library");
        if (!folder.exists()) {
            folder.mkdir();
        }
        String timestamp = dateConverter();
        return new File(folder, "VID_" + timestamp + ".mp4");

    }

    //this method is used to create date extension to files
    private static String dateConverter(){

        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }


    //  this method is used to attach a document, either text file or PDF
    public void buttonAttachDocumentClick(View view) {

        uploadImageButton.setEnabled(false);
        String[] mimeTypes = {"application/pdf", "text/plain"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
        if (mimeTypes.length > 0) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(intent, "Select a pdf for upload."), RC_DOCUMENT_PICKER);
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

        } else if (requestCode == RC_DOCUMENT_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                fileSelector.setImageBitmap(bm);
                fileSelector.setImageResource(R.drawable.ic_action_document_picture);

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            fileSelector.setImageURI(fileUri);

        } else if (requestCode == RC_VIDEO_PICKER && resultCode == RESULT_OK) {

            if (videoUri != null) {

                mProgressDialog.setMessage("Uploading Video...");
                mProgressDialog.show();
                StorageReference videoFilePath = storageReference.child("VIDEO_" + timestamp + ".mp4");
                videoFilePath.putFile(videoUri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChooseContributedItemActivity.this, "Uploaded Failed!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ChooseContributedItemActivity.this, "Upload Successful.", Toast.LENGTH_LONG).show();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        ContributedItem item = new ContributedItem(ContributedItem.VIDEO_TYPE, userName, downloadUri.toString(), singleLineTextEditor.getText().toString(), multiLineTextEditor.getText().toString(), user.getPhotoUrl().toString(), currentDate);
                        databaseReference.push().setValue(item);

                    }
                });

            }

        }

    }

    //this method is called to upload a photo taken via a camera or from gallery
    public void buttonUploadImageClick(final View view) {

        if (fileUri != null) {

            mProgressDialog.setTitle("Uploading File.");
            mProgressDialog.show();
            StorageReference filepath = storageReference.child(fileUri.getLastPathSegment());
            filepath.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChooseContributedItemActivity.this, "Uploaded Failed!", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChooseContributedItemActivity.this, "Upload Successful.", Toast.LENGTH_LONG).show();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    ContributedItem item = new ContributedItem(ContributedItem.IMAGE_TYPE, userName, downloadUri.toString(), singleLineTextEditor.getText().toString(), multiLineTextEditor.getText().toString(), user.getPhotoUrl().toString(), currentDate);
                    databaseReference.push().setValue(item);

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please select a file to upload!", Toast.LENGTH_SHORT).show();
        }

    }

    //this method is called to upload a document
    public void buttonUploadDocumentClick(View view) {
        if (fileUri != null) {

            mProgressDialog.setTitle("Uploading File.");
            mProgressDialog.show();
            StorageReference filepath = storageReference.child(fileUri.getLastPathSegment());
            filepath.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChooseContributedItemActivity.this, "Uploaded Failed!", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChooseContributedItemActivity.this, "Upload Successful.", Toast.LENGTH_LONG).show();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    ContributedItem item = new ContributedItem(ContributedItem.DOCUMENT_TYPE, userName, downloadUri.toString(), singleLineTextEditor.getText().toString(), multiLineTextEditor.getText().toString(), user.getPhotoUrl().toString(), currentDate);
                    databaseReference.push().setValue(item);

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please select a file to upload!", Toast.LENGTH_SHORT).show();
        }


    }

    // this method allows the user to return to the contributed item list
    public void returnHome(View view) {

        super.onBackPressed();
    }

    //this method handles touch event for recording audio
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //handling long and short touch events; short touch should not start recording.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
            buttonClick = true;
            timerClick = new Timer();
            timerClick.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (buttonClick) {
                        buttonClick = false;
                        startRecording();
                    }
                }
            }, 500);

            return true;

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            endX = event.getX();
            endY = event.getY();
            differenceX = Math.abs(startX - endX);
            differenceY = Math.abs(startY - endY);

            if (differenceX <= 5 && differenceY <= 5 && buttonClick) {
                Toast.makeText(ChooseContributedItemActivity.this, "Touch and hold the audio record button to start recording", Toast.LENGTH_LONG).show();

                buttonClick = false;

            } else {

                stopRecording();

            }
            return true;
        }
        return false;
    }

    //for stopping recording
    private void stopRecording() {

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadRecordedAudio();

    }

    //to upload the recorded audio
    private void uploadRecordedAudio() {

        mProgressDialog.setMessage("Uploading Audio...");
        mProgressDialog.show();
        StorageReference audioFilePath = storageReference.child("AUDIO_" + timestamp + ".3gp");

        Uri uri = Uri.fromFile(new File(mFileName));

        audioFilePath.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChooseContributedItemActivity.this, "Uploaded Failed!", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();
                Toast.makeText(ChooseContributedItemActivity.this, "Upload Successful.", Toast.LENGTH_LONG).show();
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                ContributedItem item = new ContributedItem(ContributedItem.AUDIO_TYPE, userName, downloadUri.toString(), singleLineTextEditor.getText().toString(), multiLineTextEditor.getText().toString(), user.getPhotoUrl().toString(), currentDate);
                databaseReference.push().setValue(item);

            }
        });

    }

    //to start recording
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
