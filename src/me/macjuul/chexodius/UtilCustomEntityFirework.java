package me.macjuul.chexodius;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.server.v1_9_R1.EntityFireworks;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_9_R1.World;

public class UtilCustomEntityFirework extends EntityFireworks {
    public static void spawn(Location location, FireworkEffect effect, Player... players) {
        try {
            UtilCustomEntityFirework firework = new UtilCustomEntityFirework(
                    ((CraftWorld) location.getWorld()).getHandle(), players);
            FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
            meta.addEffect(effect);
            ((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
            firework.setPosition(location.getX(), location.getY(), location.getZ());
            if(((CraftWorld) location.getWorld()).getHandle().addEntity(firework)) {
                firework.setInvisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Player[] players = null;

    boolean gone = false;

    public UtilCustomEntityFirework(World world, Player... p) {
        super(world);
        players = p;
        a(new float[] { 0.25F, 0.25F });
    }

    public void t_() {
        if(gone) {
            return;
        }
        if(!world.isClientSide) {
            gone = true;
            if(players != null) {
                if(players.length > 0) {
                    Player[] arrayOfPlayer;
                    int j = (arrayOfPlayer = players).length;
                    for(int i = 0; i < j; i++) {
                        Player player = arrayOfPlayer[i];
                        ((CraftPlayer) player).getHandle().playerConnection
                                .sendPacket(new PacketPlayOutEntityStatus(this, (byte) 17));
                    }
                } else {
                    world.broadcastEntityEffect(this, (byte) 17);
                }
            }
            die();
        }
    }
}