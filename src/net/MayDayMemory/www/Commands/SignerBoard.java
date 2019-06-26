package net.MayDayMemory.www.Commands;

import java.util.Calendar;
import java.util.UUID;

import net.MayDayMemory.www.Main;
import net.MayDayMemory.www.Signer.Calen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			Calen c;
			if(Main.instance.cmap.equals(puuid)){
				c= Main.instance.cmap.get(puuid);
			}else{
				Main.instance.cunser(p);
				c= Main.instance.cmap.get(puuid);
			}
			int lasty = c.lasty;
			int lastm = c.lastm;
			Calendar calendar = Calendar.getInstance();
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
			int month = Calendar.getInstance().get(Calendar.MONTH)+1;
			Inventory i =Bukkit.createInventory(p,36,"§0§l"+month+"月签到簿");
			for(int f = 0;f<c.sticky.length;f++){
				i.setItem(f, new ItemStack(c.sticky[f],f+1));
			}
			for(int f2=0;f2<calendar.get(Calendar.DATE);f2++){
				if(i.getItem(f2).equals(new ItemStack(Material.SNOW_BLOCK,f2+1))){ 
					i.setItem(f2,new ItemStack(Material.ICE,f2+1));
					c.sticky[f2]=Material.ICE;
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
