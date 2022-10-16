package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Operand;
import generic.Statistics;
import generic.Statistics;
import generic.Instruction.*;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch; 
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
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

	public static boolean conflictCheck(Instruction instruction, int reg_1, int reg_2) {
		int inst_ordinal = instruction != null && instruction.getOperationType() != null ? instruction.getOperationType().ordinal() : 1000;
		if ((inst_ordinal <= 21 && inst_ordinal % 2 == 0) || (inst_ordinal <= 21 && inst_ordinal % 2 != 0) || inst_ordinal == 22 || inst_ordinal == 23) {
			int dest_reg = instruction != null ? instruction.getDestinationOperand().getValue() : -1;
			if (reg_1 == dest_reg || reg_2 == dest_reg) {
				return true;
			} else {
				return false;
			}
		} else return false;
	}
	
	public boolean divConflict(int reg_1, int reg_2) {
		Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
		Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
		Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
		if (reg_1 == 31 || reg_2 == 31) {
			int inst_ex_ordinal = instruction_ex_stage != null && instruction_ex_stage.getOperationType() != null ? instruction_ex_stage.getOperationType().ordinal() : 1000;
			int inst_ma_ordinal = instruction_ma_stage != null && instruction_ma_stage.getOperationType() != null ? instruction_ma_stage.getOperationType().ordinal() : 1000;
			int inst_rw_ordinal = instruction_rw_stage != null && instruction_rw_stage.getOperationType() != null ? instruction_rw_stage.getOperationType().ordinal() : 1000;
			if (inst_ex_ordinal == 6 || inst_ex_ordinal == 7 || inst_ma_ordinal == 6 || inst_ma_ordinal == 7 || inst_rw_ordinal == 6 || inst_rw_ordinal == 7) {
				System.out.println("Conflict in division");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void bubblePCModify () {
		System.out.println("Conflict Observed");
		IF_EnableLatch.setIF_enable(false);
		System.out.println("OF - setIF_Enable: False (Bubble)");
		OF_EX_Latch.setNop(true);
		System.out.println("OF - OF_EX_Nop: True (Bubble)");
		Statistics.setNumberOfOFStalls(Statistics.getNumberOfOFStalls()+1);
	}

	
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	public void performOF()
	{
		if(IF_OF_Latch.getNop()){
			IF_OF_Latch.setNop(false);
			System.out.println("OF - IF_OF_Nop: False");
			OF_EX_Latch.setNop(true);
			System.out.println("OF - OF_EX_Nop: True");
			OF_EX_Latch.setInstruction(null);
			System.out.println("OF - OF_EX_Inst: Null");
		}else if(IF_OF_Latch.isOF_enable()){
			Instruction curr_instruction = new Instruction();
			boolean is_conflict_present = false;
			int signed_instruction = IF_OF_Latch.getInstruction();
			String binary_instruction = toBinary(signed_instruction, 32);
			String bin_op_code = binary_instruction.substring(0, 5);
			int opcode_number = Integer.parseInt(bin_op_code,2);
			
			Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
			Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
			Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();

			// if (opcode_number == 24 || opcode_number == 25 || opcode_number == 26 || opcode_number == 27 || opcode_number == 28 ) {
			// 	IF_EnableLatch.setIF_enable(false);
			// 	System.out.println("OF - setIF_Enable: False");
			// }

			if(opcode_number%2==0 && opcode_number<=20)
			{
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				String rs2_bin = binary_instruction.substring(10, 15);
				rs2.setValue(Integer.parseInt(rs2_bin,2));


				if (conflictCheck(instruction_ex_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_ma_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_rw_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (divConflict(rs1.getValue(), rs2.getValue())) {
					is_conflict_present = true;
				}
				if (is_conflict_present) {
					this.bubblePCModify();
				}
				else{
					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					String rd_bin = binary_instruction.substring(15, 20);
					rd.setValue(Integer.parseInt(rd_bin,2));


					curr_instruction.setOperationType(OperationType.values()[opcode_number]);
					curr_instruction.setSourceOperand1(rs1);
					curr_instruction.setSourceOperand2(rs2);
					curr_instruction.setDestinationOperand(rd);
				}
			}

			if((opcode_number%2!=0 && opcode_number<=23) || (opcode_number==22))
			{
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				if (conflictCheck(instruction_ex_stage, rs1.getValue(), rs1.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_ma_stage, rs1.getValue(), rs1.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_rw_stage, rs1.getValue(), rs1.getValue()))
					is_conflict_present = true;
				if (divConflict(rs1.getValue(), rs1.getValue())) {
					is_conflict_present = true;
				}
				if (is_conflict_present) {
					this.bubblePCModify();
				}
				else{
					Operand rs2 = new Operand();
					rs2.setOperandType(OperandType.Immediate);
					String rs2_bin = binary_instruction.substring(15);
					rs2.setValue(toSignedInteger(rs2_bin));

					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					String rd_bin = binary_instruction.substring(10, 15);
					rd.setValue(Integer.parseInt(rd_bin,2));


					curr_instruction.setOperationType(OperationType.values()[opcode_number]);
					curr_instruction.setSourceOperand1(rs1);
					curr_instruction.setSourceOperand2(rs2);
					curr_instruction.setDestinationOperand(rd);
				}
			}

			if(opcode_number<=28 && opcode_number>=25)
			{
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				String rs2_bin = binary_instruction.substring(10, 15);
				rs2.setValue(Integer.parseInt(rs2_bin,2));

				if (conflictCheck(instruction_ex_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_ma_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (conflictCheck(instruction_rw_stage, rs1.getValue(), rs2.getValue()))
					is_conflict_present = true;
				if (divConflict(rs1.getValue(), rs2.getValue())) {
					is_conflict_present = true;
				}
				if (is_conflict_present) {
					this.bubblePCModify();
				}
				else{
					Operand rd = new Operand();
					rd.setOperandType(OperandType.Immediate);
					String rd_bin = binary_instruction.substring(15);
					rd.setValue(toSignedInteger(rd_bin));


					curr_instruction.setOperationType(OperationType.values()[opcode_number]);
					curr_instruction.setSourceOperand1(rs1);
					curr_instruction.setSourceOperand2(rs2);
					curr_instruction.setDestinationOperand(rd);
				}
			}

			if(opcode_number==24)
			{
				Operand rd = new Operand();
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
				curr_instruction.setOperationType(OperationType.values()[opcode_number]);
				curr_instruction.setDestinationOperand(rd);
			}

			if(opcode_number==29){
				curr_instruction.setOperationType(OperationType.values()[opcode_number]);
				IF_EnableLatch.setIF_enable(false);
				System.out.println("OF - setIF_Enable: False (end)");
			}

			if (is_conflict_present) {
				this.bubblePCModify();
			}
			OF_EX_Latch.setInstruction(curr_instruction);
			OF_EX_Latch.setEX_enable(true);
			System.out.println("OF - setEX_Enable: True");
		}
	}

}
