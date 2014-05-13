package fi.helsinki.cs.plugin.tmc;
import com.google.gson.*;


public class ProofOfConcept {
	
	public ProofOfConcept()	{
		 a = 5;
	}
	
	public String tervehdi() {
		Gson gson = new Gson();
		
		return gson.toJson(this);
	}
	
	private int a;
	
	
}
