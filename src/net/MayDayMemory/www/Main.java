package net.MayDayMemory.www;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.MayDayMemory.www.Commands.Signer;
import net.MayDayMemory.www.Commands.SignerBoard;
import net.MayDayMemory.www.Events.EventManager;
import net.MayDayMemory.www.Signer.Calen;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	  public static Main instance;
	  public SignerCommands commandtable;
	  private FileConfiguration config;
	  private File configPath;
	  public Map<UUID,Calen> cmap = new HashMap<UUID,Calen>();
	  
	  public void onEnable()
	  {
		  instance = this;
		  reloadConfig();
		  SignerCommands.register("Signer", new Signer("Signer"));
		  SignerCommands.register("SignerBoard", new SignerBoard("SignerBoard"));
		  
		  try {
			File directory = new File("");
			String path = directory.getCanonicalPath();
			path +="\\Calens";
			File file =new File(path);
			if  (!file .exists()  && !file .isDirectory())  file .mkdir();
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
		  EventManager.reg();
	  }
	  
	  public void reloadConfig(){
		    configPath = new File(getDataFolder(), "config.yml");
		    if (!configPath.exists()) {
		      this.saveResource("config.yml", true);
		    }
		    config = YamlConfiguration.loadConfiguration(this.configPath);
	  }
	  
	  public void saveConfig()
	  {
	    try
	    {
	      config.save(configPath);
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	  }
	  
	  public FileConfiguration getConfig()
	  {
	    return this.config;
	  }
	  
	  public void cunser(Player p){
		UUID puuid = p.getUniqueId();
		String uuid = puuid.toString();
		File directory = new File("");
		try {
			String path = directory.getCanonicalPath();
			path +="\\Calens\\"+uuid+".ser";
			File f = new File(path);
			if(!f.exists()){
				cmap.put(puuid, new Calen(Calendar.getInstance()));
				cser(p);
			}
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Calen c= (Calen) in.readObject();
	        in.close();
	        fileIn.close();
	        cmap.put(puuid, c);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			getServer().getLogger().warning("读取玩家   "+p.getName()+"  的签到表失败！");
		}
	  }
	  
	  public void cser(Player p){
		  	try {
		  		File directory = new File("");
		  		String path = directory.getCanonicalPath();
		  		path +="\\Calens";
		  		File file =new File(path);
		  		if  (!file .exists()  && !file .isDirectory())  file .mkdir();
		  	} catch (IOException e) {
		  		e.printStackTrace();
		  	}
			UUID puuid = p.getUniqueId();
			String uuid = puuid.toString();
			File directory = new File("");
			try {
				String path = directory.getCanonicalPath();
				path +="\\Calens\\"+uuid+".ser";
				FileOutputStream fileOut =new FileOutputStream(path);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(cmap.get(puuid));
		        out.close();
		        fileOut.close();
			}catch(IOException e){
				e.printStackTrace();
				getServer().getLogger().warning("序列化玩家  "+p.getName()+"  的签到表失败！");
			}
	  }
}
