package me.macjuul.chexodius.abstraction;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.laytonsmith.core.events.BindableEvent;

public interface MCEntityToggleGlideEvent extends BindableEvent {
	
	public boolean isGliding();
	public Entity getEntity();
	public EntityType getEntityType();
	
}
