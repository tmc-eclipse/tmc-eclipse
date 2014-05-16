package tmc.handlers;
import fi.helsinki.cs.plugin.tmc.Core;


public class CoreInitializer {
	
	private static Core core;
	
	public CoreInitializer(){
		
	}
	
	public static Core getCore(){
		if(core == null){
			core = new Core();
			return core;
		}
		else{
			return core;
		}
	}
}