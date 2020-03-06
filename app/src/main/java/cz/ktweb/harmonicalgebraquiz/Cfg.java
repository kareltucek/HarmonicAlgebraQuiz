package cz.ktweb.harmonicalgebraquiz;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

public class Cfg {

    public static ConfigType c = new ConfigType();

    public static void SaveObjectToFile(Object obj, String filename) {
        try {
            FileOutputStream ow = MenuActivity.context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(ow);

            // Write objects to file
            o.writeObject(obj);

            o.close();
            ow.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

    }

    public static void Load() {
        try {
            Log.d("Cfg", "loading");
            String selection = (String) LoadObjectFromFile("selection");
            LoadFrom(selection);
        } catch(Exception e) {
            Log.d("Cfg", "load failed");
        }
    }

    public static void Save() {
        Log.d("Cfg", "saving");
        SaveObjectToFile(c.name, "selection");
        SaveObjectToFile(c, c.name);
    }

    public static void SaveAs(String n) {
        c.name = n;
        Save();
    }

    public static void LoadFrom(String filename) {
        try {
            ConfigType loadedCfg = (ConfigType) LoadObjectFromFile(filename);
            c = loadedCfg;
            c.fromSave = true;
        } catch(Exception e) {
            System.out.println("Error while loading saved state");
        }
    }

    public static Object LoadObjectFromFile(String filename) throws IOException, ClassNotFoundException  {
        FileInputStream fi = MenuActivity.context.openFileInput(filename);
        ObjectInputStream oi = new ObjectInputStream(fi);

        Object newCfg = oi.readObject();

        oi.close();
        fi.close();

        return newCfg;
    }
}