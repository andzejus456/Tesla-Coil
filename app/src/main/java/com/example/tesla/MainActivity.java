package com.example.tesla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button Connect_Button, Disconnect_Button, Leveling_Button, Continuous_Mode_Button, Interrupt_Mode_Button, Midi_Music_Mode_Button, Piano_Mode_Button;
    Intent btEnablingIntent;
    int requestCodeForeEnable;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothController controller;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        controller = BluetoothController.getInstance();
        findViewByIdFunction();
        Connect_To_The_Tesla_Coil();
        Disconnect();
        Commands();
/*
        InputStream inputStream = null;
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            for(int i = 0; i < 26; i++)
            {

                byte b = (byte) inputStream.read();
                System.out.println((char)b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
    public void Commands() {
        Leveling_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_Leveling_Activity();
            }
        });

        Continuous_Mode_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_Continuous_Mode_Activity();
            }
        });

        Interrupt_Mode_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_Interrupt_Mode_Activity();
            }
        });
        Midi_Music_Mode_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_Midi_Music_Mode_Activity();
            }
        });
        Piano_Mode_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_Piano_Mode_Activity();
            }
        });
    }
    private void Change_To_Piano_Mode_Activity() {
        if(controller.CONNECTION_STATUS) {
            Intent intent = new Intent(this, Piano_Mode_Activity.class);
            startActivity(intent);
            Message message = Message.obtain();
            message.what = controller.INTERRUPT_MODE_START;
            controller.Write_Data_Callback.sendMessage(message);
        }
        else {
            Toast.makeText(getApplicationContext(), "You must connect to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
        }
    }
    private void Change_To_Midi_Music_Mode_Activity() {
        if(controller.CONNECTION_STATUS) {
            //Message message = Message.obtain();
            //message.what = controller.INTERRUPT_MODE_START;
            //controller.Write_Data_Callback.sendMessage(message);
            Intent intent = new Intent(this, Midi_Music_Mode_Activity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You must connect to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
        }
    }
    private void Change_To_Interrupt_Mode_Activity() {
        if (controller.CONNECTION_STATUS) {
            Message message = Message.obtain();
            message.what = controller.INTERRUPT_MODE_START;
            controller.Write_Data_Callback.sendMessage(message);
            Intent intent = new Intent(this, Interrupt_Mode_Activity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You must connect to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
        }
    }
    private void Disconnect() {
        Disconnect_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = controller.disconnect();
                if(status == 1)
                    Toast.makeText(getApplicationContext(), "You have successfully disconnected from The Tesla Coil", Toast.LENGTH_LONG).show();
                else if (status == 0)
                    Toast.makeText(getApplicationContext(), "You have already disconnected from The Tesla Coil", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void findViewByIdFunction() {
        Connect_Button = (Button) findViewById(R.id.Connect_Button);
        Disconnect_Button = (Button) findViewById(R.id.Disconnect_Button);
        Leveling_Button = (Button) findViewById(R.id.Leveling_Button);
        Continuous_Mode_Button = (Button) findViewById(R.id.Continuous_Mode_Button);
        Interrupt_Mode_Button = (Button) findViewById(R.id.Interrupt_Mode_Button);
        Midi_Music_Mode_Button = (Button) findViewById(R.id.Midi_Music_Mode_Button);
        Piano_Mode_Button = (Button) findViewById(R.id.Piano_Mode_Button);

        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if(!controller.getBtAdapter().isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }
    }
    private void Connect_To_The_Tesla_Coil() {
        Connect_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = controller.connect();
                switch(status) {
                    case 1:
                        Toast.makeText(getApplicationContext(),"You have successfully connected to The Tesla Coil",Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        while(status == 0) {
                            startActivityForResult(btEnablingIntent,requestCodeForeEnable);
                            status = controller.connect();
                        }
                        break;
                    case -1:
                        Toast.makeText(getApplicationContext(),"Bluetooth does not support on this Device",Toast.LENGTH_LONG).show();
                        break;
                    case -2:
                        Toast.makeText(getApplicationContext(),"You have no paired devices. Pair to the Tesla Coil",Toast.LENGTH_LONG).show();
                        break;
                    case -3:
                        Toast.makeText(getApplicationContext(),"Connection is failed",Toast.LENGTH_LONG).show();
                        break;
                    case -4:
                        Toast.makeText(getApplicationContext(), "You have already connected to The Tesla Coil", Toast.LENGTH_LONG).show();
                        break;
                    case -5:
                        Toast.makeText(getApplicationContext(), "You must pair to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
                        break;
                } }
        });
    }
    private void Change_To_Leveling_Activity(){
        if(controller.CONNECTION_STATUS) {
            Intent intent = new Intent(this, Leveling_Activity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You must connect to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
        }
    }
    private void Change_To_Continuous_Mode_Activity() {
        if(controller.CONNECTION_STATUS) {
            Message message=Message.obtain();
            message.what=controller.CONTINUOUS_MODE_START;
            controller.Write_Data_Callback.sendMessage(message);
            Intent intent = new Intent(this, Continuous_Mode.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You must connect to the Tesla Coil firstly", Toast.LENGTH_LONG).show();
        }
    }
}
