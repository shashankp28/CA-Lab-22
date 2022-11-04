package processor.pipeline;

import processor.Processor;
import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	
	public Execute(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	private void loopAround(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}

	public void performEX()
	{
		if(EX_MA_Latch.is_busy == true) OF_EX_Latch.is_busy = true;
		else OF_EX_Latch.is_busy = false;
		loopAround(30);		
		if(OF_EX_Latch.isEX_enable() && EX_MA_Latch.is_busy == false) {
			int offset = 70000;
			if(OF_EX_Latch.is_nop == true)
			{
				EX_MA_Latch.is_nop = true;
				EX_MA_Latch.rd = 75000;
			}
			else 
			{
				EX_MA_Latch.is_nop = false;
				int alu_result = 70000;
				int rs1 = OF_EX_Latch.rs1;
				int rs2 = OF_EX_Latch.rs2;
				int rd = OF_EX_Latch.rd;
				int imm = OF_EX_Latch.imm;
				switch(OF_EX_Latch.opcode) {
					// Check for each opcode
					case "00000": {
						alu_result = rs1 + rs2;
						break;
					}
					case "00001": {
						alu_result = rs1 + imm;
						break;
					}
					case "00010": {
						alu_result = rs1 - rs2;
						break;
					}
					case "00011": {
						alu_result = rs1 - imm;
						break;
					}
					case "00100": {
						alu_result = rs1 * rs2;
						break;
					}
					case "00101": {
						alu_result = rs1 * imm;
						break;
					}
					case "00110": {
						alu_result = rs1 / rs2;
						int temp = rs1 % rs2;
						containingProcessor.getRegisterFile().setValue(31, temp);
						break;
					}
					case "00111": {
						alu_result = rs1 / imm;
						int temp = rs1 % imm;
						containingProcessor.getRegisterFile().setValue(31, temp);
						break;
					}

					case "01000": {
						alu_result = rs1 & rs2;
						break;
					}
					case "01001": {
						alu_result = rs1 & imm;
						break;
					}
					case "01010": {
						alu_result = rs1 | rs2;
						break;
					}
					case "01011": {
						alu_result = rs1 | imm;
						break;
					}
					case "01100": {
						alu_result = rs1 ^ rs2;
						break;
					}
					case "01101": {
						alu_result = rs1 ^ imm;
						break;
					}

					case "01110": {
						if(rs1 < rs2) alu_result = 1;
						else alu_result = 0;
						break;
					}
					case "01111": {
						if(rs1 < imm) alu_result = 1;
						else alu_result = 0;
					}

					case "10000": {
						alu_result = rs1 << rs2;
						String q = Integer.toBinaryString(rs1);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(5-rs2, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10001" : {
						alu_result = rs1 << imm;
						String q = Integer.toBinaryString(imm);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(5-imm, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10010" : {
						alu_result = rs1 >>> rs2;
						String q = Integer.toBinaryString(rs1);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10011" : {
						alu_result = rs1 >>> imm;
						String q = Integer.toBinaryString(imm);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10100" : {
						alu_result = rs1 >> rs2;
						String q = Integer.toBinaryString(rs1);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10101" : {
						alu_result = rs1 >> imm;
						String q = Integer.toBinaryString(imm);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}

					case "10110"  : {
						alu_result = rs1 + imm;
						break;
					}
					case "10111" : {
						alu_result = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}

					case "11000" : {
						offset = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}
					case "11001" : {
						if(rs1 == containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11010" : {
						if(rs1 != containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11011" : {
						if(rs1 < containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11100" : {
						if(rs1 > containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					default : break;
				}
				if(offset != 70000) {
					EX_IF_Latch.branch_taken = true;
					EX_IF_Latch.offset = offset - 1;
					IF_EnableLatch.setIF_enable(true);
					OF_EX_Latch.setEX_enable(false);
					IF_OF_Latch.setOF_enable(false);
					OF_EX_Latch.imm = 0;
					OF_EX_Latch.rd = 0;
					OF_EX_Latch.rs1 = 0;
					OF_EX_Latch.rs2 = 0;
				}
				EX_MA_Latch.alu_result = alu_result;
				EX_MA_Latch.rs1 = rs1;
				EX_MA_Latch.rs2 = rs2;
				EX_MA_Latch.rd = rd;
				EX_MA_Latch.imm = imm;
				EX_MA_Latch.opcode = OF_EX_Latch.opcode;
			EX_MA_Latch.pc_instruction = OF_EX_Latch.pc_instruction;

				if(OF_EX_Latch.opcode.equals("11101") == true ) {
					OF_EX_Latch.setEX_enable(false);
				}
			}
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			
	}}}
