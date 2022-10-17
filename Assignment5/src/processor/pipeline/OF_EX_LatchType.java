package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean nop;
	boolean isBusy;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public void setBusy(boolean busy){
		isBusy = busy;
	}

	public boolean getBusy(){
		return isBusy;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public Instruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(Instruction inst)
	{
		instruction = inst;
	}
	public boolean getNop(){
		return nop;
	}
	public void setNop(boolean nop_to_set){
		nop = nop_to_set;
	}

}
