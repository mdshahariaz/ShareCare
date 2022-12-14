package cseian.com;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class AskAQuestionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spinner,spinner1,spinner2,spinner3,spinner4,spinner5,spinner6;
    private EditText questionBox,totalcost,shareableamount,departuretime;
    private ImageView imageView;
    private Button cancelBtn, postQuestionBtn;

    private String askedByname="";
    private DatabaseReference askedByRef;
    private ProgressDialog loader;
    private String myUrl="";
    StorageTask uploadTask;
    StorageReference storageReference;

    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ask_aquestion);

        // toolbar=findViewById(R.id.question_toolbar);

        getSupportActionBar().setTitle("Add Post");

        //toolbar= findViewById(R.id.question_toolbar);
        spinner= findViewById(R.id.spinner);
        spinner1= findViewById(R.id.spinner1);
        spinner2= findViewById(R.id.spinner2);
        spinner3= findViewById(R.id.spinner3);
        spinner4= findViewById(R.id.spinner4);
        spinner5= findViewById(R.id.spinner5);
        spinner6=findViewById(R.id.spinner6);
        questionBox=findViewById(R.id.question_text);
        totalcost=findViewById(R.id.totalcost);
        shareableamount=findViewById(R.id.shareableamount);
        departuretime=findViewById(R.id.departuretime);
        imageView=findViewById(R.id.questionImage);
        cancelBtn=findViewById(R.id.cancel);
        postQuestionBtn=findViewById(R.id.Post_Question);

        loader=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        onlineUserId= mUser.getUid();

        askedByRef= FirebaseDatabase.getInstance().getReference("users").child(onlineUserId);

        askedByRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                askedByname=snapshot.child("fullname").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference= FirebaseStorage.getInstance().getReference("questions");

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.location));
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.destination));
        ArrayAdapter<String> adapter2=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.vehicle));
        ArrayAdapter<String> adapter3=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.totalseat));
        ArrayAdapter<String> adapter4=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.availableseat));
        ArrayAdapter<String> adapter5=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));
        ArrayAdapter<String> adapter6=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ampm));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner2.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner3.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner4.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner5.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner6.getSelectedItem().equals("select topic")){
                    Toast.makeText(AskAQuestionActivity.this, "Please Select a valid topic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performValidation();
            }
        });

    }
    String getQuestionText(){
        return questionBox.getText().toString().trim();
    }
    String getTotalcost(){
        return totalcost.getText().toString().trim();
    }
    String getShareableamount(){
        return shareableamount.getText().toString().trim();
    }
    String getDeparturetime(){
        return departuretime.getText().toString().trim();
    }
    String getTopic(){
        return spinner.getSelectedItem().toString();
    }
    String getTopic1(){
        return spinner1.getSelectedItem().toString();
    }
    String getTopic2(){
        return spinner2.getSelectedItem().toString();
    }
    String getTopic3(){
        return spinner3.getSelectedItem().toString();
    }
    String getTopic4(){
        return spinner4.getSelectedItem().toString();
    }
    String getTopic5(){
        return spinner5.getSelectedItem().toString();
    }
    String getTopic6(){
        return spinner6.getSelectedItem().toString();
    }
    String mDate= DateFormat.getDateInstance().format(new Date());
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("question posts");

    private void performValidation() {
        if (getQuestionText().isEmpty()){
            questionBox.setError("Question Required!");
        }
        else if (getTopic().equals("select topic")){
            Toast.makeText(this, "Select a valid topic", Toast.LENGTH_SHORT).show();
        }

        else if (!getQuestionText().isEmpty() && !getTopic().equals("") && imageUri==null){
            uploadAQuestionWithNoImage();
        }else if(!getQuestionText().isEmpty() && !getTopic().equals("") && imageUri!=null){
            uploadAQuestionWithImage();
        }

    }
    private void startloader(){
        loader.setMessage("posting your question");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadAQuestionWithNoImage(){
        startloader();
        String postid= ref.push().getKey();

        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("postid",postid);
        hashMap.put("question",getQuestionText());
        hashMap.put("totalcost",getTotalcost());
        hashMap.put("shareableamount",getShareableamount());
        hashMap.put("departuretime",getDeparturetime());
        hashMap.put("publisher",onlineUserId);
        hashMap.put("location",getTopic());
        hashMap.put("destination",getTopic1());
        hashMap.put("vehicle",getTopic2());
        hashMap.put("totalseat",getTopic3());
        hashMap.put("shareableseat",getTopic4());
        hashMap.put("gender",getTopic5());
        hashMap.put("ampm",getTopic6());
        hashMap.put("askedby",askedByname);
        hashMap.put("date",mDate);

        ref.child(postid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AskAQuestionActivity.this,"Question Posted Successfully",Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                    startActivity(new Intent(AskAQuestionActivity.this,HomeActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(AskAQuestionActivity.this, "Could not upload Image"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }
            }
        });
    }
    private void uploadAQuestionWithImage(){
        startloader();
        final StorageReference fileReferance;
        fileReferance= storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        uploadTask=fileReferance.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isComplete()){
                    throw task.getException();
                }
                return fileReferance.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Uri downloadUri= (Uri) task.getResult();
                    myUrl=downloadUri.toString();
                    String postid= ref.push().getKey();

                    HashMap<String,Object>hashMap=new HashMap<>();
                    hashMap.put("postid",postid);
                    hashMap.put("question",getQuestionText());
                    hashMap.put("totalcost",getTotalcost());
                    hashMap.put("shareableamount",getShareableamount());
                    hashMap.put("departuretime",getDeparturetime());
                    hashMap.put("publisher",onlineUserId);
                    hashMap.put("location",getTopic());
                    hashMap.put("destination",getTopic1());
                    hashMap.put("vehicle",getTopic2());
                    hashMap.put("totalseat",getTopic3());
                    hashMap.put("shareableseat",getTopic4());
                    hashMap.put("gender",getTopic5());
                    hashMap.put("ampm",getTopic6());
                    hashMap.put("askedby",askedByname);
                    hashMap.put("questionimage",myUrl);
                    hashMap.put("date",mDate);

                    ref.child(postid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AskAQuestionActivity.this,"Question Posted Successfully",Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                                startActivity(new Intent(AskAQuestionActivity.this,HomeActivity.class));
                                finish();
                            }else {
                                Toast.makeText(AskAQuestionActivity.this, "could not upload Image"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AskAQuestionActivity.this, "Failed to upload the question", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}