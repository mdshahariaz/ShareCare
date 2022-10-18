package cseian.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private EditText username, fullname, email, password,id,phone;
    private Button registerButton;
    private TextView question;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog loader;
    private String onlineUserID = "";
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Registration");

        profileImage = findViewById(R.id.profileImage);
        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.regEmail);
        id=findViewById(R.id.reguiuid);
        phone=findViewById(R.id.regphone);
        password = findViewById(R.id.regPassword);
        registerButton = findViewById(R.id.RegisterBtn);
        question = findViewById(R.id.regPageQuestion);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String fullName = fullname.getText().toString();
                String emailText = email.getText().toString();
                String idText=id.getText().toString();
                String phoneText=phone.getText().toString();
                String passwordText = password.getText().toString();

                if (TextUtils.isEmpty(userName)){
                    username.setError("username is required");
                }
                if (TextUtils.isEmpty(fullName)){
                    fullname.setError("Full is required");
                }
                if (TextUtils.isEmpty(emailText)){
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(idText)){
                    id.setError("Email is required");
                }
                if (TextUtils.isEmpty(phoneText)){
                    phone.setError("Email is required");
                }
                if (TextUtils.isEmpty(passwordText)){
                    password.setError("Password is required");
                }
                if (resultUri == null){
                    Toast.makeText(RegistrationActivity.this, "Profile Image is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration failed " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                onlineUserID = mAuth.getCurrentUser().getUid();
                                reference = FirebaseDatabase.getInstance().getReference().child("users").child(onlineUserID);
                                Map hashMap = new HashMap();
                                hashMap.put("username", userName);
                                hashMap.put("fullname", fullName);
                                hashMap.put("id", onlineUserID);
                                hashMap.put("email", emailText);
                                hashMap.put("uiuid",idText);
                                hashMap.put("phone",phoneText);
                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Objects.requireNonNull( mAuth.getCurrentUser()).sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(RegistrationActivity.this, "Registered successfully. Please check your email for verification",
                                                                        Toast.LENGTH_LONG).show();
                                                                email.setText("");
                                                                password.setText("");
                                                            }else {
                                                                Toast.makeText(RegistrationActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(RegistrationActivity.this, "Failed to upload data " + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        loader.dismiss();
                                    }
                                });
                                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile images").child(onlineUserID);
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                                }catch (IOException e){
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20,byteArrayOutputStream);
                                byte[] data = byteArrayOutputStream.toByteArray();
                                UploadTask uploadTask = filePath.putBytes(data);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if (taskSnapshot.getMetadata().getReference() !=null){
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();
                                                    Map hashMap = new HashMap();
                                                    hashMap.put("profileimageurl", imageUrl);
                                                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(RegistrationActivity.this, "Profile Image added successfully", Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                Toast.makeText(RegistrationActivity.this, "Process failed "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                });

                                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();

                            }
                        }
                    });
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);
        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}