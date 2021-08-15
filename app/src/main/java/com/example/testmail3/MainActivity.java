package com.example.testmail3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etContent, etRecipient;
    EditText NameUser, NumberPhone;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Email and SMS sender");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            //Toast.makeText(this, "No Permissions" , Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
        else
        {
            //Toast.makeText(this, Environment.getExternalStorageDirectory().toString() , Toast.LENGTH_LONG).show();
        }

        etContent = (EditText) findViewById(R.id.etContent);
        etRecipient = (EditText)findViewById(R.id.etRecipient);
        NameUser = (EditText) findViewById(R.id.NameUser);
        NumberPhone = (EditText)findViewById(R.id.mobileNumber);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(); // Отправка сообщений на почту и на телефон

            }
        });
    }
    // Функция отправки сообщений
    private void sendMessage() {
        // Текст сообщения
        final String FulltextMessage=NameUser.getText().toString() +" "+ NumberPhone.getText().toString() + " "+ etContent.getText().toString();
        String NumberPhoneText= NumberPhone.getText().toString(); // Получение номера телефона
        sendSMS(NumberPhoneText, FulltextMessage); // Отправка СМС
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Отправка сообщения");
        dialog.setMessage("Пожалуйста, подождите...");
        dialog.show();
        // Поток для отправки смс и письма на почту
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("test@gmail.com", "test1234"); // Логин и пароль от почты
                    sender.sendMail("EmailSender App",
                            FulltextMessage,
                            "test@gmail.com", // Почта отправителя
                            etRecipient.getText().toString());
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Заявка отправлена!",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
    // Функция отправки СМС
    private void sendSMS(String number, String sms){
        //С помощью SMS менеджера отправляем сообщение и высвечиваем
        //Toast сообщение об успехе операции:
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, sms, null, null);
            Toast.makeText(getApplicationContext(), "Заявка отправлена!",Toast.LENGTH_LONG).show();
        }
        // В случае ошибки высвечиваем соответствующее сообщение:
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Заявка не отправлена!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}