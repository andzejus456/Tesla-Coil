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

public class Midi_Music_Mode_Activity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private BluetoothController controller;

    Button Back_Button, Load_File_Button, Start_Play_Button, Stop_Play_Button;
    TextView Music_Name_Text_View, Music_Speed_Text_View;
    SeekBar Music_Speed_Bar;
    ArrayList music = new ArrayList();
    ArrayList music_time = new ArrayList();
    ArrayList music_command = new ArrayList();
    ArrayList music_frequency = new ArrayList();
    ArrayList music_velocity = new ArrayList();
    int Volume = 50;
    int length = 0;
    boolean Play_Stop = true;
    boolean playing = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midi_music_mode);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        controller = BluetoothController.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        findViewByIdFunction();
        Commands();
    }

    private void Commands() {
        Load_File_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        Start_Play_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = controller.INTERRUPT_MODE_START;
                controller.Write_Data_Callback.sendMessage(message);
                Play_Stop = false;
                if(!playing)
                {
                    ExampleRunnable runnable = new ExampleRunnable();
                    new Thread(runnable).start();
                }
            }
        });

        Stop_Play_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = controller.IDLE_MODE_START;
                controller.Write_Data_Callback.sendMessage(message);
                Play_Stop = true;
            }
        });

        Music_Speed_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Volume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        Back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = controller.IDLE_MODE_START;
                controller.Write_Data_Callback.sendMessage(message);
                Play_Stop = true;
                Change_To_MainActivity();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Message message = Message.obtain();
        message.what = controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Play_Stop = true;
        Change_To_MainActivity();
    }
    class ExampleRunnable implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.Interrupt_Duty_Cycle = 50;
            Message message=Message.obtain();
            message.what=controller.DUTY_OF_INTERRUPT_SIGNAL;
            message.arg1= controller.Interrupt_Duty_Cycle;
            message.arg2= 200;
            controller.Write_Data_Callback.sendMessage(message);
            playing = true;
            for(int i = 0; i<(length-1); i++)
            {
                if(!Play_Stop)
                {
                    int delta_time = ((int) (music_time.get(i+1))) - ((int) (music_time.get(i)));
                    if(delta_time!=0)
                    {
                        if ((boolean) music_command.get(i))
                        {
                            float freq = (float) music_frequency.get(i);
                            float duty = (float) music_velocity.get(i)*Volume*3;
                            controller.Frequency_Of_Interrupt_Signal = (long) (freq);
                            controller.Duty_Cycle =  (int) duty;
                            Message message_midi=Message.obtain();
                            message_midi.what = controller.FREQUENCY_OF_INTERRUPT_SIGNAL;
                            message_midi.arg1 = (int) controller.Frequency_Of_Interrupt_Signal;
                            message_midi.arg2 =  controller.Duty_Cycle;
                            controller.Write_Data_Callback.sendMessage(message_midi);
                        } else {
                        }
                    }
                    try {
                        Thread.sleep((int) ((float)((delta_time))*0.3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    break;
                }
            }
            playing = false;

            Message message_midi=Message.obtain();
            message_midi.what = controller.IDLE_MODE_START;
            controller.Write_Data_Callback.sendMessage(message_midi);
        }
    }
    private void findViewByIdFunction() {
        Back_Button = (Button) findViewById(R.id.Back_Button);
        Load_File_Button = (Button) findViewById(R.id.Load_File_Button);
        Start_Play_Button = (Button) findViewById(R.id.Start_Play_Button);
        Stop_Play_Button = (Button) findViewById(R.id.Stop_Play_Button);
        Music_Name_Text_View = (TextView) findViewById(R.id.Music_Name_Text_View);
        Music_Speed_Bar = (SeekBar) findViewById(R.id.Music_Speed_Bar);
        Music_Speed_Text_View = (TextView) findViewById(R.id.Music_Speed_Text_View);
    }
    private String readText(String input){
        File file = new File(Environment.getExternalStorageDirectory(), input);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
                music.add(line);
                length++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if(path.contains("emulated")){
                    path = path.substring(path.indexOf("0") + 1);
                }
                readText(path);
                filter_file();
                Music_Name_Text_View.setText(path.substring(0, path.indexOf(".")));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private String Value_To_Frequency(String value){
        switch(Integer.parseInt(value))
        {
            case 0:
                return "8.18";
            case 1:
                return "8.66";
            case 2:
                return "9.18";
            case 3:
                return "9.72";
            case 4:
                return "10.30";
            case 5:
                return "10.91";
            case 6:
                return "11.56";
            case 7:
                return "12.25";
            case 8:
                return "12.98";
            case 9:
                return "13.75";
            case 10:
                return "14.57";
            case 11:
                return "15.43";
            case 12:
                return "16.35";
            case 13:
                return "17.32";
            case 14:
                return "18.35";
            case 15:
                return "19.45";
            case 16:
                return "20.60";
            case 17:
                return "21.83";
            case 18:
                return "23.12";
            case 19:
                return "24.50";
            case 20:
                return "25.96";
            case 21:
                return "27.50";
            case 22:
                return "29.14";
            case 23:
                return "30.87";
            case 24:
                return "32.70";
            case 25:
                return "34.65";
            case 26:
                return "36.71";
            case 27:
                return "38.89";
            case 28:
                return "41.20";
            case 29:
                return "43.65";
            case 30:
                return "46.25";
            case 31:
                return "49.00";
            case 32:
                return "51.91";
            case 33:
                return "55.00";
            case 34:
                return "58.27";
            case 35:
                return "61.74";
            case 36:
                return "65.41";
            case 37:
                return "69.30";
            case 38:
                return "73.42";
            case 39:
                return "77.78";
            case 40:
                return "82.41";
            case 41:
                return "87.31";
            case 42:
                return "92.50";
            case 43:
                return "98.00";
            case 44:
                return "103.83";
            case 45:
                return "110.00";
            case 46:
                return "116.54";
            case 47:
                return "123.47";
            case 48:
                return "130.81";
            case 49:
                return "138.59";
            case 50:
                return "146.83";
            case 51:
                return "155.56";
            case 52:
                return "164.81";
            case 53:
                return "174.61";
            case 54:
                return "185.00";
            case 55:
                return "196.00";
            case 56:
                return "207.65";
            case 57:
                return "220.00";
            case 58:
                return "233.08";
            case 59:
                return "246.94";
            case 60:
                return "261.63";
            case 61:
                return "277.18";
            case 62:
                return "293.66";
            case 63:
                return "311.13";
            case 64:
                return "329.63";
            case 65:
                return "349.23";
            case 66:
                return "369.99";
            case 67:
                return "392.00";
            case 68:
                return "415.30";
            case 69:
                return "440.00";
            case 70:
                return "466.16";
            case 71:
                return "493.88";
            case 72:
                return "523.25";
            case 73:
                return "554.37";
            case 74:
                return "587.33";
            case 75:
                return "622.25";
            case 76:
                return "659.26";
            case 77:
                return "698.46";
            case 78:
                return "739.99";
            case 79:
                return "783.99";
            case 80:
                return "830.61";
            case 81:
                return "880.00";
            case 82:
                return "932.33";
            case 83:
                return "987.77";
            case 84:
                return "1046.50";
            case 85:
                return "1108.73";
            case 86:
                return "1174.66";
            case 87:
                return "1244.51";
            case 88:
                return "1318.51";
            case 89:
                return "1396.91";
            case 90:
                return "1479.98";
            case 91:
                return "1567.98";
            case 92:
                return "1661.22";
            case 93:
                return "1760.00";
            case 94:
                return "1864.66";
            case 95:
                return "1975.53";
            case 96:
                return "2093.00";
            case 97:
                return "2217.46";
            case 98:
                return "2349.32";
            case 99:
                return "2489.02";
            case 100:
                return "2637.02";
            case 101:
                return "2793.83";
            case 102:
                return "2959.96";
            case 103:
                return "3135.96";
            case 104:
                return "3322.44";
            case 105:
                return "3520.00";
            case 106:
                return "3729.31";
            case 107:
                return "3951.07";
            case 108:
                return "4186.01";
            case 109:
                return "4434.92";
            case 110:
                return "4698.64";
            case 111:
                return "4978.03";
            case 112:
                return "5274.04";
            case 113:
                return "5587.65";
            case 114:
                return "5919.91";
            case 115:
                return "6271.93";
            case 116:
                return "6644.88";
            case 117:
                return "7040.00";
            case 118:
                return "7458.62";
            case 119:
                return "7902.13";
            case 120:
                return "8372.02";
            case 121:
                return "8869.84";
            case 122:
                return "9397.27";
            case 123:
                return "9956.06";
            case 124:
                return "10548.08";
            case 125:
                return "11175.30";
            case 126:
                return "11839.82";
            case 127:
                return "12543.85";
        }
        return "0";
    }
    private void filter_file()
    {
        int counter = length;
        length = 0;
        for(int i = 0; i<counter; i++)
        {
            String line = (String) music.get(i);
            List<String> String_items = Arrays.asList(new String[5]);
            String_items = Arrays.asList(line.split(" "));
            if(String_items.contains("On") || String_items.contains("Off"))
            {
                music_time.add(Integer.parseInt(String_items.get(0)));
                if(String_items.get(1).equals("On")) music_command.add(true);
                else music_command.add(false);
                //music_command.add(String_items.get(1));
                music_frequency.add(Float.parseFloat(Value_To_Frequency(String_items.get(3).substring(String_items.get(3).lastIndexOf("=") + 1))));
                music_velocity.add((Float.parseFloat(String_items.get(4).substring(String_items.get(4).lastIndexOf("=") + 1)))/((float)(255.0)));
                length++;
            }
        }
    }
    private void Change_To_MainActivity(){
        Message message=Message.obtain();
        message.what=controller.IDLE_MODE_START;
        controller.Write_Data_Callback.sendMessage(message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
