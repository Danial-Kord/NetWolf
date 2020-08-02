package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class FileFinder {


    public static ArrayList<String> getNames(File dad,ArrayList<String>files){
        File[] fileList = dad.listFiles();
        if(fileList == null)
            return files;
        for (File temp : fileList){
            if(temp.isDirectory()){
                files = getNames(temp,files);
            }
            else {
                    files.add(temp.getName());
            }
        }
        return files;
    }


}

