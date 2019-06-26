package net.MayDayMemory.www.Signer;

import java.util.Calendar;

import org.bukkit.Material;


public class Calen implements java.io.Serializable{
	public Material[] sticky;
	public int lastm;
	public int lasty;
	public boolean state;  //true 闰年  false则不是
	private static final long serialVersionUID = -1458510272181181143L;
	
	public Calen(Calendar c){
		int year = c.get(Calendar.YEAR);
		if(year%4==0){
			if(year%100==0){
				if(year%400==0){
					state = true;
				}
				state = false;
			}
			state = true;
		}
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DATE);
		
		if(month == 2){
			if(state==true){
				sticky = new Material[28];
			}else{
				sticky = new Material[29];
			}
		}else if(month == 1){
			sticky = new Material[31];
		}else if(month<8){
			if(month%2==0){
				sticky = new Material[30];
			}else{
				sticky= new Material[31];
			}
		}else{
			if(month%2==0){
				sticky= new Material[31];
			}else{
				sticky = new Material[30];
			}
		}
		
		int f;
		for(f = 0;f<day;f++){
			sticky[f]=Material.ICE;
		}
		for(int f2=f;f2<sticky.length;f2++){
			sticky[f2]=Material.SNOW_BLOCK;
		}
		
		lastm = c.get(Calendar.MONTH);
		lasty = c.get(Calendar.YEAR);
	}
	
	public void update(){
		int date = Calendar.getInstance().get(Calendar.DATE);
		sticky[date-1] = Material.EMERALD_BLOCK;
		lastm = Calendar.getInstance().get(Calendar.MONTH);
		lasty = Calendar.getInstance().get(Calendar.YEAR);
	}
}
