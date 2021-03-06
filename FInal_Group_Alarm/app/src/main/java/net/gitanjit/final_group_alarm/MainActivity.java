package net.gitanjit.final_group_alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final int GALLERY_INTENT_CODE = 1023 ;
    TextView fullName,email,phone,verifyMsg;
    FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    //FirebaseDatabase rootNode;
    private String userId;
    Button resendCode;
    //Button resetPassLocal,changeProfileImage;
    private FirebaseUser user;
    ImageView profileImage;
    //DatabaseReference storageReference;
    private DatabaseReference reference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //phone = findViewById(R.id.profilePhone);
        //fullName = findViewById(R.id.profileName);
        //email = findViewById(R.id.profileEmail);
        //resetPassLocal = findViewById(R.id.resetPasswordLocal);

        //profileImage = findViewById(R.id.profileImage);
        //changeProfileImage = findViewById(R.id.changeProfile);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            user = fAuth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference().child("Users");
            userId = user.getUid();
            final TextView fullNameTextView = (TextView) findViewById(R.id.profileName);
            final TextView emailTextView = (TextView) findViewById(R.id.profileEmail);
            final TextView phoneTextView = (TextView) findViewById(R.id.profilePhone);
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                    if (userProfile != null) {
                        String fullName = userProfile.fullName;
                        String email = userProfile.email;
                        String phone = userProfile.phone;

                        fullNameTextView.setText(fullName);
                        emailTextView.setText(email);
                        phoneTextView.setText(phone);


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "something happened", Toast.LENGTH_LONG).show();
                }
            });


        }






        /*//fStore = FirebaseFirestore.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference("users");

        //StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");//reference
        DatabaseReference profileRef = reference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");//reference


        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);


        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }


        //DocumentReference documentReference = fStore.collection("users").document(userId);//rootNode
        //DatabaseReference documentReference;
        //documentReference = rootNode.getReference("users").child(userId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query getuserinfo = reference.orderByChild("user_id");
        getuserinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phone.setText(snapshot.child("phone").getValue(String.class));
                    fullName.setText(snapshot.child("fName").getValue(String.class));
                    email.setText(snapshot.child("email").getValue(String.class));
                    //email.setText(snapshot.getString("email"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*getuserinfo.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });*/


        /*resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                startActivity(i);
//

            }
        });


    }*/


    }


    public void addgrp(View view) {
        startActivity(new Intent(getApplicationContext(), Viewusers2.class));
    }
    public void userslist(View view) {
        startActivity(new Intent(getApplicationContext(), Viewusers2.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


}