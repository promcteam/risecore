package me.travja.darkrise.core.legacy.util.message;

import me.travja.darkrise.core.util.ReflectionUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class NMSPlayerUtils {


    /**
     * Convert an item to a NBTTagCompound and get the HoverEvent from it
     *
     * @param itemStack The {@link ItemStack} to convert
     * @return a {@link HoverEvent} object representing the item
     */
    public static HoverEvent convert(ItemStack itemStack) {
        try { //Use reflection for version independence.
            Class  cItemClass = ReflectionUtil.getBukkitClass("inventory.CraftItemStack");
            Method asNMSCopy  = cItemClass.getMethod("asNMSCopy", ItemStack.class);
            Object cItem      = asNMSCopy.invoke(cItemClass, itemStack);
            Class tagClass = ReflectionUtil.isNewVersion()
                    ? Class.forName("net.minecraft.nbt.NBTTagCompound")
                    : ReflectionUtil.getNMSClass("NBTTagCompound");
            Object tagCompound;
            if (ReflectionUtil.isVersionGreater(18))
                //Save method in 1.18
                tagCompound = cItem.getClass().getMethod("b", tagClass).invoke(cItem, tagClass.newInstance());
            else
                tagCompound = cItem.getClass().getMethod("save", tagClass).invoke(cItem, tagClass.newInstance());

            return new HoverEvent(
                    Action.SHOW_ITEM,
                    new ArrayList(Collections.singletonList(new Text(new BaseComponent[]{new TextComponent(tagCompound.toString())})))
            );
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
        //return new HoverEvent(Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString())});
    }

    public void sendMessage(BaseComponent[] msg, ChatMessageType chatPosition, Player player) {
        player.spigot().sendMessage(chatPosition, msg);
//		try {
//			Class cpClass = ReflectionUtil.getBukkitClass("entity.CraftPlayer");
//			Class pcktClass = ReflectionUtil.getNMSClass("PacketPlayOutChat");
//			Constructor pcktCtor = pcktClass.getConstructor()
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		CraftPlayer p = (CraftPlayer) player;
//        PacketPlayOutChat packet = new PacketPlayOutChat(null, ChatMessageType.a((byte) chatPosition.ordinal()));
//        packet.components = msg;
//        p.getHandle().playerConnection.sendPacket(packet);
    }
}