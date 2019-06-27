package net.MayDayMemory.www.Commands;

import java.util.Calendar;
import java.util.UUID;

import net.MayDayMemory.www.Main;
import net.MayDayMemory.www.Signer.Calen;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SignerBoard extends Command{
	public String permission = "Signer.default";

	public SignerBoard(String name) {
		super(name);
		setPermission(this.permission);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			UUID puuid = p.getUniqueId();
			Calendar calendar = Calendar.getInstance();
			Calen c;
			if(Main.instance.cmap.equals(puuid)){
				c= Main.instance.cmap.get(puuid);
			}else{
				Main.instance.cunser(p);
				c= Main.instance.cmap.get(puuid);
			}
			int lasty = c.lasty;
			int lastm = c.lastm;
			if(calendar.get(Calendar.YEAR)!=lasty){
				c=new Calen(calendar);
				Main.instance.cmap.put(puuid, c);
				Main.instance.cser(p);
			}
			if(calendar.get(Calendar.MONTH)!=lastm){
				c=new Calen(calendar);
				Main.instance.cmap.put(puuid, c);
				Main.instance.cser(p);
			}
			Configuration config = Main.instance.getConfig();
			int month = Calendar.getInstance().get(Calendar.MONTH)+1;
			Inventory i =Bukkit.createInventory(p,36,"§0§l"+month+"月签到簿");
			for(int f = 0;f<c.state.length;f++){
				ItemStack sticky = config.getItemStack("symbol."+c.state[f]+".mate");
				sticky.setAmount(f+1);
				ItemMeta newmeta =sticky.getItemMeta();
				newmeta.setDisplayName(config.getString("symbol."+c.state[f]+".displayname").replaceAll("0day0",String.valueOf(f+1)));
				sticky.setItemMeta(newmeta);
				i.setItem(f,sticky);
			}
			for(int f2=0;f2<calendar.get(Calendar.DATE);f2++){
				ItemStack check =config.getItemStack("symbol.comming.mate");
				ItemMeta newmeta2 =check.getItemMeta();
				newmeta2.setDisplayName(config.getString("symbol.comming.displayname").replaceAll("0day0",String.valueOf(f2+1)));
				check.setItemMeta(newmeta2);
				check.setAmount(f2+1);
				if(i.getItem(f2).equals(check)){ 
					ItemStack set =config.getItemStack("symbol.unfinished.mate");
					String setname =config.getString("symbol.unfinished.displayname").replaceAll("0day0",String.valueOf(f2+1));
					set.setAmount(f2+1);
					ItemMeta newmeta = set.getItemMeta();
					newmeta.setDisplayName(setname);
					set.setItemMeta(newmeta);
					i.setItem(f2,set);
					c.state[f2]="unfinished";
					Main.instance.cser(p);
				}
			}
			p.closeInventory();
			p.openInventory(i);
			return true;
		}else{
			sender.sendMessage("§c只有玩家可以执行这个命令！");
			return true;
		}
	}
}
