package com.example.android.msa_at_fau_v3;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {

    EditText recipient;
    EditText subject;
    EditText email_content;
    Button send_email_button;


    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipient = view.findViewById(R.id.recipient);
        subject = view.findViewById(R.id.subject);
        email_content = view.findViewById(R.id.email_content);
        send_email_button = view.findViewById(R.id.send_email_button);

        send_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipient_string = recipient.getText().toString().trim();
                String subject_string = subject.getText().toString().trim();
                String email_string = email_content.getText().toString().trim();

                sendEmail(recipient_string, subject_string, email_string);
            }
        });

    }

    private void sendEmail(String recipient_string, String subject_string, String email_string) {
        Intent email_intent = new Intent(Intent.ACTION_SEND);
        email_intent.setData(Uri.parse("mailto:"));
        email_intent.setType("text/plain");

        email_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient_string});
        email_intent.putExtra(Intent.EXTRA_SUBJECT, subject_string);
        email_intent.putExtra(Intent.EXTRA_TEXT, email_string);

        try {
            startActivity(Intent.createChooser(email_intent, "Please Select an Email Client"));
        }
        catch (Exception e){
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
