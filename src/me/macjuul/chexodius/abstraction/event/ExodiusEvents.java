package me.macjuul.chexodius.abstraction.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.annotations.abstraction;

import me.macjuul.chexodius.abstraction.MCEntityToggleGlideEvent;
import me.macjuul.chexodius.abstraction.MCPlayerSwapHandItemsEvent;

public class ExodiusEvents {
	
	@abstraction(type = Implementation.Type.BUKKIT)
	public static class ExodiusEntityGlideEvent implements MCEntityToggleGlideEvent {
		EntityToggleGlideEvent e;
		
		public ExodiusEntityGlideEvent(Event e) {
			this.e = (EntityToggleGlideEvent) e;
		}

		@Override
		public Object _GetObject() {
			return e;
		}

		@Override
		public boolean isGliding() {
			return e.isGliding();
		}

		@Override
		public Entity getEntity() {
			return e.getEntity();
		}

		@Override
		public EntityType getEntityType() {
			return e.getEntityType();
		}
		
	}
	
	@abstraction(type = Implementation.Type.BUKKIT)
	public static class ExodiusPlayerSwapHandItemsEvent implements MCPlayerSwapHandItemsEvent {
		PlayerSwapHandItemsEvent e;
		
		public ExodiusPlayerSwapHandItemsEvent(Event e) {
			this.e = (PlayerSwapHandItemsEvent) e;
		}

		@Override
		public Object _GetObject() {
			return e;
		}

		@Override
		public ItemStack getMainHandItem() {
			return e.getMainHandItem();
		}

		@Override
		public ItemStack getOffHandItem() {
			return e.getOffHandItem();
		}

		@Override
		public void setMainHandItem(ItemStack mainHandItem) {
			e.setMainHandItem(mainHandItem);
		}

		@Override
		public void setOffHandItem(ItemStack offHandItem) {
			e.setOffHandItem(offHandItem);
		}
		
		public Player getPlayer() {
			return e.getPlayer();
		}
		
	}
}
