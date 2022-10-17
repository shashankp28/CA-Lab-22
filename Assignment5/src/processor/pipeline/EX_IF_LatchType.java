package processor.pipeline;

public class EX_IF_LatchType {

	boolean IS_enable;
	int PC;
	boolean isBusy;
	int offset;
	boolean isBranchTaken;
	
	public EX_IF_LatchType() {
		IS_enable = false;
	}

	public boolean getBranchTaken() {
		return isBranchTaken;
	}

	public void setOffset(int new_offset){
		this.offset = new_offset;
	}

	public EX_IF_LatchType(boolean is_enable) {
		IS_enable = is_enable;
	}

	public void setBusy(boolean busy){
		isBusy = busy;
	}

	public boolean getBusy(){
		return isBusy;
	}

	public EX_IF_LatchType(boolean is_enable, int pc) {
		IS_enable = is_enable;
		PC = pc;
	}

	public boolean getIS_enable() {
		return IS_enable;
	}

	public void setIS_enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC; 
	}

	public void setIS_enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	public void setPC(int newPC) {
		PC = newPC;
	}

	public int getPC() {
		return PC;
	}

}
