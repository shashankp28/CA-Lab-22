package processor.pipeline;
import java.util.Arrays;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import generic.Instruction;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	/**
	 * converts binary representation of number to signed integer
	 * @param binary: Sring representation of binary form of number
	 * @return: returns signed representation of given number
	*/
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

	public static char flip(char c) {
		
        return (c == '0') ? '1' : '0';
	}

	public static String twosComplement(String bin) {
		
        String twos = "", ones = "";
        for (int i = 0; i < bin.length(); i++) 
            ones += flip(bin.charAt(i));

        StringBuilder builder = new StringBuilder(ones);
        boolean addExtra = false;
		
        for (int i = ones.length() - 1; i > 0; i--) {
		
            if (ones.charAt(i) == '1') 
                builder.setCharAt(i, '0');
            else {
		
                builder.setCharAt(i, '1');
                addExtra = true;
                break;
            }
        }
		
        if (addExtra == false) 
            builder.append("1", 0, 7);
		
        twos = builder.toString();
        return twos;
    }

	
	public void performEX()
	{
		//TODO
		if(OF_EX_Latch.isEX_enable())
		{
			// Get Instruction from the latch
			Instruction inst = OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstruction(inst);
			OperationType ot = inst.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(ot);
			int PC_curr = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			// int signedInt = toSignedInteger("001");
			// String binary_int = toBinaryOfSpecificPrecision(signedInt, 5);

			int alu_result = 0;

			loopAround(30);

			if(opcode % 2 == 0 && opcode < 21 && opcode >= 0) {

				int operand_1 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand1().getValue());
				int operand_2 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand2().getValue());

				switch(ot) {

					case add:
						alu_result = (operand_1 + operand_2);
						break;
					case mul:
						alu_result = (operand_1 * operand_2);
						break;
					case sub:
						alu_result = (operand_1 - operand_2);
						break;
					case load:
						break;
					case and:
						alu_result = (operand_1 & operand_2);
						break;
					case div:
						alu_result = (operand_1 / operand_2);
						int remainder = (operand_1 % operand_2);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case xor:
						alu_result = (operand_1 ^ operand_2);
						break;
					case or:
						alu_result = (operand_1 | operand_2);
						break;
					case store:
						break;						
					case slt:
						if(operand_1 < operand_2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case srli:
						break;
					case srl:
						alu_result = (operand_1 >>> operand_2);
						break;
					case sll:
						alu_result = (operand_1 << operand_2);
						break;
					case sra:
						alu_result = (operand_1 >> operand_2);
						break;
					case end:
						break;
					default:
						break;
				}
			}

			else if(opcode < 23) {

				int imm = inst.getSourceOperand1().getValue();
				int operand_1 = containingProcessor.getRegisterFile().getValue(imm);
				int operand_2 = inst.getSourceOperand2().getValue();

				switch(ot) {

					case addi:
						alu_result = (operand_1 + operand_2);
						break;
					case muli:
						alu_result = (operand_1 * operand_2);
						break;
					case beq:
						break;
					case subi:
						alu_result = (operand_1 - operand_2);
						break;
					case andi:
						alu_result = (operand_1 & operand_2);
						break;
					case end:
						break;
					case xori:
						alu_result = (operand_1 ^ operand_2);
						break;
					case ori:
						alu_result = (operand_1 | operand_2);
						break;
					case divi:
						alu_result = (operand_1 / operand_2);
						int remainder = (operand_1 % operand_2);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case jmp:
						break;
					case srli:
						alu_result = (operand_1 >>> operand_2);
						break;
					case slti:
						if(operand_1 < operand_2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case slli:
						alu_result = (operand_1 << operand_2);
						break;
					case load:
						alu_result = (operand_1 + operand_2);
						break;
					case srai:
						alu_result = (operand_1 >> operand_2);
						break;
					default:
						break;
				}
			}

			else if(opcode == 23) {

				int operand_1 = containingProcessor.getRegisterFile().getValue(inst.getDestinationOperand().getValue());
				int operand_2 = inst.getSourceOperand2().getValue();
				alu_result = operand_1 + operand_2;
			}

			else if(opcode == 24) {

				OperandType optype = inst.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register) 
					imm = containingProcessor.getRegisterFile().getValue(
						inst.getDestinationOperand().getValue());
				else 
					imm = inst.getDestinationOperand().getValue();

				alu_result = imm + PC_curr;
				EX_IF_Latch.setIS_enable(true, alu_result);
			}
			else if(opcode < 29) {

				int imm = inst.getDestinationOperand().getValue();
				int operand_1 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand1().getValue());
				int operand_2 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand2().getValue());
				System.out.println(operand_1);
				System.out.println(operand_2);
				System.out.println(inst);
				switch(ot) {

					case beq:
						if(operand_1 == operand_2) {

							alu_result = imm + PC_curr;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					case bne:
						if(operand_1 != operand_2) {

							alu_result = imm + PC_curr;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}

						break;
					case blt:
						if(operand_1 < operand_2) {

							alu_result = imm + PC_curr;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					case bgt:
						if(operand_1 > operand_2) {

							alu_result = imm + PC_curr;
							EX_IF_Latch.setIS_enable(true, alu_result);
						}
						break;
					default:
						break;
				}
			}
			EX_MA_Latch.setALU_result(alu_result);
		}

		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
	}

}
