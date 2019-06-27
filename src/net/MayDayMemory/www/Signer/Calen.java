package net.MayDayMemory.www.Signer;

import java.util.Calendar;


public class Calen implements java.io.Serializable{
	public String[] state;
	public int lastm;
	public int lasty;
	public boolean fixstate;  //true 是闰年 
	private static final long serialVersionUID = -1458510272181181143L;
	
	public Calen(Calendar c){
		int year = c.get(Calendar.YEAR);
		if(year%4==0){
			if(year%100==0){
				if(year%400==0){
					fixstate = true;
				}
				fixstate = false;
			}
			fixstate = true;
		}
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DATE);
		
		if(month == 2){
			if(fixstate==true){
				state = new String[28];
			}else{
				state = new String[29];
			}
		}else if(month == 1){
			state = new String[31];
		}else if(month<8){
			if(month%2==0){
				state = new String[30];
			}else{
				state= new String[31];
			}
		}else{
			if(month%2==0){
				state= new String[31];
			}else{
				state= new String[30];
			}
		}
		
		int f;
		for(f = 0;f<day;f++){
			state[f]="unfinished";
		}
		for(int f2=f;f2<state.length;f2++){
			state[f2]="comming";
		}
		lastm = c.get(Calendar.MONTH);
		lasty = c.get(Calendar.YEAR);
	}
	
	public void update(){
		int date = Calendar.getInstance().get(Calendar.DATE);
		state[date-1] ="finished";
		lastm = Calendar.getInstance().get(Calendar.MONTH);
		lasty = Calendar.getInstance().get(Calendar.YEAR);
	}
}
