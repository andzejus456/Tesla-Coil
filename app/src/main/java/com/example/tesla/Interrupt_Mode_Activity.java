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

import java.io.OutputStream;

public class Interrupt_Mode_Activity extends AppCompatActivity {

    Button Back_Button, Set_Interrupt_Frequency_Confirm_Button, Start_Interrupt_Frequency_Confirm_Button, Stop_Interrupt_Frequency_Confirm_Button, Frequency_Of_Output_Signal_Confirm_Button;
    EditText Frequency_Of_Interrupt_Signal_Edit_Text, Start_Interrupt_Frequency_Edit_Text, Stop_Interrupt_Frequency_Edit_Text, Frequency_Of_Output_Signal_Edit_Text;
    SeekBar Interrupt_Frequency_SeekBar, Interrupt_Duty_Cycle_SeekBar;
    TextView Interrupt_Duty_Cycle_Text_View;

    private BluetoothController controller;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrupt_mode);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        controller = BluetoothController.getInstance();

        findViewByIdFunction();
        Commands();

    }

    @Override
    public void onBackPressed() {
        Change_To_MainActivity();
    }

    private void Commands() {
        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_To_MainActivity();
            }
        });

        Set_Interrupt_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Frequency_Of_Interrupt_Signal_Edit_Text.length()==0)
                {
                    controller.Frequency_Of_Interrupt_Signal = controller.Start_Interrupt_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than Start Frequency", Toast.LENGTH_LONG).show();
                }
                else controller.Frequency_Of_Interrupt_Signal = Long.parseLong(String.valueOf(Frequency_Of_Interrupt_Signal_Edit_Text.getText()));
                if(controller.Frequency_Of_Interrupt_Signal<controller.Start_Interrupt_Frequency)
                {
                    controller.Frequency_Of_Interrupt_Signal = controller.Start_Interrupt_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than Start Frequency", Toast.LENGTH_LONG).show();
                }
                else if(controller.Frequency_Of_Interrupt_Signal>controller.Max_Interrupt_Frequency)
                {
                    controller.Frequency_Of_Interrupt_Signal = controller.Stop_Interrupt_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be larger than Stop Frequency", Toast.LENGTH_LONG).show();
                }
                Interrupt_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Interrupt_Signal);
                Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                message.arg1= (int) controller.Frequency_Of_Interrupt_Signal;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Start_Interrupt_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Start_Interrupt_Frequency_Edit_Text.length()==0)
                {
                    controller.Start_Interrupt_Frequency = controller.Min_Interrupt_Frequency;
                    Start_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Minimum Start Frequency is " + String.valueOf(controller.Min_Interrupt_Frequency) + " Hz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    controller.Start_Interrupt_Frequency = Integer.parseInt(String.valueOf(Start_Interrupt_Frequency_Edit_Text.getText()));
                }

                if(controller.Start_Interrupt_Frequency<controller.Min_Interrupt_Frequency)
                {
                    controller.Start_Interrupt_Frequency = controller.Min_Interrupt_Frequency;
                    Start_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Minimum Start Frequency is " + String.valueOf(controller.Min_Interrupt_Frequency) + " Hz", Toast.LENGTH_LONG).show();
                }
                if(controller.Start_Interrupt_Frequency>=controller.Stop_Interrupt_Frequency)
                {
                    controller.Start_Interrupt_Frequency = controller.Stop_Interrupt_Frequency - 100;
                    if(controller.Start_Interrupt_Frequency<1) controller.Start_Interrupt_Frequency = controller.Min_Interrupt_Frequency;
                    Start_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Start Interrupt Frequency can't be less than Stop Interrupt Frequency", Toast.LENGTH_LONG).show();
                }
                Interrupt_Frequency_SeekBar.setMin((int) controller.Start_Interrupt_Frequency);
                if(controller.Frequency_Of_Interrupt_Signal<Interrupt_Frequency_SeekBar.getMin())
                {
                    controller.Frequency_Of_Interrupt_Signal = Interrupt_Frequency_SeekBar.getMin();
                    Interrupt_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Interrupt_Signal);
                    Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                    Start_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                }
            }
        });

        Stop_Interrupt_Frequency_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Stop_Interrupt_Frequency_Edit_Text.length()==0)
                {
                    controller. Stop_Interrupt_Frequency = controller.Max_Interrupt_Frequency;
                    Stop_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Maximum Start Frequency is "+ String.valueOf(controller.Max_Interrupt_Frequency) + " Hz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    controller.Stop_Interrupt_Frequency = Integer.parseInt(String.valueOf(Stop_Interrupt_Frequency_Edit_Text.getText()));
                }

                if(controller.Stop_Interrupt_Frequency>controller.Max_Interrupt_Frequency)
                {
                    controller.Stop_Interrupt_Frequency = controller.Max_Interrupt_Frequency;
                    Stop_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Maximum Start Frequency is "+ String.valueOf(controller.Max_Interrupt_Frequency) + " Hz", Toast.LENGTH_LONG).show();
                }
                if(controller.Start_Interrupt_Frequency>=controller.Stop_Interrupt_Frequency)
                {
                    controller.Stop_Interrupt_Frequency = controller.Start_Interrupt_Frequency + 100;
                    Stop_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Interrupt_Frequency));
                    Toast.makeText(getApplicationContext(), "Stop Frequency can't be larger than Start Frequency", Toast.LENGTH_LONG).show();
                }

                Interrupt_Frequency_SeekBar.setMax((int) controller.Stop_Interrupt_Frequency);
                if(controller.Frequency_Of_Interrupt_Signal>Interrupt_Frequency_SeekBar.getMax())
                {
                    controller.Frequency_Of_Interrupt_Signal = Interrupt_Frequency_SeekBar.getMax();
                    Interrupt_Frequency_SeekBar.setProgress((int)controller.Frequency_Of_Interrupt_Signal);
                    Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                    Stop_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                }
            }
        });

        Interrupt_Frequency_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.Frequency_Of_Interrupt_Signal = progress;
                Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                message.arg1= (int) (controller.Frequency_Of_Interrupt_Signal);
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                message.arg1= (int) (controller.Frequency_Of_Interrupt_Signal);
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Interrupt_Duty_Cycle_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controller.Interrupt_Duty_Cycle = progress;
                Interrupt_Duty_Cycle_Text_View.setText("Duty Cycle: "+String.valueOf(controller.Interrupt_Duty_Cycle) + "%");
                Message message=Message.obtain();
                message.what=controller.DUTY_OF_INTERRUPT_SIGNAL;
                message.arg1= controller.Interrupt_Duty_Cycle;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message=Message.obtain();
                message.what=controller.DUTY_OF_INTERRUPT_SIGNAL;
                message.arg1= controller.Interrupt_Duty_Cycle;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });

        Frequency_Of_Output_Signal_Confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Frequency_Of_Output_Signal_Edit_Text.length()==0)
                {
                    controller.Frequency_Of_Output_Signal = controller.Min_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than "+String.valueOf(controller.Min_Frequency)+" Hz", Toast.LENGTH_LONG).show();
                }
                else controller.Frequency_Of_Output_Signal = Long.parseLong(String.valueOf(Frequency_Of_Output_Signal_Edit_Text.getText()));
                if(controller.Frequency_Of_Output_Signal<controller.Min_Frequency)
                {
                    controller.Frequency_Of_Output_Signal = controller.Min_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be less than "+String.valueOf(controller.Min_Frequency)+" Hz", Toast.LENGTH_LONG).show();
                }
                else if(controller.Frequency_Of_Output_Signal>controller.Max_Frequency)
                {
                    controller.Frequency_Of_Output_Signal = controller.Max_Frequency;
                    Toast.makeText(getApplicationContext(), "Frequency can't be larger than "+String.valueOf(controller.Max_Frequency)+" Hz", Toast.LENGTH_LONG).show();
                }
                Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
                Message message=Message.obtain();
                message.what=controller.FREQUENCY_OF_OUTPUT_SIGNAL;
                message.arg1= (int) controller.Frequency_Of_Output_Signal;
                message.arg2= 200;
                controller.Write_Data_Callback.sendMessage(message);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void findViewByIdFunction() {
        Back_Button = (Button) findViewById(R.id.Back_Button);
        Frequency_Of_Interrupt_Signal_Edit_Text = (EditText) findViewById(R.id.Frequency_Of_Interrupt_Signal_Edit_Text);
        Frequency_Of_Interrupt_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Interrupt_Signal));
        Start_Interrupt_Frequency_Edit_Text = (EditText) findViewById(R.id.Start_Interrupt_Frequency_Edit_Text);
        Start_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Start_Interrupt_Frequency));
        Stop_Interrupt_Frequency_Edit_Text = (EditText) findViewById(R.id.Stop_Interrupt_Frequency_Edit_Text);
        Stop_Interrupt_Frequency_Edit_Text.setText(String.valueOf(controller.Stop_Interrupt_Frequency));
        Interrupt_Frequency_SeekBar = (SeekBar) findViewById(R.id.Interrupt_Frequency_SeekBar);
        Interrupt_Frequency_SeekBar.setProgress((int) controller.Frequency_Of_Interrupt_Signal);
        Start_Interrupt_Frequency_Confirm_Button = (Button) findViewById(R.id.Start_Interrupt_Frequency_Confirm_Button);
        Stop_Interrupt_Frequency_Confirm_Button  = (Button) findViewById(R.id.Stop_Interrupt_Frequency_Confirm_Button);
        Set_Interrupt_Frequency_Confirm_Button  = (Button) findViewById(R.id.Set_Interrupt_Frequency_Confirm_Button);
        Interrupt_Duty_Cycle_SeekBar = (SeekBar) findViewById(R.id.Interrupt_Duty_Cycle_SeekBar);
        Interrupt_Duty_Cycle_SeekBar.setProgress(controller.Interrupt_Duty_Cycle);
        Interrupt_Duty_Cycle_Text_View = (TextView) findViewById(R.id.Interrupt_Duty_Cycle_Text_View);
        Interrupt_Duty_Cycle_Text_View.setText("Duty Cycle: "+String.valueOf(controller.Interrupt_Duty_Cycle) + "%");
        Frequency_Of_Output_Signal_Confirm_Button = (Button) findViewById(R.id.Frequency_Of_Output_Signal_Confirm_Button);
        Frequency_Of_Output_Signal_Edit_Text = (EditText) findViewById(R.id.Frequency_Of_Output_Signal_Edit_Text);
        Frequency_Of_Output_Signal_Edit_Text.setText(String.valueOf(controller.Frequency_Of_Output_Signal));
    }

    private void Change_To_MainActivity(){
        Message message=Message.obtain();
        message.what=controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
