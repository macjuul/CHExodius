package me.macjuul.chexodius.utility;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Target;

public class ExoUtil {

    /**
     * Get an entity object from UUID
     *
     */
    public static Entity getEntityByID(UUID uniqueId){
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().equals(uniqueId)) {
                        return entity;
                    }
                }
            }
        }

        return null;
    }

    public static Entity getEntityByID(String uniqueId){
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().toString().equals(uniqueId)) {
                        return entity;
                    }
                }
            }
        }

        return null;
    }
    
    public static Location getLocation(CArray location) {
        Target t = Target.UNKNOWN;
        int x = Integer.valueOf(location.get(0, t).val());
        int y = Integer.valueOf(location.get(1, t).val());
        int z = Integer.valueOf(location.get(2, t).val());
        World world = Bukkit.getWorld(location.get(3, t).val());
        
        return new Location(world, x, y, z);
    }
}
