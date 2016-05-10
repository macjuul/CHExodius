package me.macjuul.chexodius.events;

import com.laytonsmith.abstraction.MCEntity;
import com.laytonsmith.core.events.BindableEvent;

public interface MCEntityCombustEvent extends BindableEvent {
	
	public int getDuration();
	public void setDuration(int d);
	public MCEntity getEntity();
	
}
