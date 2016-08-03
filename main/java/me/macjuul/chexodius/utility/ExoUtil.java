package me.macjuul.chexodius.utility;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;

import me.macjuul.chexodius.enums.DefaultFontInfo;

public class ExoUtil {
    private final static int CENTER_PX = 154;

    /**
     * Get an entity object from UUID
     *
     */
    public static Entity getEntityByID(UUID uniqueId) {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                for(Entity entity : chunk.getEntities()) {
                    if(entity.getUniqueId().equals(uniqueId)) {
                        return entity;
                    }
                }
            }
        }

        return null;
    }

    public static Entity getEntityByID(String uniqueId) {
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                for(Entity entity : chunk.getEntities()) {
                    if(entity.getUniqueId().toString().equals(uniqueId)) {
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

    public static CString getCenteredMessage(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
                continue;
            } else if(previousCode == true) {
                previousCode = false;
                if(c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        
        while(compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        
        return new CString(sb.toString() + message, Target.UNKNOWN);
    }
}
