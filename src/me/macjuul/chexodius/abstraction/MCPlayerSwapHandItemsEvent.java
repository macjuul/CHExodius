package me.macjuul.chexodius.abstraction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.laytonsmith.core.events.BindableEvent;

public interface MCPlayerSwapHandItemsEvent extends BindableEvent {
	
	public ItemStack getMainHandItem();
	public ItemStack getOffHandItem();
	public void setMainHandItem(ItemStack mainHandItem);
	public void setOffHandItem(ItemStack offHandItem);
	public Player getPlayer();
	
}
