package me.macjuul.chexodius;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class UtilClass {

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
}
