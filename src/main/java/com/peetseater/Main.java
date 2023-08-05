package com.peetseater;

import com.peetseater.filesorter.DesktopApplication;

public class Main {
    private static final class RunnableImplementation implements Runnable {
        public void run() {
            new DesktopApplication();
        }
    }

    public static void main(String[] args) {
        AppLogger.info("Booting");
        javax.swing.SwingUtilities.invokeLater((new RunnableImplementation()));
    }
}