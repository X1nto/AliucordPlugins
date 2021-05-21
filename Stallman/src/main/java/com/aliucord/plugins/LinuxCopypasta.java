package com.aliucord.plugins;

import android.content.Context;
import com.aliucord.entities.Plugin;
import com.aliucord.api.CommandsAPI;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LinuxCopypasta extends Plugin {

    @NotNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto") };
        manifest.description = "A plugin that replaces the word \"Linux\" in the Linux Copypasta with your desired string.";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordStallman/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        commands.registerCommand(
            "stallman",
            "Replace \"Linux\" with your desired string",
            Collections.singletonList(CommandsAPI.messageOption),
            args -> {
                Object desiredString = args.get("message");
                String copypasta =
                        "I'd just like to interject for a moment. What you're referring to as Linux, is in fact, GNU/Linux, or as I've recently taken to calling it, GNU plus Linux. Linux is not an operating system unto itself, but rather another free component of a fully functioning GNU system made useful by the GNU corelibs, shell utilities and vital system components comprising a full OS as defined by POSIX.\n\n" +
                        "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called \"Linux\", and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n\n" +
                        "There really is a Linux, and these people are using it, but it is just a part of the system they use. Linux is the kernel: the program in the system that allocates the machine's resources to the other programs that you run. The kernel is an essential part of an operating system, but useless by itself; it can only function in the context of a complete operating system. Linux is normally used in combination with the GNU operating system: the whole system is basically GNU with Linux added, or GNU/Linux. All the so-called \"Linux\" distributions are really distributions of GNU/Linux.";

                return new CommandsAPI.CommandResult(copypasta.replace("Linux", desiredString.toString()));
            }
        );
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }

}