package me.macjuul.chexodius;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

import me.macjuul.chexodius.events.EventListeners;

@MSExtension("CHExodius")
public class LifeCycle
extends AbstractExtension {
    public static CommandHelperPlugin chp;

    public Version getVersion() {
        return new SimpleVersion(2, 3, 0);
    }

    public void onShutdown() {
        System.out.println("CHExodius " + getVersion() + " has sucessfully been disabled!");
        EventListeners.unregister();
    }

    public void onStartup() {
        System.out.println("CHExodius " + getVersion() + " has sucessfully been enabled!");
        chp = CommandHelperPlugin.self;
        EventListeners.register();
        
    }
}