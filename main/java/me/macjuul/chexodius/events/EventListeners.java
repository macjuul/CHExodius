package me.macjuul.chexodius.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.laytonsmith.abstraction.events.MCProjectileHitEvent;
import com.laytonsmith.annotations.event;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;

import me.macjuul.chexodius.LifeCycle;

@SuppressWarnings("deprecation")
public class EventListeners implements Listener {
	private static EventListeners listener;

	public static void register() {
		if(listener == null){
			listener = new EventListeners();
		}
		LifeCycle.chp.registerEvents(listener);
	}
	
	public static void unregister() {
	}
	
	@event
	public void onHit(MCProjectileHitEvent event) {
		EventUtils.TriggerListener(Driver.EXTENSION, "projectile_hit_block", event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityCombust(EntityCombustEvent event) {
		BukkitMCEntityCombustEvent seme = new BukkitMCEntityCombustEvent(event);
		EventUtils.TriggerListener(Driver.EXTENSION, "entity_combust", seme);
	}
}
