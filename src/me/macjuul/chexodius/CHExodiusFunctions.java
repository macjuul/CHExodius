package me.macjuul.chexodius;

import java.lang.reflect.Field;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.PureUtilities.Common.StringUtils;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCWorldCreator;
import com.laytonsmith.abstraction.StaticLayer;
import com.laytonsmith.abstraction.enums.MCWorldEnvironment;
import com.laytonsmith.abstraction.enums.MCWorldType;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CDouble;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.exceptions.CRE.CREBadEntityException;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.functions.AbstractFunction;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Blocks;
import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EnumSkyBlock;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockAction;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_9_R1.PacketPlayOutCamera;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PlayerConnection;

public class CHExodiusFunctions {
    public static String docs() {
        return "This Extension will add some sick things to CommandHelper!";
    }

    @api
    public static class set_tab_msg
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String headerraw = args[1].getValue();
            String footerraw = args[2].getValue();

            String header = "{\"text\": \"" + headerraw + "\"}";
            String footer = "{\"text\": \"" + footerraw + "\"}";

            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();

            PlayerConnection connection = p.getHandle().playerConnection;
            IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a(header);
            IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a(footer);
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, headerJSON);
                headerField.setAccessible(!headerField.isAccessible());

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, footerJSON);
                footerField.setAccessible(!footerField.isAccessible());
            } catch (Exception e) {
                e.printStackTrace();
            }
            connection.sendPacket(packet);

            return CBoolean.TRUE;
        }

        public String getName() {
            return "set_tab_msg";
        }

        public Integer[] numArgs() {
            return new Integer[] {
 Integer.valueOf(3)
            };
        }

        public String docs() {
            return "void {player, string Header, string Footer} Sets a header and footer message in the players TAB list";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class send_action_msg
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
 CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            String message = args[1].getValue();

            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();

            IChatBaseComponent txt = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat action = new PacketPlayOutChat(txt, (byte) 2);
            p.getHandle().playerConnection.sendPacket(action);

            return CVoid.VOID;
        }

        public String getName() {
            return "send_action_msg";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {player, message} Sends a message in the players actionbar";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class send_title
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();
            String title = args[1].getValue();
            String subtitle = args[2].getValue();
            Integer fadeIn = Integer.valueOf(Integer.parseInt(args[3].getValue()));
            Integer stay = Integer.valueOf(Integer.parseInt(args[4].getValue()));
            Integer fadeOut = Integer.valueOf(Integer.parseInt(args[5].getValue()));

            PlayerConnection connection = p.getHandle().playerConnection;

            IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle lengthPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, titleJSON, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON);
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
            connection.sendPacket(lengthPacket);
            connection.sendPacket(titlePacket);
            connection.sendPacket(subtitlePacket);

            return CVoid.VOID;
        }

        public String getName() {
            return "send_title";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(6)
            };
        }

        public String docs() {
            return "void {player, string title, string subtitle, int fadeIn, int stay, int fadeOut} Send a title message to a player";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class send_chest_packet
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        @SuppressWarnings("rawtypes")
        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();

            CArray loc = Static.getArray(args[1], t);

            double x = Math.floor(Double.valueOf(Static.getDouble(loc.get(0, t), t)).doubleValue()) + 0.5D;
            double y = Math.floor(Double.valueOf(Static.getDouble(loc.get(1, t), t)).doubleValue()) + 0.5D;
            double z = Math.floor(Double.valueOf(Static.getDouble(loc.get(2, t), t)).doubleValue()) + 0.5D;

            BlockPosition location = new BlockPosition(x, y, z);

            Boolean state = Boolean.valueOf(Static.getBoolean(args[2]));

            Packet chestOpen = new PacketPlayOutBlockAction(location, Blocks.CHEST, 1, state.booleanValue() ? 1 : 0);
            p.getHandle().playerConnection.sendPacket(chestOpen);

            return CVoid.VOID;
        }

        public String getName() {
            return "send_chest_packet";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(3)
            };
        }

        public String docs() {
            return "void {player, location array, state} Sends a fake chest opening and closing packet to the player. State is a boolean: true will open the chest, false will close it";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class set_hotbar_slot
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();

            int slot = Integer.valueOf(args[1].val()).intValue();
            p.getInventory().setHeldItemSlot(slot);

            return CVoid.VOID;
        }

        public String getName() {
            return "set_hotbar_slot";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(1), Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {player, int slot} Sets the slot the player is holding. Can be an int from 0 to 8";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class send_block_cracks
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();

            CArray loc = Static.getArray(args[1], t);

            double x = Double.valueOf(Static.getDouble(loc.get(0, t), t)).doubleValue();
            double y = Double.valueOf(Static.getDouble(loc.get(1, t), t)).doubleValue();
            double z = Double.valueOf(Static.getDouble(loc.get(2, t), t)).doubleValue();

            BlockPosition pos = new BlockPosition(x, y, z);
            int damage = Integer.valueOf(args[2].val()).intValue();
            int id;
            if (args.length == 4) {
                id = Integer.valueOf(args[3].val()).intValue();
            } else {
                id = new Random().nextInt(1000);
            }
            PacketPlayOutBlockBreakAnimation pack = new PacketPlayOutBlockBreakAnimation(id, pos, damage);
            p.getHandle().playerConnection.sendPacket(pack);

            return CVoid.VOID;
        }

        public String getName() {
            return "send_block_cracks";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(3), Integer.valueOf(4)
            };
        }

        public String docs() {
            return "void {player, location array, int crackSize, [int id]} Makes a fake block crack effect at the location. crackSize changes the size of the cracks. Supplying the function with an ID allows you to 'remove' the crack later by making a level 0 crack with the same ID";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class set_light_at
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CArray loc = Static.getArray(args[0], t);
            Short level = Short.valueOf(args[1].val());

            if((level < 0) || (level > 15)) {
                throw new CREIllegalArgumentException("The light level cannot be lower than 0 or be higher than 15", t);
            }

            double x = Double.valueOf(Static.getDouble(loc.get(0, t), t)).doubleValue();
            double y = Double.valueOf(Static.getDouble(loc.get(1, t), t)).doubleValue();
            double z = Double.valueOf(Static.getDouble(loc.get(2, t), t)).doubleValue();

            BlockPosition pos = new BlockPosition(x, y, z);

            net.minecraft.server.v1_9_R1.World w = ((CraftWorld) Bukkit.getWorld(loc.get(3, t).val())).getHandle();

            w.a(EnumSkyBlock.BLOCK, pos, level);

            return CVoid.VOID;
        }

        public String getName() {
            return "set_light_at";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {Location array, int level} Spawns a fake light source at the given location";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class user_input
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(final Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            final CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();
            final CClosure callback = (CClosure) args[1];
            
            UtilAnvilGUI gui = new UtilAnvilGUI(p, new UtilAnvilGUI.AnvilClickEventHandler() {
                public void onAnvilClick(UtilAnvilGUI.AnvilClickEvent event) {
                    if (event.getSlot() == UtilAnvilGUI.AnvilSlot.OUTPUT) {
                        event.setWillClose(true);
                        event.setWillDestroy(true);

                        callback.execute(new CString(event.getName(), t));
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                    }
                }
            });
            
            ItemStack item;
            if(args.length == 3) {
                item = (ItemStack) ObjectGenerator.GetGenerator().item(Static.getArray(args[2], t), t).getHandle();
            } else {
                item = new ItemStack(Material.NAME_TAG);
                ItemMeta m = item.getItemMeta();
                m.setDisplayName("Enter input...");
                item.setItemMeta(m);
            }
            

            gui.setSlot(UtilAnvilGUI.AnvilSlot.INPUT_LEFT, item);

            gui.open();

            return CVoid.VOID;
        }

        public String getName() {
            return "user_input";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2), Integer.valueOf(3)
            };
        }

        public String docs() {
            return "void {player, callback closure, [item array]} Open an Anvil GUI input for player, calling the callback closure when the player submits the input." + "The text the player typed in gets returned to the closure. Item can be an item array, already only the keys 'type', 'data' and 'display' are used. ";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class json_msg
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            String msg;
            CraftPlayer player;
            if (args.length == 1) {
                player = (CraftPlayer)environment.getEnv(CommandHelperEnvironment.class).GetPlayer().getHandle();
                msg = args[0].val();
            } else {
                player = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();
                msg = args[1].val();
            }
            Gson gson = new Gson();
            try {
                gson.fromJson(msg, Object.class);

                IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(msg);
                PacketPlayOutChat packet = new PacketPlayOutChat(comp);
                player.getHandle().playerConnection.sendPacket(packet);
            } catch (JsonSyntaxException ex) {
                throw new CRECastException("Invalid JSON string", t);
            }
            return CVoid.VOID;
        }

        public String getName() {
            return "json_msg";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(1), Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {[player], string message} Sends a JSON message to the player. This JSON string uses the same format as used in the tellraw command.";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class set_pspectating
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            CraftPlayer p = (CraftPlayer) Static.GetPlayer(args[0], t).getHandle();
            CraftEntity victim = null;
            if (args[1] instanceof CNull) {
                victim = p;
            } else {
                victim = (CraftEntity) UtilClass.getEntityByID(args[1].val());
                if (victim == null) {
                    throw new CREBadEntityException("Entity " + args[1].val() + " does not exist!", t);
                }
            }

            PacketPlayOutCamera pack = new PacketPlayOutCamera(victim.getHandle());
            p.getHandle().playerConnection.sendPacket(pack);

            return CVoid.VOID;
        }

        public String getName() {
            return "set_pspectating";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {player, EntityID | player, null} Makes the player spectate the given entity. Setting the entity id to null resets the players vision";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class set_entity_target
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            Entity attacker = UtilClass.getEntityByID(args[0].val());
            Entity victim = UtilClass.getEntityByID(args[1].val());

            EntityCreature ec = (EntityCreature)((CraftEntity) attacker).getHandle();
            ec.b((EntityLiving)((CraftEntity) victim).getHandle());

            return CVoid.VOID;
        }

        public String getName() {
            return "set_entity_target";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {AttackerEntityId, VictimEntityId} make an entity target another mob. This function is very likeley to do nothing at all.";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class get_tps
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return false;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            @SuppressWarnings("deprecation")
            double[] tps = MinecraftServer.getServer().recentTps;
            CArray ret = new CArray(t);
            double[] arrayOfDouble1;
            int j = (arrayOfDouble1 = tps).length;
            for (int i = 0; i < j; i++) {
                Double f = Double.valueOf(arrayOfDouble1[i]);
                ret.push(new CDouble(f.doubleValue(), t), t);
            }
            return ret;
        }

        public String getName() {
            return "get_tps";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(0)
            };
        }

        public String docs() {
            return "array {} Returns an array containing the TPS over the last 1, 10 and 15 minutes. Only works for Spigot";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api(environments = {
            CommandHelperEnvironment.class
    })
    public static class catched_create_world
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CREFormatException.class,
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment env, Construct...args)
                throws ConfigRuntimeException {
            MCWorldCreator creator = StaticLayer.GetConvertor().getWorldCreator(args[0].val());
            if (args.length >= 3) {
                MCWorldType type;
                try {
                    type = MCWorldType.valueOf(args[1].val().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new CREFormatException(args[1].val() + " is not a valid world type. Must be one of: " + StringUtils.Join(MCWorldType.values(), ", "), t);
                }
                MCWorldEnvironment environment;
                try {
                    environment = MCWorldEnvironment.valueOf(args[2].val().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new CREFormatException(args[2].val() + " is not a valid environment type. Must be one of: " + StringUtils.Join(MCWorldEnvironment.values(), ", "), t);
                }
                creator.type(type).environment(environment);
            }
            if ((args.length >= 4) && (!(args[3] instanceof CNull))) {
                if ((args[3] instanceof CInt)) {
                    creator.seed(Static.getInt(args[3], t));
                } else {
                    creator.seed(args[3].val().hashCode());
                }
            }
            if (args.length == 5) {
                creator.generator(args[4].val());
            }
            try {
                creator.createWorld();
            } catch (Exception e) {
                System.out.println("CommandHelper: Catched error in world load > code continued");
            }
            return CVoid.VOID;
        }

        public String getName() {
            return "catched_create_world";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)
            };
        }

        public String docs() {
            return "void {<See create_world()>} VERY unstable command that could be harming to your worlds. This function allows worlds to load Async without throwing errors. I strongly advice you to not use this unless you know what you are doing. This uses the exact same syntax as create_world()";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class launch_instant_firework
    extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args)
                throws ConfigRuntimeException {
            Boolean flicker = Boolean.valueOf(false);
            Boolean trail = Boolean.valueOf(false);
            CArray color = new CArray(t);
            String type = "ball";
            switch (args.length) {
                case 1:
                    color.push(new CInt(255, t), t);
                    color.push(new CInt(255, t), t);
                    color.push(new CInt(255, t), t);
                    break;
                case 2:
                    color = Static.getArray(args[1], t);
                    break;
                case 3:
                    color = Static.getArray(args[1], t);
                    type = args[2].val();
                    break;
                case 4:
                    color = Static.getArray(args[1], t);
                    type = args[2].val();
                    flicker = Boolean.valueOf(args[3].val());
                    break;
                case 5:
                    color = Static.getArray(args[1], t);
                    type = args[2].val();
                    flicker = Boolean.valueOf(args[3].val());
                    trail = Boolean.valueOf(args[4].val());
            }
            if (!Integer.valueOf((int) color.size()).equals(3)) {
                throw new CREFormatException("Color has to be an RGB array with 3 values", t);
            }
            FireworkEffect.Builder builder = FireworkEffect.builder();

            int R = Integer.valueOf(color.get(0, t).val());
            int G = Integer.valueOf(color.get(1, t).val());
            int B = Integer.valueOf(color.get(2, t).val());

            builder.withColor(Color.fromRGB(R, G, B));

            if (type.equalsIgnoreCase("ball")) {
                builder.with(FireworkEffect.Type.BALL);
            }
            if (type.equalsIgnoreCase("largeball")) {
                builder.with(FireworkEffect.Type.BALL_LARGE);
            }
            if (type.equalsIgnoreCase("star")) {
                builder.with(FireworkEffect.Type.STAR);
            }
            if (type.equalsIgnoreCase("creeper")) {
                builder.with(FireworkEffect.Type.CREEPER);
            }
            if (type.equalsIgnoreCase("burst")) {
                builder.with(FireworkEffect.Type.BURST);
            }
            MCLocation l = ObjectGenerator.GetGenerator().location(args[0], null, t);

            World w = (World) l.getWorld().getHandle();
            Location loc = new Location(w, l.getX(), l.getY(), l.getZ());

            FireworkEffect effect = builder.flicker(flicker.booleanValue()).trail(trail.booleanValue()).build();

            UtilCustomEntityFirework.spawn(loc, effect, new Player[0]);

            return CVoid.VOID;
        }

        public String getName() {
            return "launch_instant_firework";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4),
                    Integer.valueOf(5)
            };
        }

        public String docs() {
            return "void {locationArray, [color], [type], [flicker], [trail]} Launch an instantly detonating firework. Color accepts an array containing 3 ints, using RGB";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }

    @api
    public static class set_entity_advanced_spec extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args) throws ConfigRuntimeException {
            Entity e = UtilClass.getEntityByID(args[0].val());
            if(e == null) {
                throw new CREBadEntityException("The entity with ID " + args[0].val() + " does not exist", t);
            }

            CArray spec = Static.getArray(args[1], t);

            net.minecraft.server.v1_9_R1.Entity nmsEntity = ((CraftEntity) e).getHandle();

            NBTTagCompound tag = new NBTTagCompound();

            nmsEntity.c(tag);

            if(spec.containsKey("NoAI")) {
                tag.setBoolean("NoAI", Boolean.valueOf(spec.get("NoAI", t).val()));
            }

            if(spec.containsKey("Ignited")) {
                tag.setBoolean("ignited", Boolean.valueOf(spec.get("Ignited", t).val()));
            }

            EntityLiving el = (EntityLiving) nmsEntity;
            el.a(tag);

            return CVoid.VOID;
        }

        public String getName() {
            return "set_entity_advanced_spec";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {Entity ID, spec array} Sets special data on entities that CommandHelper does not support. This function will not assign different specs to different entities, so be carefull when you apply a tag to the wrong entity, since this might cause uninteded behavoir. Currently supported values are (Boolean) NoAI and (Boolean)[Creepers] Ignited";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class set_skull_at extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args) throws ConfigRuntimeException {
            CArray location = Static.getArray(args[0], t);
            String name = args[1].val();
            
            Block block = UtilClass.getLocation(location).getBlock();
            
            BlockState blockState = block.getState();
             
            if (blockState instanceof Skull) {
                Skull skull = (Skull) blockState;
                skull.setOwner(name);
                skull.update();
            } else throw new CRENotFoundException("The block at that location is not a head", t);

            return CVoid.VOID;
        }

        public String getName() {
            return "set_skull_at";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(2)
            };
        }

        public String docs() {
            return "void {Location array, name} Change the skull owner of a player head";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class get_skull_at extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }

        public Construct exec(Target t, Environment environment, Construct...args) throws ConfigRuntimeException {
            CArray location = Static.getArray(args[0], t);
            String name;
            
            Location loc = UtilClass.getLocation(location);
            Block block = loc.getBlock();
            
            BlockState blockState = block.getState();
             
            if (blockState instanceof Skull) {
                Skull skull = (Skull) blockState;
                name = skull.getOwner();
            } else throw new CRENotFoundException("The block at that location is not a head", t);

            return new CString(name, t);
        }

        public String getName() {
            return "get_skull_at";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                    Integer.valueOf(1)
            };
        }

        public String docs() {
            return "String {Location array} Gets the skull owner of the head at the given location";
        }

        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class get_ptarget extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCPlayer p = ((CommandHelperEnvironment)environment.getEnv(CommandHelperEnvironment.class)).GetPlayer();
            if(args.length == 1) {
                p = Static.GetPlayer(args[0], t);
            }
            Static.AssertPlayerNonNull(p, t);
            Player player = (Player)p.getHandle();
            Entity target = null;
            double targertDistanceSquared = 0.0D;
            Vector l = player.getEyeLocation().toVector();
            Vector n = player.getLocation().getDirection().normalize();
            double cos45 = Math.cos(0.7853981633974483D);
            for(LivingEntity other : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                if(other != player) {
                    if((target == null) || (targertDistanceSquared > other.getLocation().distanceSquared(player.getLocation()))) {
                        Vector v = other.getLocation().add(0.0D, 1.0D, 0.0D).toVector().subtract(l);
                        if((n.clone().crossProduct(v).lengthSquared() < 1.0D) && (v.normalize().dot(n) >= cos45)) {
                            target = other;
                            targertDistanceSquared = target.getLocation().distanceSquared(player.getLocation());
                        }
                    }
                }
            }
            if(target == null) {
                return CNull.NULL;
            }
            return new CString(target.getUniqueId().toString(), t);
        }
  
        public String getName() {
            return "get_ptarget";
        }
      
        public Integer[] numArgs() {
            return new Integer[] { Integer.valueOf(1), Integer.valueOf(0) };
        }
      
        public String docs() {
            return "Integer {[Player]} gets the entity id of the entity the player is looking at";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class popen_villager_trade extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Player p;
            String u;
            Boolean f;
            
            if(args.length == 2) {
                p = (Player) ((CommandHelperEnvironment)environment.getEnv(CommandHelperEnvironment.class)).GetPlayer().getHandle();
                u = args[0].val();
                f = Boolean.valueOf(args[1].val());
            } else {
                p = Bukkit.getPlayer(args[0].val());
                u = args[1].val();
                f = Boolean.valueOf(args[2].val());
            }
            
            World w = p.getWorld();
            
            for(Villager v : w.getEntitiesByClass(Villager.class)) {
                if(v.getUniqueId().toString().equals(u)) {
                    p.openMerchant(v, f);
                    break;
                }
            }
            
            return CVoid.VOID;
        }
  
        public String getName() {
            return "popen_villager_trade";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {2, 3};
        }
      
        public String docs() {
            return "void {[Player], Entity ID, Boolean force} Opens a villager trading GUI. If force is true the current player trading with the villager exits the trade";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class open_elytra extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Player p;
            
            if(args.length == 0) {
                p = (Player) ((CommandHelperEnvironment)environment.getEnv(CommandHelperEnvironment.class)).GetPlayer().getHandle();
            } else {
                p = Bukkit.getPlayer(args[0].val());
            }
            
            if(p.getEquipment().getChestplate().getType() != Material.ELYTRA || ((Entity) p).isOnGround()) {
                throw new CREException("The specified player is not ready to fly", t);
            }
            
            ((CraftPlayer) p).getHandle().setFlag(7, true);
            
            return CVoid.VOID;
        }
  
        public String getName() {
            return "open_elytra";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {0, 1};
        }
      
        public String docs() {
            return "void {[Player]} Opens the players Elytra";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class set_entity_glowing extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Entity e = (Entity) Static.getEntity(args[0], t).getHandle();
            e.setGlowing(Static.getBoolean(args[1]));
            
            return CVoid.VOID;
        }
  
        public String getName() {
            return "set_entity_glowing";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {2};
        }
      
        public String docs() {
            return "void {Entity, glowing} Sets the entity to glow";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class get_entity_glowing extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Entity e = (Entity) Static.getEntity(args[0], t).getHandle();
            
            return CBoolean.GenerateCBoolean(e.isGlowing(), t);
        }
  
        public String getName() {
            return "get_entity_glowing";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {1};
        }
      
        public String docs() {
            return "boolean {Entity} gets if the entity is glowing";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class set_pglowing extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Player p;
            Boolean g;
            
            if(args.length == 1) {
                p = (Player) ((CommandHelperEnvironment)environment.getEnv(CommandHelperEnvironment.class)).GetPlayer().getHandle();
                g = Boolean.valueOf(args[0].val());
            } else {
                p = Bukkit.getPlayer(args[0].val());
                g = Boolean.valueOf(args[1].val());
            }
            
            p.setGlowing(g);
            
            return CVoid.VOID;
        }
  
        public String getName() {
            return "set_pglowing";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {1, 2};
        }
      
        public String docs() {
            return "void {[Player], glowing} Sets the player to glow";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
    
    @api
    public static class get_pglowing extends AbstractFunction {
        @SuppressWarnings("unchecked")
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[] {
                    CRECastException.class
            };
        }
      
        public boolean isRestricted() {
            return false;
        }
      
        public Boolean runAsync() {
            return Boolean.valueOf(false);
        }
      
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Player p;
            
            if(args.length == 0) {
                p = (Player) ((CommandHelperEnvironment)environment.getEnv(CommandHelperEnvironment.class)).GetPlayer().getHandle();
            } else {
                p = Bukkit.getPlayer(args[0].val());
            }
            
            return CBoolean.GenerateCBoolean(p.isGlowing(), t);
        }
  
        public String getName() {
            return "get_pglowing";
        }
      
        public Integer[] numArgs() {
            return new Integer[] {0, 1};
        }
      
        public String docs() {
            return "void {[Player]} Gets if the player is glowing";
        }
      
        public Version since() {
            return CHVersion.V3_3_2;
        }
    }
}
