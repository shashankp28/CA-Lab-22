package processor.memorysystem;
import generic.*;
import processor.*;

public class MainMemory implements Element {
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event e) {
		if (e.getEventType() == Event.EventType.MemoryRead) {
			System.out.println("Memory Read Event Happening");
			MemoryReadEvent readEvent = (MemoryReadEvent) e ; 

			System.out.println(getWord(readEvent.getAddressToReadFrom()));
			Simulator.getEventQueue().addEvent( 
				new MemoryResponseEvent( 
					Clock.getCurrentTime(), 
					this, 
					readEvent.getRequestingElement(), 
					getWord(readEvent.getAddressToReadFrom())
				)
			); 
		}
		else if(e.getEventType() == Event.EventType.MemoryWrite) {
			System.out.println("Memory Write Event Happening");
			MemoryWriteEvent writeEvent = (MemoryWriteEvent) e ; 
			System.out.println(getWord(writeEvent.getAddressToWriteTo()));

			this.setWord(writeEvent.getAddressToWriteTo(), writeEvent.getValue());
			Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime(), 
					this, 
					writeEvent.getRequestingElement())
			);
		}
	}
}
