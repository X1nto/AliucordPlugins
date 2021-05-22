package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;

public class NitroSpoof extends Plugin {

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch();
    }

    @Override
    public void stop(Context context) {

    }
}
