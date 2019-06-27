package net.MayDayMemory.www.Commands;

//import java.util.Calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.MayDayMemory.www.Main;
import net.MayDayMemory.www.Signer.Calen;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Signer extends Command{
	public String permission = "Signer.admin";

	public Signer(String name) {
		super(name);
		setPermission(this.permission);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(args.length<1){
			sender.sendMessage("/Signer reload   重载配置文件");
			sender.sendMessage("/Signer board 或者/Signer b  打开签到簿");
			sender.sendMessage("/Signer addcommand [想要加的指令]");
			sender.sendMessage("/Signer commandslist  查看已添加的指令表");
			sender.sendMessage("/Signer remove [想要移除的指令编号]  可以用commandslist查看指令编号。");
			sender.sendMessage("/Signer setitem  将签到获得物品设置成你背包里已有的物品。");
			sender.sendMessage("/Signer setsymbol [finished/unfinished/comming]  将手上物品设置为[完成/未完成/未来]的图标");
			return true;
		}
		if(args[0].equals("reload")){
			Main.instance.reloadConfig();
			sender.sendMessage("配置文件重载！");
			return true;
		}
		
		/*
		if(args[0].equals("settime")){
			if(args.length<2){
				Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);
				Main.instance.getConfig().set("refresh.time.hour", hour);
				Main.instance.getConfig().set("refresh.time.minute", minute);
				Main.instance.getConfig().set("refresh.time.second", second);
				sender.sendMessage("§a设置刷新时间为："+hour+":"+minute+":"+second);
			}else{
				if(Integer.parseInt(args[1])>-1&&Integer.parseInt(args[1])<24){
					Main.instance.getConfig().set("refresh.time.hour",Integer.parseInt(args[1]));
				}else{
					sender.sendMessage("§c请输入正确的时间数字！");
					return true;
				}
				if(Integer.parseInt(args[2])>-1&&Integer.parseInt(args[2])<61){
					Main.instance.getConfig().set("refresh.time.minute",Integer.parseInt(args[2]));
				}else{
					sender.sendMessage("§c请输入正确的时间数字！");
					return true;
				}
				if(Integer.parseInt(args[3])>-1&&Integer.parseInt(args[3])<61){
					Main.instance.getConfig().set("refresh.time.second",Integer.parseInt(args[3]));
				}else{
					sender.sendMessage("§c请输入正确的时间数字！");
					return true;
				}
				sender.sendMessage("§a设置刷新时间为："+args[1]+":"+args[2]+":"+args[3]);
			}
			Main.instance.saveConfig();
			return true;
		}*/
		
		//repairing
		if(args[0].equals("board")||args[0].equals("b")){
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
		
		if(args[0].equals("addcommand")){
			if(args.length<2){
				sender.sendMessage("参数不足！请输入/Signer查看帮助");
				return true;
			}
			List<String> cl = Main.instance.getConfig().getStringList("commands");
			String c = "";
			for(int f = 1;f<args.length;f++){
				c+=args[f];
				c+=" ";
			}
			cl.add(c);
			Main.instance.getConfig().set("commands", cl);
			Main.instance.saveConfig();
			sender.sendMessage("[Signer]添加了 "+c+" 到指令表中！");
			return true;
		}
		
		if(args[0].equals("commandslist")){
			List<String> cl = Main.instance.getConfig().getStringList("commands");
			sender.sendMessage("[Signer]指令列表：");
			for(int f = 1;f<=cl.size();f++){
				sender.sendMessage(f+"."+cl.get(f-1));
			}
			return true;
		}
		
		if(args[0].equals("remove")){
			if(args.length<2){
				sender.sendMessage("参数不足！请输入/Signer查看帮助。");
				return true;
			}
			List<String> cl = Main.instance.getConfig().getStringList("commands");
			int index = Integer.parseInt(args[1])-1;
			if(index>cl.size()||index<0){
				sender.sendMessage("您输入的编号不正确！请输入/Signer commandslist查看指令列表。");
				return true;
			}
			sender.sendMessage("您移除了指令 "+cl.get(index));
			cl.remove(index);
			Main.instance.getConfig().set("commands", cl);
			Main.instance.saveConfig();
			return true;
		}
		
		if(args[0].equals("setitem")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				Inventory i = p.getInventory();
				ItemStack[] items =i.getContents();
				List<ItemStack> box = new ArrayList<ItemStack>();
				for(int f=0;f<items.length;f++){
					if(items[f]!=null){
						box.add(items[f]);
					}
				}
				Main.instance.getConfig().set("items", box);
				Main.instance.saveConfig();
				sender.sendMessage("设置完成。");
			}else{
				sender.sendMessage("只有玩家可以执行这个指令。");
			}
			return true;
		}
		
		//repaired
		if(args[0].equals("setsymbol")){
			if(sender instanceof Player){
				if(args.length<2){
					sender.sendMessage("参数不足，请输入/Signer查看帮助");
				}else{
					Player p = (Player) sender;
					ItemStack item = p.getItemInHand();
					FileConfiguration config = Main.instance.getConfig();
					item.setAmount(1);
					if(args[1].equals("unfinished")){
						config.set("symbol.unfinished.mate",item);
					}else
					if(args[1].equals("finished")){
						config.set("symbol.finished.mate",item);
					}else
					if(args[1].equals("comming")){
						config.set("symbol.comming.mate",item);
					}else{
						sender.sendMessage("您输入的参数不正确，请输入/Signer查看帮助");
						return true;
					}
					Main.instance.saveConfig();
					sender.sendMessage("设置成功");
				}
				return true;
			}else{
				sender.sendMessage("只有玩家可以执行这个指令！");
				return true;
			}
		}
		
		if(args[0].equals("test")){
			
			}
		
		sender.sendMessage("您输入的指令不存在！请输入/Signer 查看。");
		return true;
	}
}
