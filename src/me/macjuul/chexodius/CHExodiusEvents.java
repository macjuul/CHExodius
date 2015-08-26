package me.macjuul.chexodius;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCProjectile;
import com.laytonsmith.abstraction.events.MCProjectileHitEvent;
import com.laytonsmith.annotations.event;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;

@SuppressWarnings("deprecation")
public class CHExodiusEvents
implements Listener {
    public static class projectile_hit_block extends AbstractEvent {

        public BindableEvent convert(CArray manualObject, Target t) {
            return null;
        }

        public String docs() {
            return "";
        }

        public Driver driver() {
            return Driver.EXTENSION;
        }

        public Map < String, Construct > evaluate(BindableEvent e)
                throws EventException {
            Map < String, Construct > retn = new HashMap < String, Construct > ();
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
                    if (hitBlock.getTypeId() != 0) {
                        break;
                    }
                }
                Location loc = hitBlock.getLocation();
                CArray arr = new CArray(Target.UNKNOWN);

                arr.set(0, new CInt(loc.getBlockX(), Target.UNKNOWN), Target.UNKNOWN);
                arr.set(1, new CInt(loc.getBlockY(), Target.UNKNOWN), Target.UNKNOWN);
                arr.set(2, new CInt(loc.getBlockZ(), Target.UNKNOWN), Target.UNKNOWN);
                arr.set(3, new CString(loc.getWorld().getName(), Target.UNKNOWN), Target.UNKNOWN);

                retn.put("id", new CInt(ent.getEntityId(), Target.UNKNOWN));

                retn.put("type", new CString(ent.getType().concreteName(), Target.UNKNOWN));

                retn.put("location", arr);
            }
            return retn;
        }

        public String getName() {
            return "projectile_hit_block";
        }

        public boolean matches(Map < String, Construct > prefilter, BindableEvent e)
                throws PrefilterNonMatchException {
            return true;
        }

        public boolean modifyEvent(String key, Construct value, BindableEvent event)
                throws ConfigRuntimeException {
            return false;
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @event
    public void onHit(MCProjectileHitEvent event) {
        EventUtils.TriggerListener(Driver.EXTENSION, "projectile_hit_block", event);
    }
}