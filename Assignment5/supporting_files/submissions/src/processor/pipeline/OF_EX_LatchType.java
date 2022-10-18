package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	boolean nop;
	boolean IsBusy;

	public void setEX_Busy(boolean set_busy){
		IsBusy = set_busy;
	}

	public boolean getEX_Busy(){
		return IsBusy;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
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
