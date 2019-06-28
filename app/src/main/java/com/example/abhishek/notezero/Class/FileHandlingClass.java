package com.example.abhishek.notezero.Class;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.cert.TrustAnchor;

public class FileHandlingClass {

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Context context;

    public FileHandlingClass(Context context) {
        this.context = context;
    }

}
