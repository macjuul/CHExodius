package me.macjuul.chexodius;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCProjectile;
import com.laytonsmith.abstraction.bukkit.BukkitMCItemStack;
import com.laytonsmith.abstraction.events.MCProjectileHitEvent;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.Prefilters;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;

import me.macjuul.chexodius.abstraction.MCEntityToggleGlideEvent;
import me.macjuul.chexodius.abstraction.MCPlayerSwapHandItemsEvent;

public class CHExodiusEvents {
	
	public static String docs() {
		return "Adds misc handy events";
	}

	@api
	public static class projectile_hit_block extends AbstractEvent {
		public String getName() {
			return "projectile_hit_block";
		}

		public String docs() {
			return "";
		}

		public boolean matches(Map<String, Construct> prefilter, BindableEvent e) throws PrefilterNonMatchException {
			return true;
		}

		public BindableEvent convert(CArray manualObject, Target t) {
			return null;
		}

		public Map<String, Construct> evaluate(BindableEvent e) throws EventException {
			Map<String, Construct> retn = new HashMap<String, Construct>();
			if ((e instanceof MCProjectileHitEvent)) {
				MCProjectile ent = ((MCProjectileHitEvent) e).getEntity();

				double x1 = ent.getLocation().toVector().X();
				double y1 = ent.getLocation().toVector().Y();
				double z1 = ent.getLocation().toVector().Z();

				Vector v1 = new Vector(x1, y1, z1);

				double x2 = ent.getVelocity().normalize().X();
				double y2 = ent.getVelocity().normalize().Y();
				double z2 = ent.getVelocity().normalize().Z();

				Vector v2 = new Vector(x2, y2, z2);

				World w = Bukkit.getServer().getWorld(ent.getLocation().getWorld().getName());

				BlockIterator iterator = new BlockIterator(w, v1, v2, 0.0D, 4);
				Block hitBlock = null;
				while (iterator.hasNext()) {
					hitBlock = iterator.next();
					if (hitBlock.getType() != Material.AIR) {
						break;
					}
				}
				Location loc = hitBlock.getLocation();
				CArray arr = new CArray(Target.UNKNOWN);

				arr.set(0, new CInt(loc.getBlockX(), Target.UNKNOWN), Target.UNKNOWN);
				arr.set(1, new CInt(loc.getBlockY(), Target.UNKNOWN), Target.UNKNOWN);
				arr.set(2, new CInt(loc.getBlockZ(), Target.UNKNOWN), Target.UNKNOWN);
				arr.set(3, new CString(loc.getWorld().getName(), Target.UNKNOWN), Target.UNKNOWN);

				retn.put("id", new CString(ent.getUniqueId().toString(), Target.UNKNOWN));

				retn.put("location", arr);
			}
			return retn;
		}

		public boolean modifyEvent(String key, Construct value, BindableEvent event) throws ConfigRuntimeException {
			return false;
		}

		public Version since() {
			return CHVersion.V3_3_1;
		}

		public Driver driver() {
			return Driver.EXTENSION;
		}
	}
	
	@api
	public static class entity_toggle_glide extends AbstractEvent {
		public String getName() {
			return "entity_toggle_glide";
		}

		public String docs() {
			return "";
		}

		public boolean matches(Map<String, Construct> filter, BindableEvent e) throws PrefilterNonMatchException {
			if(e instanceof MCEntityToggleGlideEvent) {
				MCEntityToggleGlideEvent evt = (MCEntityToggleGlideEvent) e;
				
				Prefilters.match(filter, "type", evt.getEntityType().toString(), Prefilters.PrefilterType.MACRO);
				Prefilters.match(filter, "id", evt.getEntity().getUniqueId().toString(), Prefilters.PrefilterType.MACRO);
				
				if(evt.getEntityType().equals(EntityType.PLAYER)) {
					Prefilters.match(filter, "player", evt.getEntity().getName(), Prefilters.PrefilterType.MACRO);
				}
				return true;
			}
			return false;
		}

		public BindableEvent convert(CArray manualObject, Target t) {
			return null;
		}

		public Map<String, Construct> evaluate(BindableEvent e) throws EventException {
			if(e instanceof MCEntityToggleGlideEvent) {
				MCEntityToggleGlideEvent evt = (MCEntityToggleGlideEvent) e;
				Map<String, Construct> ret = evaluate_helper(evt);
				Target t = Target.UNKNOWN;
				
				ret.put("gliding", CBoolean.GenerateCBoolean(evt.isGliding(), t));
				ret.put("id", new CString(evt.getEntity().getUniqueId().toString(), t));	
				ret.put("type", new CString(evt.getEntityType().toString(), t));
				
				if(evt.getEntityType().equals(EntityType.PLAYER)) {
					ret.put("player", new CString(evt.getEntity().getName(), t));
				}
				
				return ret;
			} else {
				throw new EventException("Could not convert to MCEntityToggleGlideEvent");
			}
		}

		public boolean modifyEvent(String key, Construct value, BindableEvent event) throws ConfigRuntimeException {
			return false;
		}

		public Version since() {
			return CHVersion.V3_3_2;
		}

		public Driver driver() {
			return Driver.EXTENSION;
		}
	}
	
	@api
	public static class player_swap_hand extends AbstractEvent {
		public String getName() {
			return "player_swap_hand";
		}

		public String docs() {
			return "";
		}

		public boolean matches(Map<String, Construct> filter, BindableEvent e) throws PrefilterNonMatchException {
			if(e instanceof MCPlayerSwapHandItemsEvent) {
				MCPlayerSwapHandItemsEvent evt = (MCPlayerSwapHandItemsEvent) e;
				
				Prefilters.match(filter, "player", evt.getPlayer().getName(), Prefilters.PrefilterType.STRING_MATCH);
				
				return true;
			}
			return false;
		}

		public BindableEvent convert(CArray manualObject, Target t) {
			return null;
		}

		public Map<String, Construct> evaluate(BindableEvent e) throws EventException {
			if(e instanceof MCPlayerSwapHandItemsEvent) {
				MCPlayerSwapHandItemsEvent evt = (MCPlayerSwapHandItemsEvent) e;
				Map<String, Construct> ret = evaluate_helper(evt);
				Target t = Target.UNKNOWN;
				
				ret.put("player", new CString(evt.getPlayer().getName(), t));
				
				Construct main = ObjectGenerator.GetGenerator().item(new BukkitMCItemStack(evt.getMainHandItem()), t);
				if(Static.isNull(main)) {
					ret.put("main_hand_item", CNull.NULL);
				} else {
					ret.put("main_hand_item", Static.getArray(main, t));
				}
				
				Construct off = ObjectGenerator.GetGenerator().item(new BukkitMCItemStack(evt.getOffHandItem()), t);
				if(Static.isNull(off)) {
					ret.put("off_hand_item", CNull.NULL);
				} else {
					ret.put("off_hand_item", Static.getArray(off, t));
				}
				
				return ret;
			} else {
				throw new EventException("Could not convert to MCPlayerSwapHandItemsEvent");
			}
		}

		public boolean modifyEvent(String key, Construct value, BindableEvent e) throws ConfigRuntimeException {
			MCPlayerSwapHandItemsEvent event = (MCPlayerSwapHandItemsEvent) e;

			if(key.equalsIgnoreCase("main_hand_item") && value instanceof CArray) {
				CArray item = Static.getArray(value, Target.UNKNOWN);
				event.setMainHandItem((ItemStack) (ObjectGenerator.GetGenerator().item(item, Target.UNKNOWN).getHandle()));
				return true;
			}
			
			if(key.equalsIgnoreCase("off_hand_item") && value instanceof CArray) {
				CArray item = Static.getArray(value, Target.UNKNOWN);
				event.setOffHandItem((ItemStack) (ObjectGenerator.GetGenerator().item(item, Target.UNKNOWN).getHandle()));
				return true;
			}

			return false;
		}

		public Version since() {
			return CHVersion.V3_3_2;
		}

		public Driver driver() {
			return Driver.EXTENSION;
		}
	}
}
