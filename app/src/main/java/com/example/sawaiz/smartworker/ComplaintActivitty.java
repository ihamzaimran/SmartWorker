package com.example.sawaiz.smartworker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ComplaintActivitty extends AppCompatActivity {

    private Button sendBtn, backBtn;
    private EditText complaintTxt;
    private String complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_activitty);

        sendBtn = (Button)(findViewById(R.id.complaintSendBtn));
        backBtn = (Button)(findViewById(R.id.complaintBackBtn));

        complaintTxt = (EditText)(findViewById(R.id.compaint_txt));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint = complaintTxt.getText().toString();
                if(complaint.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter your complaint first.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ComplaintActivitty.this);
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Are you sure you want to registered a complaint?");

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference mydbRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Handyman").child(userId).child("ComplaintsList");
                            DatabaseReference myDBref1 = FirebaseDatabase.getInstance().getReference().child("Complaints").child("HandymanComplaints");
                            String ComplaintID = myDBref1.push().getKey();
                            mydbRef.child(ComplaintID).setValue(true);

                            Calendar calendar = Calendar.getInstance();
                            String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            String currenttime = format.format(calendar.getTime());


                            Map SavingComplaint = new HashMap();
                            SavingComplaint.put("HandymanId",userId);
                            SavingComplaint.put("Date",currentdate);
                            SavingComplaint.put("Time",currenttime);
                            SavingComplaint.put("ComplaintText",complaint);
                            SavingComplaint.put("Status","Unresolved");
                            myDBref1.child(ComplaintID).updateChildren(SavingComplaint);

                            complaintTxt.setText("");


                            Toast.makeText(ComplaintActivitty.this, "We've registered your complaint. Once reviewed you'll be contacted.", Toast.LENGTH_SHORT).show();


                        }
                    });


                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    alertDialog.show();


                }
            }
        });


    }
}
