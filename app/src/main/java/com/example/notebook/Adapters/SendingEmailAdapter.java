package com.example.notebook.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.notebook.Ui.RegisterActivity;
import com.example.notebook.Ui.SettingsActivity;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendingEmailAdapter extends AsyncTask<Message , String , String > {
    private ProgressDialog progressDialog ;
    Context context ;

    public SendingEmailAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending Email.....");
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected String doInBackground(Message... messages) {
        try {
            Transport.send(messages[0]);
            return "Success" ;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error" ;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        if (s.equals("Success")){
            Toast.makeText(context , "message sent successfully..." , Toast.LENGTH_LONG).show();
            Intent intent =  new Intent(context , SettingsActivity.class) ;
            context.startActivity(intent);
        }else {
            Toast.makeText(context , "Something went wrong ?..." , Toast.LENGTH_LONG).show();
        }
    }
}
