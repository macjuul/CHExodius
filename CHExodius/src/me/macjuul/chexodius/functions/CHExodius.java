package me.macjuul.chexodius.functions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.PureUtilities.Common.StringUtils;
import com.laytonsmith.abstraction.MCLocation;
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
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;

import java.lang.reflect.Field;
import java.util.Random;
import me.macjuul.chexodius.classes.AnvilGUI;
import me.macjuul.chexodius.classes.CustomEntityFirework;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CHExodius {
    public static String docs() {
        return "This Extension will add some sick things to CommandHelper!";
    }

    @api
    public static class set_tab_msg
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            return "Set a TAB list message";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class send_action_msg
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            return "Send an action bar message to the player";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class send_title
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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

            IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + title + "'}");
            IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + subtitle + "'}");
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
            return "Display txt";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class send_chest_packet
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            return "Chest anim";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class set_hotbar_slot
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            return "Set a hotbar slot";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class send_block_cracks
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            return "Crack!";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class user_input
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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

            int id = 421;
            int data = 0;
            String name = "Enter input...";
            if (new Integer(args.length).equals(3)) {
                CArray item = Static.getArray(args[2], t);
                if (item.containsKey("type")) {
                    id = Integer.valueOf(item.get("type", t).val());
                }
                if (item.containsKey("data")) {
                    data = Integer.valueOf(item.get("data", t).val());
                }
                if (item.containsKey("meta")) {
                    CArray meta = Static.getArray(item.get("meta", t), t);
                    if (meta.containsKey("display")) {
                        name = meta.get("display", t).val();
                    }
                }
            }
            AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
                    if (event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
                        event.setWillClose(true);
                        event.setWillDestroy(true);

                        callback.execute(new CString(event.getName(), t));
                    } else {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                    }
                }
            });@SuppressWarnings("deprecation")
            ItemStack item = new ItemStack(id, 1, (short) data);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);

            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, item);

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
            return "void {Player, Callback, [Item]} Open an Anvil GUI input for player, calling the callback closure when the player submits the input." + "The text the player typed in gets returned to the closure. Item can be an item array, already only the keys 'type', 'data' and 'display' are used. ";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class json_msg
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
                player = (CraftPlayer)((CommandHelperEnvironment) environment.getEnv(CommandHelperEnvironment.class)).GetPlayer().getHandle();
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
                throw new ConfigRuntimeException("Invalid JSON string", Exceptions.ExceptionType.CastException, t);
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
            return "void {[player], String} Sends a JSON message to the player. This JSON string uses the same format as used in the tellraw command.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class set_pspectating
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
                for (World world: Bukkit.getWorlds()) {
                    for (Entity entity: world.getEntities()) {
                        String id = entity.getUniqueId().toString();
                        if (id.equals(args[1].val())) {
                            victim = (CraftEntity) entity;
                        }
                    }
                }

                if (victim == null) {
                    throw new ConfigRuntimeException("Entity " + args[1].val() + "does not exist!", ExceptionType.BadEntityException, t);
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
            return ":O";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class set_entity_target
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.PlayerOfflineException, Exceptions.ExceptionType.CastException
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
            Entity attacker = null;
            Entity victim = null;
            for (World world: Bukkit.getWorlds()) {
                for (Entity entity: world.getEntities()) {
                    if (Integer.toString(entity.getEntityId()).equals(args[0].val())) {
                        if ((entity instanceof CraftPlayer)) {
                            throw new ConfigRuntimeException("Cannot set target on entity Player", Exceptions.ExceptionType.CastException, t);
                        }
                        attacker = entity;
                    } else if (Integer.toString(entity.getEntityId()).equals(args[1].val())) {
                        victim = entity;
                    }
                }
            }
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
            return "void {AttackerEntityId, VictimEntityId} make an entity target another mob. The the raget behavoir is different for each mob type.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class get_tps
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.CastException
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
            double[] tps = MinecraftServer.getServer().recentTps;
            CArray ret = new CArray(t);
            double[] arrayOfDouble1;
            int j = (arrayOfDouble1 = tps).length;
            for (int i = 0; i < j; i++) {
                Double f = Double.valueOf(arrayOfDouble1[i]);
                ret.push(new CDouble(f.doubleValue(), t));
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
            return "double {void} Get the current server TPS";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api(environments = {
        CommandHelperEnvironment.class
    })
    public static class catched_create_world
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.FormatException, Exceptions.ExceptionType.CastException
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
                    throw new ConfigRuntimeException(args[1].val() + " is not a valid world type. Must be one of: " + StringUtils.Join(MCWorldType.values(), ", "), Exceptions.ExceptionType.FormatException, t);
                }
                MCWorldEnvironment environment;
                try {
                    environment = MCWorldEnvironment.valueOf(args[2].val().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigRuntimeException(args[2].val() + " is not a valid environment type. Must be one of: " + StringUtils.Join(MCWorldEnvironment.values(), ", "), Exceptions.ExceptionType.FormatException, t);
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
            return "- {-} Same syntax as create_world()";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    @api
    public static class launch_instant_firework
    extends AbstractFunction {
        public Exceptions.ExceptionType[] thrown() {
            return new Exceptions.ExceptionType[] {
                Exceptions.ExceptionType.CastException
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
                    color.push(new CInt(255, t));
                    color.push(new CInt(255, t));
                    color.push(new CInt(255, t));
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
                throw new ConfigRuntimeException("Color has to be an RGB array with 3 values", ExceptionType.FormatException, t);
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

            CustomEntityFirework.spawn(loc, effect, new Player[0]);

            return CVoid.VOID;
        }

        public String getName() {
            return "launch_instant_firework";
        }

        public Integer[] numArgs() {
            return new Integer[] {
                Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)
            };
        }

        public String docs() {
            return "void {locationArray, [color], [type], [flicker], [trail]} Launch an insantly detonating firework.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
}