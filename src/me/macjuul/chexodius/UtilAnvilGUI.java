package me.macjuul.chexodius;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.ContainerAnvil;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenWindow;

public class UtilAnvilGUI {
    public class AnvilClickEvent {
        private UtilAnvilGUI.AnvilSlot slot;
        private String name;
        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(UtilAnvilGUI anvilGUI, UtilAnvilGUI.AnvilSlot slot, String name) {
            this.slot = slot;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public UtilAnvilGUI.AnvilSlot getSlot() {
            return slot;
        }

        public boolean getWillClose() {
            return close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
    public static abstract interface AnvilClickEventHandler {
        public abstract void onAnvilClick(UtilAnvilGUI.AnvilClickEvent paramAnvilClickEvent);
    }

    private class AnvilContainer
    extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    public static enum AnvilSlot {
        INPUT_LEFT(0), INPUT_RIGHT(1), OUTPUT(2);

        public static AnvilSlot bySlot(int slot) {
            AnvilSlot[] arrayOfAnvilSlot;
            int j = (arrayOfAnvilSlot = values()).length;
            for (int i = 0; i < j; i++) {
                AnvilSlot anvilSlot = arrayOfAnvilSlot[i];
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }
            return null;
        }

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }

    private Player player;
    private HashMap < AnvilSlot, ItemStack > items = new HashMap < AnvilSlot, ItemStack > ();
    private Inventory inv;

    private Listener listener;

    public UtilAnvilGUI(Player player, final AnvilClickEventHandler handler) {
        this.player = player;
        listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
            if ((event.getWhoClicked() instanceof Player)) {
                event.getWhoClicked();
                if (event.getInventory().equals(inv)) {
                    event.setCancelled(true);

                    ItemStack item = event.getCurrentItem();
                    int slot = event.getRawSlot();
                    String name = "";
                    if ((item != null) && (item.hasItemMeta())) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.hasDisplayName()) {
                            name = meta.getDisplayName();
                        }
                    }
                    UtilAnvilGUI.AnvilClickEvent clickEvent = new UtilAnvilGUI.AnvilClickEvent(UtilAnvilGUI.this, UtilAnvilGUI.AnvilSlot.bySlot(slot), name);

                    handler.onAnvilClick(clickEvent);
                    if (clickEvent.getWillClose()) {
                        event.getWhoClicked().closeInventory();
                    }
                    if (clickEvent.getWillDestroy()) {
                        UtilAnvilGUI.this.destroy();
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if ((event.getPlayer() instanceof Player)) {
                event.getPlayer();
                Inventory inv = event.getInventory();
                if (inv.equals(UtilAnvilGUI.this.inv)) {
                    inv.clear();
                    UtilAnvilGUI.this.destroy();
                }
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            if (event.getPlayer().equals(UtilAnvilGUI.this.getPlayer())) {
                UtilAnvilGUI.this.destroy();
            }
        }
        };
        Bukkit.getPluginManager().registerEvents(listener, Bukkit.getPluginManager().getPlugin("CommandHelper"));
    }

    public void destroy() {
        player = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void open() {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        inv = container.getBukkitView().getTopInventory();
        for (AnvilSlot slot: items.keySet()) {
            inv.setItem(slot.getSlot(), items.get(slot));
        }
        int c = p.nextContainerCounter();

        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0])));

        p.activeContainer = container;

        p.activeContainer.windowId = c;

        p.activeContainer.addSlotListener(p);
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }
}