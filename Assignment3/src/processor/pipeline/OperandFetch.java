package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Operand;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
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
			

			curr_instruction.setOperationType(curr_instruction.OperationType.values()[opcode_number]);

			Operand rs1, rs2, rd;

			if(opcode_number%2==0 && opcode_number<=20)
			{
				rs1.setOperandType(OperandType.Register);
				String rs1_bin = binary_instruction.substring(5, 10);
				rs1.setValue(Integer.parseInt(rs1_bin,2));

				rs2.setOperandType(OperandType.Register);
				String rs2_bin = binary_instruction.substring(10, 15);
				rs2.setValue(Integer.parseInt(rs2_bin,2));

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

				rd.setOperandType(OperandType.Immediate);
				String rd_bin = binary_instruction.substring(15);
				rd.setValue(toSignedInteger(rd_bin));
			}

			if(opcode_number==25)
			{
				String rd_bin = binary_instruction.substring(5, 10);
				String imm_bin = binary_instruction.substring(10);

				int rd_val = Integer.parseInt(rd_bin,2);
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
