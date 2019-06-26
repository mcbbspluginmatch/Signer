package net.MayDayMemory.www.Events;

import org.bukkit.plugin.PluginManager;

import net.MayDayMemory.www.Main;

public class EventManager {
	public static void reg(){
		PluginManager pm = Main.instance.getServer().getPluginManager();
		pm.registerEvents(new InventoryClick(), Main.instance);
	}
}
