package processor.pipeline;

public class EX_MA_LatchType {	
	boolean MA_enable;
	int alu_result;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String opcode;
	int pc_instruction;
	boolean is_nop;
	boolean is_busy;

	// Constructor
	public EX_MA_LatchType()
	{
		MA_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		alu_result = 70000;
		pc_instruction = -1;
		is_nop = false;
		rs1addr = 45;
		rs2addr = 45;
		is_busy = false;
	}
	public boolean isMA_enable() {
		return MA_enable;
	}
	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}
}
