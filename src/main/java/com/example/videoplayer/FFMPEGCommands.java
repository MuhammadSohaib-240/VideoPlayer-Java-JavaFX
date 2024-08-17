package com.example.videoplayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SuppressWarnings("All")
public class FFMPEGCommands {

    int seconds;

    public void screenshotCommand(String command) {
        new Thread(() -> {
            try {
                // Execute CMD command
                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Loop to show CMD execution commands line
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (Exception e) {
                // PASS
            }
        }).start();
    }

    public void extractingImages(String command) {
        new Thread(() -> {
            try {
                // Execute CMD command
                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Loop to show CMD execution commands line
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (Exception e) {
                // PASS
            }
        }).start();
    }

    public void copyVideo(String command) {
        new Thread(() -> {
            try {
                // Execute CMD command
                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Loop to show CMD execution commands line
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (Exception e) {
                // PASS
            }
        }).start();
    }

    public void convertVideoToMonochrome(String command) {
        new Thread(() -> {
            try {
                // Execute CMD command
                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Loop to show CMD execution commands line
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (Exception e) {
                // PASS
            }
        }).start();
    }
}
