package com.hord.downloadExamples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class Downloader {

    private static final Logger logger = LogManager.getLogger(Downloader.class);

    public static void download(String url, boolean override) throws IOException {
        download(url, "NewFile" + System.currentTimeMillis(), override);
    }

    public static void download(String url, String outputFileName, boolean override) throws IOException {
        download(new URL(url), outputFileName, override);
    }

    public static void download(URL url, String outputFileName, boolean override) throws IOException {
        try {
            File file = new File(outputFileName);
            file.getParentFile().mkdirs();
            if (file.createNewFile()) {
                download(url, file);
            } else if (override) {
                Files.delete(file.toPath());
                if (file.createNewFile()) {
                    download(url, file);
                } else {
                    logger.warn("Can't delete existing file and then created new one by path: {}", outputFileName);
                }
            } else {
                logger.warn("Downloading skipped! Destination file is already exist by path: {}", outputFileName);
            }
        } catch (Exception e) {
            logger.info("Can't download file by URL: {} to Destination: {}", url, outputFileName);
        }
    }

    private static void download(URL url, File file) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            Files.copy(inputStream, file.toPath());
            logger.info("File was successfully downloaded!");
        }
    }
}
