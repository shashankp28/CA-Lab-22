package generic;

import java.nio.ByteBuffer;
import java.io.*;
import generic.Operand.OperandType;
import generic.Instruction.*;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                            Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
        return null;
    }

	public static void assemble(String objectProgramFile)
	{
		try
		{
			//TODO your assembler code
			//1. open the objectProgramFile in binary mode
			FileOutputStream binary_file = new FileOutputStream(objectProgramFile);
			BufferedOutputStream out_file = new BufferedOutputStream(binary_file);
			// fos.write("Hey, there!".getBytes());
			// fos.write("\n".getBytes());
			// fos.write("How are you doing?".getBytes());
			
			//2. write the firstCodeAddress to the  file
			byte[] fca = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
			out_file.write(fca);

			//3. write the data to the file
			for(int data: ParsedProgram.data)
			{	
				byte[] bytes_to_write = ByteBuffer.allocate(4).putInt(data).array();
				out_file.write(bytes_to_write);
			}
			//4. assemble one instruction at a time, and write to the file
			for(Instruction instruction: ParsedProgram.code)
			{	
				String bits = "";
				String op_name = instruction.operationType.name();
				int operation_number = OperationType.valueOf(op_name).ordinal();
				String operation_bin = Simulator.toBinary(operation_number, 5);
				bits += operation_bin;
				
				if(operation_number<=21 and operation_number%2==0)
				{
					String rs1 = Simulator.toBinary(instruction.sourceOperand1.value, 5);
					String rs2 = Simulator.toBinary(instruction.sourceOperand2.value, 5);
					String rd = Simulator.toBinary(instruction.destinationOperand.value, 5);
					bits += rs1 + rs2 + rd;
					String zero = "0";
					bits += zero.repeat(12);
				}

				else if((operation_number<=21 and operation_number%2!=0) or )
				{
					String rs1 = Simulator.toBinary(instruction.sourceOperand1.value, 5);
					String imm = Simulator.toBinary(instruction.sourceOperand2.value, 5);
					String rd = Simulator.toBinary(instruction.destinationOperand.value, 17);
					bits += rs1 + rd + imm;
				}


				int signed_int = (int) Long.parseLong(bits,2);
				byte[] read = ByteBuffer.allocate(4).putInt(signed_int).array();
				out_file.write(read);
			}


			//5. close the file
			out_file.close();
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong\n"+e);
		}
	}
}
