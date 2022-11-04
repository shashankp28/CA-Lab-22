package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	int pc_instruction;
	boolean is_busy;
	
	public IF_OF_LatchType() {
		OF_enable = false;
		pc_instruction = -1;
		instruction = -1999;
		is_busy = false;
	}
	public IF_OF_LatchType(boolean oF_enable, boolean is_busy) {
		OF_enable = oF_enable;
		pc_instruction = -1;
		instruction = -1999;
		this.is_busy = is_busy;
	}
	public boolean isOF_enable() {
		return OF_enable;
	}
	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}
	public boolean checkInstruction(int instruction) {
		return this.instruction == instruction;
	}
	public boolean checkPC(int pc) {
		return pc_instruction == pc;
	}
	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
}
