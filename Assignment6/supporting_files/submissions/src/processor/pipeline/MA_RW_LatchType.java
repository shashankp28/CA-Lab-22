package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int alu_result;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String opcode;
	int pc_instruction;
	boolean isLoad;
	boolean is_nop;
	int load_result;
	boolean NOP;
	
	public MA_RW_LatchType() {
		RW_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		alu_result = 70000;
		pc_instruction = -1;
		isLoad =  false;
		is_nop = false;
		rs1addr = 45;
		rs2addr = 45;
	}

	public String toString() {
		return "MA_RW_LatchType";
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setLoad_result(int result) {
		load_result = result;
	}

	public int getLoad_result() {
		return load_result;
	}

	public int getALU_result() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}
	
	public boolean getis_nop() {
		return NOP;
	}
	
	public void setis_nop(boolean is_NOP) {
		NOP = is_NOP;
	}
}
