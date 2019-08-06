package net.MayDayMemory.www.Events;

import java.util.Calendar;
import java.util.List;

import net.MayDayMemory.www.Main;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClick implements Listener{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory i =e.getInventory();
		List<HumanEntity> p = e.getViewers();
		String title = i.getTitle();
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		if (title.equals("§0§l"+month+"月签到簿")){ // 基于 title 的背包判定，建议更改为 InventoryHolder —— 754503921
			if(e.getSlot()+1==Calendar.getInstance().get(Calendar.DATE)){
				int slot = e.getSlot();
				ItemStack finished = Main.instance.getConfig().getItemStack("symbol.finished.mate");
				String name =Main.instance.getConfig().getString("symbol.finished.displayname").replaceAll("0day0",String.valueOf(slot+1));
				finished.setAmount(slot+1);
				ItemMeta newmeta =finished.getItemMeta();
				newmeta.setDisplayName(name);
				finished.setItemMeta(newmeta);
				if(!i.getItem(slot).equals(finished)){
					i.setItem(slot, finished);
					Main.instance.cmap.get(p.get(0).getUniqueId()).state[slot]="finished";
					Main.instance.cser((Player)p.get(0));
					for(String m:Main.instance.getConfig().getStringList("commands"))   ((Player) p.get(0)).chat(m);
					if(Main.instance.getConfig().contains("items")){
						List<?> items = Main.instance.getConfig().getList("items");
						if(!items.isEmpty()){
							for(Object item :items)p.get(0).getInventory().addItem((ItemStack)item);
						}
					}
					Location location = p.get(0).getLocation();
					location.getWorld().playEffect(location, Effect.BOW_FIRE, 1);
					for (int degree = 0; degree < 360; degree++) {
					    double radians = Math.toRadians(degree);
					    double x = Math.cos(radians);
					    double y = Math.sin(radians); // 迷惑命名 —— 754503921
					    location.add(x, 0, y);
					    location.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 1);
					    location.subtract(x, 0, y);
					}
					Main.instance.getServer().broadcastMessage("[Signer] §3§l玩家  §f"+p.get(0).getName()+" §3§l签到成功！");
				}
			}
			e.setCancelled(true);
		}
	}
}
