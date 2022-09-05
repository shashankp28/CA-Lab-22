package generic;

import java.nio.ByteBuffer;
import java.io.*;
import generic.Operand.OperandType;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
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
		for(var data: ParsedProgram.data)
		{	
			byte[] read = ByteBuffer.allocate(4).putInt(data).array();
			out_file.write(read);

		}

		//4. assemble one instruction at a time, and write to the file
		//5. close the file
		out_file.close();
	}
	
	public static void main(String[] args){


	}
}
