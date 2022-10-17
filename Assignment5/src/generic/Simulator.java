package generic;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;
import generic.Statistics;
import generic.EventQueue;

public class Simulator {
			
	static Processor processor;
	static EventQueue eventQueue;
	static boolean sc;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) {

		eventQueue = new EventQueue();

		Simulator.processor = p;
		try {

			loadProgram(assemblyProgramFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		sc = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws IOException {
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
		InputStream input_file = null;
		try {

			input_file = new FileInputStream(assemblyProgramFile);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		DataInputStream d_input_file = new DataInputStream(input_file);

		int address = -1;
		int add_offset = 1;
		while(d_input_file.available() > 0) 
		{

			int next = d_input_file.readInt();
			if(address == -1)
			{
				processor.getRegisterFile().setProgramCounter(next);
			}
			else
			{
				processor.getMainMemory().setWord(address, next);
			}
			address += add_offset;
		}
		
		int CONST = 65535;
		
        processor.getRegisterFile().setValue(0, 0);
        processor.getRegisterFile().setValue(1, CONST);
        processor.getRegisterFile().setValue(2, CONST);
	}

	public static EventQueue getEventQueue(){
		return eventQueue;
	}

	public static void simulate() {

		while(sc == false) {
			System.out.println("Current PC: ");
			System.out.println(processor.getRegisterFile().getProgramCounter());
			processor.getRWUnit().performRW();
			System.out.println("Register Write Completed");
			processor.getMAUnit().performMA();
			System.out.println("Memory Access Completed");
			processor.getEXUnit().performEX();
			System.out.println("Execute Completed");
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			System.out.println("Operand Fetch Completed");
			processor.getIFUnit().performIF();
			System.out.println("Instruction Fetch Completed");
			Clock.incrementClock();

			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
	}
	
	public static void setSimulationComplete(boolean value) {

		sc = value;
	}
}
