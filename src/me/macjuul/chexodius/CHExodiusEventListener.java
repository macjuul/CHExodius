package me.macjuul.chexodius;

import org.bukkit.event.Listener;

import com.laytonsmith.commandhelper.CommandHelperPlugin;

public class CHExodiusEventListener
implements Listener {
    public CHExodiusEventListener(CommandHelperPlugin chp) {
        CHExodiusEvents events = new CHExodiusEvents();
        chp.registerEvents(events);
    }
}