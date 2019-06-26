package net.MayDayMemory.www;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

public class SignerCommands {
	private static CommandMap map;
	
	static
	{
		try
		{
		    Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
		    field.setAccessible(true);
		    map = (CommandMap)field.get(Bukkit.getServer());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	 }
	
	public static void register(String str, Command command)
	{
	    map.register(str, command);
	}
	
	public static CommandMap getCMap()
	{
		return map;
	}
	
}
