package me.macjuul.chexodius.abstraction.event;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.laytonsmith.abstraction.events.MCProjectileHitEvent;
import com.laytonsmith.annotations.event;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;

import me.macjuul.chexodius.CHExodiusMain;

@SuppressWarnings("deprecation")
public class EventListeners implements Listener {
	private static EventListeners listener;

	public static void register() {
		if(listener == null){
			listener = new EventListeners();
		}
		CHExodiusMain.chp.registerEvents(listener);
	}
	
	public static void unregister() {
		EntityToggleGlideEvent.getHandlerList().unregister(listener);
	}
	
	@event
	public void onHit(MCProjectileHitEvent event) {
		EventUtils.TriggerListener(Driver.EXTENSION, "projectile_hit_block", event);
	}
	
	@EventHandler
	public void onGlide(EntityToggleGlideEvent event) {
		if(event.getEntityType().equals(EntityType.PLAYER) && ((Player) event.getEntity()).isFlying()) {
			return;
		}
		ExodiusEvents.ExodiusEntityGlideEvent evt = new ExodiusEvents.ExodiusEntityGlideEvent(event);
		EventUtils.TriggerListener(Driver.EXTENSION, "entity_toggle_glide", evt);
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent event) {
		ExodiusEvents.ExodiusPlayerSwapHandItemsEvent evt = new ExodiusEvents.ExodiusPlayerSwapHandItemsEvent(event);
		EventUtils.TriggerListener(Driver.EXTENSION, "player_swap_hand", evt);
	}
}
