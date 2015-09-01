package me.macjuul.chexodius;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

@MSExtension("CHExodius")
public class CHExodiusMain
extends AbstractExtension {
    public static CommandHelperPlugin chp;
    public static CHExodiusEventListener listener;

    public Version getVersion() {
        return new SimpleVersion(2, 2, 0);
    }

    public void onShutdown() {
        System.out.println("CHExodius " + getVersion() + " has sucessfully been disabled!");
    }

    public void onStartup() {
        System.out.println("CHExodius " + getVersion() + " has sucessfully been enabled!");
        chp = CommandHelperPlugin.self;
        listener = new CHExodiusEventListener(chp);
    }
}