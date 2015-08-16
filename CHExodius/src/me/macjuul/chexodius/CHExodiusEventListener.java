package me.macjuul.chexodius;

import org.bukkit.event.Listener;

import com.laytonsmith.commandhelper.CommandHelperPlugin;

import me.macjuul.chexodius.events.Events;

public class CHExodiusEventListener
implements Listener {
    public CHExodiusEventListener(CommandHelperPlugin chp) {
        Events events = new Events();
        chp.registerEvents(events);
    }
}