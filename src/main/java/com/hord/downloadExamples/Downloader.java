package com.hord.downloadExamples;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

public class Downloader {

    public final void download(String url) {
        download(url, "NewFile" + Calendar.getInstance().getTimeInMillis());
    }

    public final void download(String url, String name) {
        try (FileOutputStream outputStream = new FileOutputStream(name)) {
            try (InputStream inputStream = new URL(url).openStream()) {
                int data;
                while ((data = inputStream.read()) != -1) {
                    System.out.println("[downloading]...");
                    outputStream.write(data);
                }
                System.out.println("File was successfully downloaded!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
