package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean branch_taken;
	int offset;
	boolean IS_enable;
	int PC;
	
	public EX_IF_LatchType() {
		branch_taken = false;
		offset = 70000;
	}
	public boolean get_is_branch_taken() {
		return branch_taken;
	}

	public void setPc(int pc) {
		this.PC = pc;
	}
	
	public boolean getIS_enable() {
		return IS_enable;
	}

	public void setIS_enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	public void setIS_enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC;
	}

	public int getPC() {
		return PC;
	}

}
