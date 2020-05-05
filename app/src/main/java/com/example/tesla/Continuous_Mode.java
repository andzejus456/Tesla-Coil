package com.example.tesla;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Continuous_Mode extends AppCompatActivity {

    Button Back_Button, Start_Sweep_Button, Set_Frequency_Confirm_Button, Start_Frequency_Confirm_Button, Stop_Frequency_Confirm_Button, Level_To_Zero_Position_Button, Stop_Sweep_Button, Change_Sweep_Step_Button;
    EditText Frequency_Of_Output_Signal_Edit_Text, Start_Frequency_Edit_Text, Stop_Frequency_Edit_Text;
    SeekBar Output_Frequency_SeekBar, Sweep_Frequency_Bar, Duty_Cycle_SeekBar, Needed_Level_Seek_Bar;
    TextView Sweep_Frequency_Text_View, Duty_Cycle_Text_View, Needed_Level_Text_View, Sweep_Step_Text_View;

    private BluetoothController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_mode);
        controller = BluetoothController.getInstance();

        findViewByIdFunction();
        Commands();

    }

    @Override
    public void onBackPressed() {
        Change_To_MainActivity();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void Commands() {
        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_MainActivity();
            }
        });
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Set_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Frequency_Of_Output_Signal_Edit_Text.length()==0)
                {
                    controller.Frequency_Of_Output_Signal = controller.Start_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than Start Frequency", Toast.LENGTH_LONG).show();
                }
                else controller.Frequency_Of_Output_Signal = Long.parseLong(String.valueOf(Frequency_Of_Output_Signal_Edit_Text.getText()));
                if(controller.Frequency_Of_Output_Signal<controller.Start_Frequency)
                {
                    controller.Frequency_Of_Output_Signal = controller.Start_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than Start Frequency", Toast.LENGTH_LONG).show();
                }
                else if(controller.Frequency_Of_Output_Signal>controller.Max_Frequency)
                {
                    controller.Frequency_Of_Output_Signal = controller.Stop_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be larger than Stop Frequency", Toast.LENGTH_LONG).show();
                }
                Output_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Output_Signal);
                Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_OUTPUT_SIGNAL;
                message.arg1= (int) controller.Frequency_Of_Output_Signal;
                message.arg2= 2;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Start_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.START_FREQUENCY;
                if(Start_Frequency_Edit_Text.length()==0)
                {
                    controller.Start_Frequency = controller.Min_Frequency;
                    Start_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Frequency));
                    Toast.makeText(getApplicationContext(), "Minimum Start Frequency is 100kHz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    controller.Start_Frequency = Integer.parseInt(String.valueOf(Start_Frequency_Edit_Text.getText()));
                }

                if(controller.Start_Frequency<controller.Min_Frequency)
                {
                    controller.Start_Frequency = controller.Min_Frequency;
                    Start_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Frequency));
                    Toast.makeText(getApplicationContext(), "Minimum Start Frequency is 100kHz", Toast.LENGTH_LONG).show();
                }
                if(controller.Start_Frequency>=controller.Stop_Frequency)
                {
                    controller.Start_Frequency = controller.Stop_Frequency - 100000;
                    if(controller.Start_Frequency < controller.Min_Frequency) controller.Start_Frequency = controller.Min_Frequency;
                    Start_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Frequency));
                    Toast.makeText(getApplicationContext(), "Start Frequency can't be less than Stop Frequency", Toast.LENGTH_LONG).show();
                }
                message.arg1 =(int) controller.Start_Frequency;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
                Output_Frequency_SeekBar.setMin((int) controller.Start_Frequency);
                if(controller.Frequency_Of_Output_Signal<Output_Frequency_SeekBar.getMin())
                {
                    controller.Frequency_Of_Output_Signal = Output_Frequency_SeekBar.getMin();
                    Output_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Output_Signal);
                    Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                    Start_Frequency_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                }
            }
        });

        Stop_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.STOP_FREQUENCY;
                if(Stop_Frequency_Edit_Text.length()==0)
                {
                    controller.Stop_Frequency = controller.Max_Frequency;
                    Stop_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Frequency));
                    Toast.makeText(getApplicationContext(), "Maximum Start Frequency is 1000kHz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    controller.Stop_Frequency = Integer.parseInt(String.valueOf(Stop_Frequency_Edit_Text.getText()));
                }

                if(controller.Stop_Frequency>controller.Max_Frequency)
                {
                    controller.Stop_Frequency = controller.Max_Frequency;
                    Stop_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Frequency));
                    Toast.makeText(getApplicationContext(), "Maximum Start Frequency is 1000kHz", Toast.LENGTH_LONG).show();
                }
                if(controller.Start_Frequency>=controller.Stop_Frequency)
                {
                    controller.Stop_Frequency = controller.Start_Frequency + 100000;
                    Stop_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Frequency));
                    Toast.makeText(getApplicationContext(), "Stop Frequency can't be larger than Start Frequency", Toast.LENGTH_LONG).show();
                }
                message.arg1 =(int) controller.Stop_Frequency;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
                Output_Frequency_SeekBar.setMax((int) controller.Stop_Frequency);
                if(controller.Frequency_Of_Output_Signal>Output_Frequency_SeekBar.getMax())
                {
                    controller.Frequency_Of_Output_Signal = Output_Frequency_SeekBar.getMax();
                    Output_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Output_Signal);
                    Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                    Stop_Frequency_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                }
            }
        });

        Output_Frequency_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.Frequency_Of_Output_Signal = progress;
                Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_OUTPUT_SIGNAL;
                message.arg1= (int) (controller.Frequency_Of_Output_Signal);
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_OUTPUT_SIGNAL;
                message.arg1= (int) (controller.Frequency_Of_Output_Signal);
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Start_Sweep_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.SWEEP_FREQUENCY;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Sweep_Frequency_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.Sweep_Frequency = progress;
                Sweep_Frequency_Text_View.setText("Sweep Frequency: "+String.valueOf(controller.Sweep_Frequency));
                Message message=Message.obtain();
                message.what=controller.SWEEP_FREQUENCY_VALUE;
                message.arg1= controller.Sweep_Frequency;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message=Message.obtain();
                message.what=controller.SWEEP_FREQUENCY_VALUE;
                message.arg1= controller.Sweep_Frequency;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Duty_Cycle_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.Duty_Cycle = progress;
                Duty_Cycle_Text_View.setText("Duty Cycle: "+String.valueOf(controller.Duty_Cycle) + "%");
                Message message=Message.obtain();
                message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                message.arg1= controller.Duty_Cycle;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message=Message.obtain();
                message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                message.arg1= controller.Duty_Cycle;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Needed_Level_Seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar Needed_Level_Seek_Bar, int progress, boolean fromUser) {
                controller.Actual_Level = progress;
                Needed_Level_Text_View.setText("Level: " + String.valueOf(controller.Actual_Level));
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=controller.Actual_Level;
                message.arg2= 200;
                Needed_Level_Text_View.setText("Level: " + String.valueOf(controller.Actual_Level));
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar Needed_Level_Seek_Bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar Needed_Level_Seek_Bar) {
                Message message=Message.obtain();
                message.what=controller.LEVELING;
                message.arg1=controller.Actual_Level;
                message.arg2= 200;
                Needed_Level_Text_View.setText("Level: " + String.valueOf(controller.Actual_Level));
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Level_To_Zero_Position_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.LEVELING_TO_ZERO_POSITION;
                controller.Actual_Level = 0;
                Needed_Level_Text_View.setText("Level: " + String.valueOf(controller.Actual_Level));
                Needed_Level_Seek_Bar.setProgress(controller.Actual_Level);
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Stop_Sweep_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=Message.obtain();
                message.what=controller.SWEEP_STOP;
                message.arg1 = 1;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Change_Sweep_Step_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller.Sweep_Frequency_Step == 10) controller.Sweep_Frequency_Step = 100;
                else if(controller.Sweep_Frequency_Step == 100) controller.Sweep_Frequency_Step = 1000;
                else if(controller.Sweep_Frequency_Step == 1000) controller.Sweep_Frequency_Step = 1;
                else if(controller.Sweep_Frequency_Step == 1) controller.Sweep_Frequency_Step = 10;
                Sweep_Step_Text_View.setText("Sweep Step Frequency: "+String.valueOf(controller.Sweep_Frequency_Step));
                Message message=Message.obtain();
                message.what=controller.SWEEP_FREQUENCY_STEP;
                message.arg1 = controller.Sweep_Frequency_Step;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void findViewByIdFunction() {
        Start_Sweep_Button = (Button) findViewById(R.id.Start_Sweep_Button);
        Back_Button = (Button) findViewById(R.id.Back_Button);
        Frequency_Of_Output_Signal_Edit_Text = (EditText) findViewById(R.id.Frequency_Of_Output_Signal_Edit_Text);
        Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
        Start_Frequency_Edit_Text = (EditText) findViewById(R.id.Start_Frequency_Edit_Text);
        Start_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Frequency));
        Stop_Frequency_Edit_Text = (EditText) findViewById(R.id.Stop_Frequency_Edit_Text);
        Stop_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Frequency));
        Output_Frequency_SeekBar = (SeekBar) findViewById(R.id.Output_Frequency_SeekBar);
        Output_Frequency_SeekBar.setProgress((int) controller.Frequency_Of_Output_Signal);
        Output_Frequency_SeekBar.setMin((int) controller.Start_Frequency);
        Output_Frequency_SeekBar.setMax((int) controller.Stop_Frequency);
        Start_Frequency_Confirm_Button = (Button) findViewById(R.id.Start_Frequency_Confirm_Button);
        Stop_Frequency_Confirm_Button  = (Button) findViewById(R.id.Stop_Frequency_Confirm_Button);
        Set_Frequency_Confirm_Button  = (Button) findViewById(R.id.Set_Frequency_Confirm_Button);
        Sweep_Frequency_Bar = (SeekBar) findViewById(R.id.Sweep_Frequency_Bar);
        Sweep_Frequency_Bar.setProgress(controller.Sweep_Frequency);
        Sweep_Frequency_Text_View = (TextView) findViewById(R.id.Sweep_Frequency_Text_View);
        Sweep_Frequency_Text_View.setText("Sweep Frequency: "+String.valueOf(controller.Sweep_Frequency));
        Duty_Cycle_SeekBar = (SeekBar) findViewById(R.id.Duty_Cycle_SeekBar);
        Duty_Cycle_SeekBar.setProgress(controller.Duty_Cycle);
        Duty_Cycle_Text_View = (TextView) findViewById(R.id.Duty_Cycle_Text_View);
        Duty_Cycle_Text_View.setText("Duty Cycle: "+String.valueOf(controller.Duty_Cycle) + "%");
        Needed_Level_Seek_Bar = (SeekBar) findViewById(R.id.Needed_Level_Seek_Bar);
        Needed_Level_Seek_Bar.setProgress(controller.Actual_Level);
        Level_To_Zero_Position_Button = (Button) findViewById(R.id.Level_To_Zero_Position_Button);
        Needed_Level_Text_View = (TextView) findViewById(R.id.Needed_Level_Text_View);
        Needed_Level_Text_View.setText("Level: "+ String.valueOf(controller.Actual_Level));
        Change_Sweep_Step_Button = (Button) findViewById(R.id.Change_Sweep_Step_Button);
        Stop_Sweep_Button = (Button) findViewById(R.id.Stop_Sweep_Button);
        Sweep_Step_Text_View = (TextView) findViewById(R.id.Sweep_Step_Text_View);
        Sweep_Step_Text_View.setText("Sweep Step Frequency: "+String.valueOf(controller.Sweep_Frequency_Step));
    }

    private void Change_To_MainActivity(){
        Message message=Message.obtain();
        message.what=controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
