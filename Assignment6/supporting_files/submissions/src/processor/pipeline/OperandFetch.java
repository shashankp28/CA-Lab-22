package processor.pipeline;

import processor.Processor;
import java.util.Arrays;

public class OperandFetch {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public static char flip(char c) {
		return (c == '0') ? '1' : '0';
	}

	public static String twosComplement(String bin) {
		String twos = "", ones = "";
		for (int i = 0; i < bin.length() && true; i++) {
			ones += flip(bin.charAt(i));
		}

		StringBuilder builder = new StringBuilder(ones);
		boolean b = false;
		for (int i = ones.length() - 1; i > 0 && i > -2; i--) {
			if (ones.charAt(i) == '1') {
				builder.setCharAt(i, '0');
			} else {
				builder.setCharAt(i, '1');
				b = true;
				break;
			}
		}
		if (!b) {
			builder.append("1", 0, 7);
		}
		twos = builder.toString();
		return twos;
	}

	// Perform OF
	public void performOF()
	{
		if(OF_EX_Latch.is_busy == true) IF_OF_Latch.is_busy = true;
		else IF_OF_Latch.is_busy = false;
		if(IF_OF_Latch.isOF_enable() && OF_EX_Latch.is_busy == false)
		{
			String insStr = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			System.out.println("insStr " + insStr);
			if(IF_OF_Latch.getInstruction() < 0) {
				while(insStr.length() < 32) insStr = "1" + insStr;
			}
			else {
				while(insStr.length() < 32) insStr = "0" + insStr;
			}
			int opcode,rs1,rs2,dest_reg,imm;
			int address_rs1,address_rs2;
			String op = insStr.substring(0, 5);
			opcode = Integer.parseInt(op,2);
			rs1 = 70000;
			rs2 = 70000;
			dest_reg = 70000;
			imm = 70000;
			address_rs1 = 45;
			address_rs2 = 45;
			if(opcode == 0) {
				address_rs1 = Integer.parseInt(insStr.substring(5, 10),2);
				address_rs2 = Integer.parseInt(insStr.substring(10, 15),2);
				rs1 = containingProcessor.getRegisterFile().getValue(address_rs1);
				rs2 = containingProcessor.getRegisterFile().getValue(address_rs2);
				dest_reg = Integer.parseInt(insStr.substring(15, 20),2);
				imm = 70000;
			}
			else if(0 < opcode && opcode < 22) {
				if(opcode % 2 == 0) {
					address_rs1 = Integer.parseInt(insStr.substring(5, 10),2);
					address_rs2 = Integer.parseInt(insStr.substring(10, 15),2);
					rs1 = containingProcessor.getRegisterFile().getValue(address_rs1);
					rs2 = containingProcessor.getRegisterFile().getValue(address_rs2);
					dest_reg = Integer.parseInt(insStr.substring(15, 20),2);
					imm = 70000;
				}
				else {
					address_rs1 = Integer.parseInt(insStr.substring(5, 10),2);
					rs1 = containingProcessor.getRegisterFile().getValue(address_rs1);
					rs2 = 70000;
					dest_reg = Integer.parseInt(insStr.substring(10, 15),2);
					imm = Integer.parseInt(insStr.substring(15, 32),2);
				}
			}
			else {
				if(opcode == 24) {
					rs1 = 70000;
					rs2 = 70000;
					dest_reg = Integer.parseInt(insStr.substring(5, 10),2);
					imm = Integer.parseInt(insStr.substring(10, 32),2);
					if(insStr.substring(10, 32).charAt(0) == '1') {
						imm = imm - 4194304;
					}
				}
				else if(opcode != 29) {
					address_rs1 = Integer.parseInt(insStr.substring(5, 10),2);
					rs1 = containingProcessor.getRegisterFile().getValue(address_rs1);
					rs2 = 70000;
					dest_reg = Integer.parseInt(insStr.substring(10, 15),2);
					imm = Integer.parseInt(insStr.substring(15, 32),2);
					if(insStr.substring(15, 32).charAt(0) == '1') {
						imm = imm - 131072;
					}
				}
				else {
					rs1 = 70000;
					rs2 = 70000;
					dest_reg = 70000;
					imm = 70000;
				}
			}
				OF_EX_Latch.is_nop = false;
				OF_EX_Latch.opcode = op;
				OF_EX_Latch.rs1 = rs1;
				OF_EX_Latch.rs2 = rs2;
				OF_EX_Latch.rd = dest_reg;
				OF_EX_Latch.imm = imm;
				OF_EX_Latch.pc_instruction = IF_OF_Latch.pc_instruction;
				OF_EX_Latch.setEX_enable(true);
				IF_EnableLatch.setIF_enable(true);
			// End
			if(opcode == 29) {
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setIF_enable(false);
			}	
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
	}}}


