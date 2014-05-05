package frontend.soundpanel;

public class SyncBoolean {

	private boolean b;
	
	public SyncBoolean(boolean b){
		this.b = b;
	}
	
	
	public synchronized void setValue(boolean b){
		this.b = b;
	}
	
	public synchronized boolean getValue(){
		return b;
	}
	
}
