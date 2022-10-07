package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Operand;
import generic.Instruction.*;
import generic.Operand.OperandType;

public class OperandFetch {

	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public static String toBinary(int x, int len)
    {
		String bin = String.format("%" + len + "s",
							Integer.toBinaryString(x)).replaceAll(" ", "0");
		if(x<0)
		{
			bin = bin.substring(32-len, 32);
		}
		return bin;
    }
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	public static boolean conflict(Instruction inst, int r1, int r2) {
		int ist_no = inst != null && inst.getOperationType() != null ? inst.getOperationType().ordinal() : 1000;
		if ((ist_no <= 21 && ist_no % 2 == 0) || (ist_no <= 21 && ist_no % 2 != 0) || ist_no == 22 || ist_no == 23) {
			int rd = inst != null ? inst.getDestinationOperand().getValue() : -1;
			if (r1 == rd || r2 == rd) {
				return true;
			} else {
				return false;
			}
		} else return false;
	}

	public boolean div_conflict(int r1, int r2) {
		Instruction inst_ex = OF_EX_Latch.getInstruction();
		Instruction inst_ma = EX_MA_Latch.getInstruction();
		Instruction inst_rw = MA_RW_Latch.getInstruction();
		if (r1 == 31 || r2 == 31) {
			int inst_ex_ordinal = inst_ex != null && inst_ex.getOperationType() != null ? inst_ex.getOperationType().ordinal() : 1000;
			int inst_ma_ordinal = inst_ma != null && inst_ma.getOperationType() != null ? inst_ma.getOperationType().ordinal() : 1000;
			int inst_rw_ordinal = inst_rw != null && inst_rw.getOperationType() != null ? inst_rw.getOperationType().ordinal() : 1000;
			if (inst_ex_ordinal == 6 || inst_ex_ordinal == 7 || inst_ma_ordinal == 6 || inst_ma_ordinal == 7 || inst_rw_ordinal == 6 || inst_rw_ordinal == 7) {
				System.out.println("Conflict is observed in division");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void bubble_modify () {
		System.out.println("Conflict is observed");
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setNop(true);
	}

	public void performOF()
	{
		Instruction curr_instruction = new Instruction();
		//TODO
		if(IF_OF_Latch.isOF_enable())
		{
			int signed_instruction = IF_OF_Latch.getInstruction();
			String binary_instruction = toBinary(signed_instruction, 32);
			String bin_op_code = binary_instruction.substring(0, 5);
			int opcode_number = Integer.parseInt(bin_op_code,2);

			boolean instruction_conflict = false;
			Instruction ex_inst = OF_EX_Latch.getInstruction();
			Instruction ma_inst = EX_MA_Latch.getInstruction();
			Instruction rw_inst = MA_RW_Latch.getInstruction();
			

			curr_instruction.setOperationType(OperationType.values()[opcode_number]);

			Operand rs1 = new Operand();
			Operand rs2 = new Operand();
			Operand rd = new Operand();

			if(opcode_number%2==0 && opcode_number<=20)
			{
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				rs2.setOperandType(OperandType.Register);
				String rs2_bin = binary_instruction.substring(10, 15);
				rs2.setValue(Integer.parseInt(rs2_bin,2));

				int registerNo = Integer.parseInt(rs1_bin,2);
				int registerNo2 = Integer.parseInt(rs2_bin,2);
				if (conflict(ex_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (conflict(ma_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (conflict(rw_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (div_conflict(registerNo, registerNo2)) {
					instruction_conflict = true;
				}
				if (instruction_conflict) {
					this.bubble_modify();
				}

				rd.setOperandType(OperandType.Register);
				String rd_bin = binary_instruction.substring(15, 20);
				rd.setValue(Integer.parseInt(rd_bin,2));
			}

			if((opcode_number%2!=0 && opcode_number<=23) || (opcode_number==22))
			{
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				rs2.setOperandType(OperandType.Immediate);
				String rs2_bin = binary_instruction.substring(15);
				rs2.setValue(toSignedInteger(rs2_bin));

				int registerNo = Integer.parseInt(rs1_bin,2);
				int registerNo2 = Integer.parseInt(rs2_bin,2);
				if (conflict(ex_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (conflict(ma_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (conflict(rw_inst, registerNo, registerNo2))
					instruction_conflict = true;
				if (div_conflict(registerNo, registerNo2)) {
					instruction_conflict = true;
				}
				if (instruction_conflict) {
					this.bubble_modify();
				}

				rd.setOperandType(OperandType.Register);
				String rd_bin = binary_instruction.substring(10, 15);
				rd.setValue(Integer.parseInt(rd_bin,2));
			}

			if(opcode_number<=28 && opcode_number>=25)
			{
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));
				rs2.setOperandType(OperandType.Register);
				String rs2_bin = binary_instruction.substring(10, 15);
				rs2.setValue(Integer.parseInt(rs2_bin,2));

				int registerNo = Integer.parseInt(rs1_bin,2);
				int registerNo2 = Integer.parseInt(rs2_bin,2);
				if (conflict(ex_inst, registerNo, registerNo)) {
					instruction_conflict = true;
				}	
				if (conflict(ma_inst, registerNo, registerNo)) {
					instruction_conflict = true;
				}
				if (conflict(rw_inst, registerNo, registerNo)) {
					instruction_conflict = true;
				}
				if (div_conflict(registerNo, registerNo)) {
					instruction_conflict = true;
				}
					
				if (instruction_conflict) {
					this.bubble_modify();
				}

				rd.setOperandType(OperandType.Immediate);
				String rd_bin = binary_instruction.substring(15);
				rd.setValue(toSignedInteger(rd_bin));
			}

			if(opcode_number==24)
			{
				String rd_bin = binary_instruction.substring(5, 10);
				String imm_bin = binary_instruction.substring(10);

				int imm_val = toSignedInteger(imm_bin);

				if(imm_val==0)
				{
					rd.setOperandType(OperandType.Register);
					rd.setValue(Integer.parseInt(rd_bin,2));
				}
				else
				{
					rd.setOperandType(OperandType.Immediate);
					rd.setValue(toSignedInteger(imm_bin));
				}
			}

			curr_instruction.setSourceOperand1(rs1);
			curr_instruction.setSourceOperand2(rs2);
			curr_instruction.setDestinationOperand(rd);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
			OF_EX_Latch.setInstruction(curr_instruction);
		}
	}

}
