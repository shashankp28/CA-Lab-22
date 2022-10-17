package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean Nop;
	boolean isBusy;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public void setBusy(boolean busy){
		isBusy = busy;
	}

	public boolean getBusy(){
		return isBusy;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean getNop(){
		return Nop;
	}

	public void setNop(boolean set_nop){
		Nop = set_nop;
	}

}
