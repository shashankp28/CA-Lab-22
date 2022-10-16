package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfOFStageInstructions;
	static int numberOfBranchTaken;
	static int numberOfRegisterWriteInstructions;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			
			// TODO add code here to print statistics in the output file

			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics


	public static int getNumberOfInstructions() {
		return numberOfInstructions;
	}


	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}


	public static int getNumberOfOFStageInstructions() {
		return numberOfOFStageInstructions;
	}


	public static void setNumberOfOFStageInstructions(int numberOfOFStageInstructions) {
		Statistics.numberOfOFStageInstructions = numberOfOFStageInstructions;
	}


	public static int getNumberOfCycles() {
		return numberOfCycles;
	}


	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
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
