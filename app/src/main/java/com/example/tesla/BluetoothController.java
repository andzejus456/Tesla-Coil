package com.example.tesla;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class BluetoothController { //Singleton <---
    private static BluetoothController instnace = null;

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice[] btArray;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket btSocket = null;
    public boolean CONNECTION_STATUS = false;

    public int Actual_Level = 0;
    public long Min_Frequency = 100000;
    public long Max_Frequency = 1000000;
    public long Start_Frequency = 100000;
    public long Stop_Frequency = 1000000;
    public long Frequency_Of_Output_Signal = 500000;
    public int Duty_Cycle = 100;
    public int Sweep_Frequency = 100;
    public long Min_Interrupt_Frequency = 1;
    public long Max_Interrupt_Frequency = 30000;
    public long Start_Interrupt_Frequency = 1;
    public long Stop_Interrupt_Frequency = 500;
    public long Frequency_Of_Interrupt_Signal = 1;
    public int Interrupt_Duty_Cycle = 10;
    public int Sweep_Frequency_Step = 10;

    byte State_Word = 'a';
    byte Mode_Word = 'b';
    byte IDLE_Word = 'c';
    byte LEVELING_TO_ZERO_POSITION_Word = 'd';
    byte LEVELING_Word = 'e';
    byte SWEEP_FREQUENCY_Word = 'f';
    byte CONTINUOUS_MODE_START_Word = 'g';
    byte INTERRUPT_MODE_START_Word = 'h';
    byte PWM_MODE_START_Word = 'i';
    byte Frequency_Of_Interrupt_Signal_Word = 'j';
    byte Duty_Of_Interrupt_Signal_Word = 'k';
    byte Needed_Level_Word = 'l';
    byte Duty_Of_Output_Signal_Word = 'm';
    byte Frequency_Of_Output_Signal_Word = 'n';
    byte Start_Frequency_Word = 'o';
    byte Stop_Frequency_Word = 'p';
    byte IDLE_MODE_START_Word = 'r';
    byte Sweep_Frequency_Value_Word = 's';
    byte Sweep_Frequency_Step_Word = 't';
    byte Sweep_Stop_Word = 'u';
    byte Equal_Symbol = '=';
    byte End_Of_Sentence_Symbol = '\n';

    public static final int LEVELING_TO_ZERO_POSITION = 1;
    public static final int LEVELING = 2;
    public static final int SWEEP_FREQUENCY = 3;
    public static final int CONTINUOUS_MODE_START = 4;
    public static final int INTERRUPT_MODE_START = 5;
    public static final int PWM_MODE_START = 6;
    public static final int FREQUENCY_OF_INTERRUPT_SIGNAL = 7;
    public static final int DUTY_OF_INTERRUPT_SIGNAL = 8;
    public static final int Needed_Level = 9;
    public static final int DUTY_OF_OUTPUT_SIGNAL = 10;
    public static final int FREQUENCY_OF_OUTPUT_SIGNAL = 11;
    public static final int START_FREQUENCY = 12;
    public static final int STOP_FREQUENCY = 13;
    public static final int IDLE_MODE_START = 14;
    public static final int SWEEP_FREQUENCY_VALUE = 15; //variable
    public static final int IDLE = 16;
    public static final int SWEEP_FREQUENCY_STEP = 't';
    public static final int SWEEP_STOP = 'u';

    InputStream inputStream = null;
    OutputStream outputStream = null;

    private final String DEVICE_NAME = "Tesla Coil";

    protected BluetoothController() { //constructor
    }
    public  BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }
    public BluetoothSocket getBtSocket() {
        return btSocket;
    }
    public int connect() {
        if (btAdapter == null) {
            return -1;
        } else {
            if (!btAdapter.isEnabled()) {
            }

            while (!btAdapter.isEnabled()) ;

            Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
            String[] strings = new String[bt.size()];
            btArray = new BluetoothDevice[bt.size()];

            int index = 0;
            int index_Tesla = -1;

            if (btAdapter.isEnabled()) {

                if (bt.size() > 0) {
                    for (BluetoothDevice device : bt) {
                        btArray[index] = device;
                        strings[index] = device.getName();
                        if (strings[index].equals(DEVICE_NAME))
                            index_Tesla = index;
                        index++;
                    }
                    //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    //listViewPairedDevices.setAdapter(arrayAdapter);
                } else {
                    return -2;
                }
            }

            if (index_Tesla != -1) {
                if (!CONNECTION_STATUS) {
                    bluetoothDevice = btAdapter.getRemoteDevice(btArray[index_Tesla].getAddress());
                    int counter = 0;
                    do {
                        try {
                            btSocket = bluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
                            btSocket.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        counter++;
                    } while (!btSocket.isConnected() && counter < 3);

                    if (btSocket.isConnected()) {
                        CONNECTION_STATUS = true;
                        return 1;

                    } else {
                        CONNECTION_STATUS = false;
                        return -3;
                    }
                } else {
                    return -4;
                }
            } else {
                return -5;
            }
        }
    }
    public int disconnect() {

        if (CONNECTION_STATUS) {

            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!btSocket.isConnected()) {
                CONNECTION_STATUS = false;
                return 1;//Toast.makeText(getApplicationContext(), "You have successfully disconnected from The Tesla Coil", Toast.LENGTH_LONG).show();
            } else
                return 0;
        } else {
            return 0; //Toast.makeText(getApplicationContext(), "You have already disconnected from The Tesla Coil", Toast.LENGTH_LONG).show();
        }
    }
    private void Send_Bluetooth_Command(byte id_of_command, byte value) {
        try {
            byte[] data_to_send = {id_of_command, Equal_Symbol, value, End_Of_Sentence_Symbol};

            outputStream = btSocket.getOutputStream();
            outputStream.write(1);
            outputStream.write(data_to_send);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void Send_Bluetooth_Variable(byte id_of_command, float value, float value2) {
        try {
            if(value2 == 200.0)
            {
                byte[] value_in_bytes = String.valueOf(value).getBytes();
                byte[] data_to_send = {id_of_command, Equal_Symbol};
                byte[] result = new byte[data_to_send.length + value_in_bytes.length + 1];
                System.arraycopy(data_to_send, 0, result, 0, data_to_send.length);
                System.arraycopy(value_in_bytes, 0, result, data_to_send.length, value_in_bytes.length);
                result[result.length - 1] = End_Of_Sentence_Symbol;
                outputStream = btSocket.getOutputStream();
                outputStream.write(1);
                outputStream.write(result);
            }
            else
            {
                byte[] value_in_bytes = String.valueOf(value).getBytes();
                byte[] data_to_send = {id_of_command, Equal_Symbol};
                byte[] result = new byte[data_to_send.length + value_in_bytes.length + 1];
                System.arraycopy(data_to_send, 0, result, 0, data_to_send.length);
                System.arraycopy(value_in_bytes, 0, result, data_to_send.length, value_in_bytes.length);
                result[result.length - 1] = End_Of_Sentence_Symbol;
                value2 = (float) (value2/100.0);
                byte[] value2_in_bytes = String.valueOf(value2).getBytes();
                byte[] data2_to_send = {Duty_Of_Output_Signal_Word, Equal_Symbol};
                byte[] result2 = new byte[data2_to_send.length + value2_in_bytes.length + 1];
                System.arraycopy(data2_to_send, 0, result2, 0, data2_to_send.length);
                System.arraycopy(value2_in_bytes, 0, result2, data2_to_send.length, value2_in_bytes.length);
                result2[result2.length - 1] = End_Of_Sentence_Symbol;
                outputStream = btSocket.getOutputStream();
                outputStream.write(2);
                outputStream.write(result);
                outputStream.write(result2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Handler Write_Data_Callback=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) { switch(msg.what) {
            case LEVELING_TO_ZERO_POSITION:
                Send_Bluetooth_Command(State_Word,LEVELING_TO_ZERO_POSITION_Word);
                break;
            case LEVELING:
                Send_Bluetooth_Variable(Needed_Level_Word,msg.arg1, msg.arg2);
                break;
            case CONTINUOUS_MODE_START:
                Send_Bluetooth_Command(Mode_Word,CONTINUOUS_MODE_START_Word);
                break;
            case FREQUENCY_OF_OUTPUT_SIGNAL:
                Send_Bluetooth_Variable(Frequency_Of_Output_Signal_Word, msg.arg1, msg.arg2);
                break;
            case IDLE_MODE_START:
                Send_Bluetooth_Command(Mode_Word,IDLE_MODE_START_Word);
                break;
            case START_FREQUENCY:
                Send_Bluetooth_Variable(Start_Frequency_Word,msg.arg1, msg.arg2);
                break;
            case STOP_FREQUENCY:
                Send_Bluetooth_Variable(Stop_Frequency_Word,msg.arg1, msg.arg2);
                break;
            case DUTY_OF_OUTPUT_SIGNAL:
                Send_Bluetooth_Variable(Duty_Of_Output_Signal_Word,(float) ((float) (msg.arg1)/100.0), msg.arg2);
                break;
            case SWEEP_FREQUENCY:
                Send_Bluetooth_Command(State_Word, SWEEP_FREQUENCY_Word);
                break;
            case SWEEP_FREQUENCY_VALUE:
                Send_Bluetooth_Variable(Sweep_Frequency_Value_Word,msg.arg1,msg.arg2);
                break;
            case IDLE:
                Send_Bluetooth_Command(State_Word, IDLE_Word);
                break;
            case FREQUENCY_OF_INTERRUPT_SIGNAL:
                Send_Bluetooth_Variable(Frequency_Of_Interrupt_Signal_Word,msg.arg1, msg.arg2);
                break;
            case DUTY_OF_INTERRUPT_SIGNAL:
                Send_Bluetooth_Variable(Duty_Of_Interrupt_Signal_Word,(float) ((float) (msg.arg1)/100.0),msg.arg2);
                break;
            case INTERRUPT_MODE_START:
                Send_Bluetooth_Command(Mode_Word,INTERRUPT_MODE_START_Word);
                break;
            case SWEEP_FREQUENCY_STEP:
                Send_Bluetooth_Variable(Sweep_Frequency_Step_Word, msg.arg1, msg.arg2);
                break;
            case SWEEP_STOP:
                Send_Bluetooth_Variable(Sweep_Stop_Word,1, msg.arg2);
                break;
        }
            return false;
        }
    });
    public static synchronized BluetoothController getInstance() {
        if(instnace == null ) {
            instnace = new BluetoothController();
        }
        return  instnace;
    }

}
