package com.company;

import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnpackedZip {
    private static final String INPUT_ZIP_FILE = "C:\\Program Files\\Java\\jdk1.8.0_92\\src.zip";
    private static final String OUTPUT_FOLDER = "C:\\outputzip";
    private static final String JAVA = ".java";
    private static int counterJavaFiles;
    private static int counterAnotation;

    public static void main(String[] args)  {
        String userDir = System.getProperty("user.dir");
        File homeDir = new File(userDir);
        showAllFiles(homeDir);
        System.out.println();
        UnpackedZip unZip = new UnpackedZip();
        unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    }

    private static void showAllFiles(File files) {
        File[] allFiles = files.listFiles();
        for (File file : allFiles) {
            if (file.isDirectory() && files.isDirectory()) {
                showAllFiles(file);
            } else {
                System.out.println(file.getName());
            }
        }
    }

    public void unZipIt(String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];
        try {
            File folder = new File(OUTPUT_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                if (newFile.getName().contains(JAVA)) {
                    counterJavaFiles++;
                }
                try {
                    Scanner scanner = new Scanner(newFile);
                    while (scanner.hasNext()) {
                        if (scanner.nextLine().contains("@FunctionalInterface")) {
                            counterAnotation++;
                            System.out.println(newFile);
                            break;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("UNPACKED COMPLETE! Number of .java files: " + counterJavaFiles);
            System.out.println("Number .java files that contains @FunctionalInterface: " + counterAnotation);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
