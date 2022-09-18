package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;;
import processor.Clock;
import processor.Processor;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;


	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */

		InputStream input = null;

		try {

			input = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		DataInputStream d = new DataInputStream(input);

		int address = 0;
		boolean isPCset = false;
		
		while(d.available() > 0) {

			int next = d.readInt();

			if(!isPCset)
			{
				processor.getRegisterFile().setProgramCounter(next);
				isPCset = true;
			}
			
			else
			{
				processor.getMainMemory().setWord(address, next);
				address+=1;
			}
		}

		int CONST = 65535;

		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, CONST);
		processor.getRegisterFile().setValue(2, CONST);

	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();

			// Set Statistics
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);

		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
