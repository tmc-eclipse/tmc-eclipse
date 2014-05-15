package tmc.handlers;
import fi.helsinki.cs.plugin.tmc.Core;


public class PluginGetter {
	
	private static Core plugin;
	
	public PluginGetter(){
		
	}
	
	public static Core getPlugin(){
		if(plugin == null){
			plugin = new Core();
			return plugin;
		}
		else{
			return plugin;
		}
	}
	
	
}
