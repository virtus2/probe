package com.radiantraccon.probe.data;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.radiantraccon.probe.R;
import com.radiantraccon.probe.site.Quasarzone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AddressDataListWrapper {
    // data to populate the RecyclerView with
    private ArrayList<AddressData> addressDataList;
    private AddressAdapter addressAdapter;

    // constructor
    public AddressDataListWrapper() {
        addressDataList = new ArrayList<>();
    }
    // getter and setter for list
    public ArrayList<AddressData> getAddressDataList() {
        return addressDataList;
    }
    public void setAddressDataList(ArrayList<AddressData> list) {
        addressDataList = list;
    }
    // getter and init for adapter
    public AddressAdapter getAddressAdapter() {
        return addressAdapter;
    }
    public void initAdapter() {
        addressAdapter = new AddressAdapter(addressDataList);
    }

    public void writeAddressDataFile(String filename, Context context) {
        File file = new File(context.getFilesDir(), filename);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try{
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            JsonWriter jsonWriter = new JsonWriter(bufferedWriter);
            jsonWriter.beginArray();
            for(AddressData data : addressDataList) {
                jsonWriter.beginObject();
                jsonWriter.name("imageid").value(data.getImageId());
                jsonWriter.name("title").value(data.getTitle());
                jsonWriter.name("address").value(data.getAddress());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch(IOException e) {
            Log.e("File write", "Can't write FIle "+e.toString());
        }
    }

    public void readAddressDataFile(String filename, Context context) {
        File file = new File(context.getFilesDir(), filename);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            if(!file.exists()) {
                writeFirstDataFile(filename, context);
                return;
            }
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            JsonReader jsonReader = new JsonReader(bufferedReader);
            jsonReader.beginArray();
            while(jsonReader.hasNext()) {
                int imageId = -1;
                String title = "";
                String address = "";
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    if (name.equals("imageid")) {
                        imageId = jsonReader.nextInt();
                    } else if (name.equals("title")) {
                        title = jsonReader.nextString();
                    } else if (name.equals("address")) {
                        address = jsonReader.nextString();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                AddressData data = new AddressData(imageId, title, address);
                jsonReader.endObject();
                addressDataList.add(data);
                Log.e("File read", data.toString() + "readed ");
            }
            jsonReader.endArray();
            jsonReader.close();
            bufferedReader.close();
            fileReader.close();

        } catch(FileNotFoundException e) {
            Log.e("File read", "File not found: "+e.toString());
        } catch(IOException e) {
            Log.e("File read", "Can't read file: "+e.toString());
        }
        Log.e("result",addressDataList.toString());
    }

    private void writeFirstDataFile(String filename, Context context) {
        addressDataList = new ArrayList<>();
        AddressData QUASARZONE_GAME = new AddressData(R.drawable.quasarzone, context.getString(R.string.quasarzone_game), Quasarzone.NEWS_GAME);
        AddressData QUASARZONE_HARDWARE = new AddressData(R.drawable.quasarzone, context.getString(R.string.quasarzone_hardware), Quasarzone.NEWS_HARDWARE);
        AddressData QUASARZONE_MOBILE = new AddressData(R.drawable.quasarzone, context.getString(R.string.quasarzone_mobile), Quasarzone.NEWS_MOBILE);

        addressDataList.add(QUASARZONE_GAME);
        addressDataList.add(QUASARZONE_HARDWARE);
        addressDataList.add(QUASARZONE_MOBILE);

        writeAddressDataFile(filename,context);
    }
}
