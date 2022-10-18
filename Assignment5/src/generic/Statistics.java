package generic;

import java.io.PrintWriter;
public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfOFStalls;
	static int numberOfBranchTaken;
	static int numberOfRegisterWriteInstructions;
	static float throughput = 0;
	
	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Data Hazard OF Stalls = " + numberOfOFStalls);
			writer.println("Number of Branch Taken = " + numberOfBranchTaken);
			writer.println("Number of Register Write = " + numberOfRegisterWriteInstructions);
			writer.println("Throughput = " + throughput/numberOfCycles);
			
			// TODO add code here to print statistics in the output file

			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}

	public static float getThroughput() {
		return throughput;
	}

	public static void setThroughput(float tp) {
		throughput = tp;
	}

	public static int getNumberOfInstructions() {
		return numberOfInstructions;
	}
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}
	public static int getNumberOfCycles() {
		return numberOfCycles;
	}
	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}
	public static int getNumberOfOFStalls() {
		return numberOfOFStalls;
	}
	public static void setNumberOfOFStalls(int numberOfOFStalls) {
		Statistics.numberOfOFStalls = numberOfOFStalls;
	}
	public static int getNumberOfBranchTaken() {
		return numberOfBranchTaken;
	}
	public static void setNumberOfBranchTaken(int numberOfBranchTaken) {
		Statistics.numberOfBranchTaken = numberOfBranchTaken;
	}
	public static int getNumberOfRegisterWriteInstructions() {
		return numberOfRegisterWriteInstructions;
	}
	public static void setNumberOfRegisterWriteInstructions(int numberOfRegisterWriteInstructions) {
		Statistics.numberOfRegisterWriteInstructions = numberOfRegisterWriteInstructions;
	}
}
