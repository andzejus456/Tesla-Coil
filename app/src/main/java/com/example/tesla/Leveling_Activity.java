package com.example.tesla;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class Leveling_Activity extends AppCompatActivity {

    int Actual_Level;
    SeekBar Needed_Level_Seek_Bar;
    Button One_Step_Down_Button, One_Step_Up_Button, Level_To_Zero_Position_Button, Back_Button;
    TextView Show_Needed_Level_Text;
    private BluetoothController controller;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leveling);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewByIdFunction();
        Commands();

        controller = BluetoothController.getInstance();
    }

    public void Commands() {
        Needed_Level_Seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar Needed_Level_Seek_Bar, int progress, boolean fromUser) {
                Actual_Level = progress;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=Actual_Level;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar Needed_Level_Seek_Bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar Needed_Level_Seek_Bar) {
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=Actual_Level;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        One_Step_Down_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Actual_Level<=0)) Actual_Level--;
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=Actual_Level;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                Needed_Level_Seek_Bar.setProgress(Actual_Level);
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        One_Step_Up_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Actual_Level>=6800)) Actual_Level++;
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=Actual_Level;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                Needed_Level_Seek_Bar.setProgress(Actual_Level);
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Level_To_Zero_Position_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.LEVELING_TO_ZERO_POSITION;
                Actual_Level = 0;
                Show_Needed_Level_Text.setText("Level: " + String.valueOf(Actual_Level));
                Needed_Level_Seek_Bar.setProgress(Actual_Level);
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_MainActivity();
            }
        });
    }

    private void findViewByIdFunction() {
        Needed_Level_Seek_Bar = (SeekBar) findViewById(R.id.Needed_Level_Seek_Bar);
        One_Step_Down_Button = (Button) findViewById(R.id.One_Step_Down_Button);
        One_Step_Up_Button = (Button) findViewById(R.id.One_Step_Up_Button);
        Level_To_Zero_Position_Button = (Button) findViewById(R.id.Level_To_Zero_Position_Button);
        Show_Needed_Level_Text = (TextView) findViewById(R.id.Show_Needed_Level_Text);
        Back_Button = (Button) findViewById(R.id.Back_Button);
    }
    private void Change_To_MainActivity(){
        Message message=Message.obtain();
        message.what=controller.IDLE;
        controller.Write_Data_Callback.sendMessage(message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


