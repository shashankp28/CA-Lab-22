package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	String opcode;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	int pc_instruction;
	boolean is_nop;
	boolean is_busy;
	boolean NOP;
	
	public OF_EX_LatchType() {
		EX_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		pc_instruction = -1;
		is_nop = false;
		rs1addr = 45;
		rs2addr = 45;
		is_busy = false;
	}

	public String toString() {
		return "OF_EX_LatchType";
	}
	public boolean isEX_enable() {
		return EX_enable;
	}
	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	public boolean getis_nop() {
		return NOP;
	}
	public void setis_nop(boolean is_NOP) {
		NOP = is_NOP;
	}
}
