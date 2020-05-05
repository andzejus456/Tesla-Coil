package com.example.tesla;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piano_Mode_Activity extends AppCompatActivity {

    private BluetoothController controller;

    Button C4, D4, E4, F4, G4, A4, B4, CD4, DE4, FG4, GA4, AB4, C5, D5, E5, F5, G5, A5, B5, CD5, DE5, FG5, GA5, AB5;
    SeekBar Volume_Bar;

    int Volume = 50;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano_mode);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        controller = BluetoothController.getInstance();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        controller.Duty_Cycle = 0;
        Message message1=Message.obtain();
        message1.what=controller.DUTY_OF_OUTPUT_SIGNAL;
        message1.arg1= controller.Duty_Cycle;
        message1.arg2= 200;
        controller.Write_Data_Callback.sendMessage(message1);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.Interrupt_Duty_Cycle = 50;
        Message message2=Message.obtain();
        message2.what=controller.DUTY_OF_INTERRUPT_SIGNAL;
        message2.arg1= (int) (controller.Interrupt_Duty_Cycle);
        message2.arg2= 200;
        controller.Write_Data_Callback.sendMessage(message2);

        findViewByIdFunction();
        Commands();
    }

    private void Commands() {
        Volume_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Volume = progress;
                controller.Duty_Cycle = Volume;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        C4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 261.63;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        D4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 293.66;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        E4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 329.66;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        F4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 349.23;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        G4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 392.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        A4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 440.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        B4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 493.88;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        CD4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 277.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        DE4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 311.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        FG4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 370.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        GA4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 415.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        AB4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 466.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        C5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 523.25;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        D5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 587.33;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        E5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 659.25;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        F5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 698.46;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        G5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 783.99;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        A5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 880.0;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        B5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 987.77;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        CD5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 554.37;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        DE5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 622.25;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        FG5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 739.99;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        GA5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 830.61;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });

        AB5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    float freq = (float) 932.33;
                    controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                    controller.Duty_Cycle =  Volume;
                    Message message=Message.obtain();
                    message.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                    message.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                    message.arg2 =  controller.Duty_Cycle;
                    controller.Write_Data_Callback.sendMessage(message);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    controller.Duty_Cycle = 0;
                    Message message=Message.obtain();
                    message.what=controller.DUTY_OF_OUTPUT_SIGNAL;
                    message.arg1= (int) (controller.Duty_Cycle);
                    message.arg2= 200;
                    controller.Write_Data_Callback.sendMessage(message);
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Message message = Message.obtain();
        message.what = controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Change_To_MainActivity();
    }
    private void findViewByIdFunction() {
        C4 = (Button) findViewById(R.id.C4);
        D4 = (Button) findViewById(R.id.D4);
        E4 = (Button) findViewById(R.id.E4);
        F4 = (Button) findViewById(R.id.F4);
        G4 = (Button) findViewById(R.id.G4);
        A4 = (Button) findViewById(R.id.A4);
        B4= (Button) findViewById(R.id.B4);
        CD4 = (Button) findViewById(R.id.CD4);
        DE4 = (Button) findViewById(R.id.DE4);
        FG4 = (Button) findViewById(R.id.FG4);
        GA4 = (Button) findViewById(R.id.GA4);
        AB4 = (Button) findViewById(R.id.AB4);
        C5 = (Button) findViewById(R.id.C5);
        D5 = (Button) findViewById(R.id.D5);
        E5 = (Button) findViewById(R.id.E5);
        F5 = (Button) findViewById(R.id.F5);
        G5 = (Button) findViewById(R.id.G5);
        A5 = (Button) findViewById(R.id.A5);
        B5= (Button) findViewById(R.id.B5);
        CD5 = (Button) findViewById(R.id.CD5);
        DE5 = (Button) findViewById(R.id.DE5);
        FG5 = (Button) findViewById(R.id.FG5);
        GA5 = (Button) findViewById(R.id.GA5);
        AB5 = (Button) findViewById(R.id.AB5);

        Volume_Bar = (SeekBar) findViewById(R.id.Volume_Bar);
    }

    private void Change_To_MainActivity(){
        Message message=Message.obtain();
        message.what=controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
