package generic;

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
		FileOutputStream fos = new FileOutputStream(new File(objectProgramFile));
		fos.write("Hey, there!".getBytes());
		fos.write("\n".getBytes());
		fos.write("How are you doing?".getBytes());
		fos.close();
		//2. write the firstCodeAddress to the  file
		
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file
	}
	
}
