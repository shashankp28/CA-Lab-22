package processor.memorysystem;
import generic.*;
import generic.Event.EventType;
import processor.*;
import processor.pipeline.*;


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
	public void handleEvent ( Event e ) {
		if ( e.getEventType ( ) == EventType.MemoryRead )
		{
			MemoryReadEvent event = ( MemoryReadEvent ) e ;
			Simulator. getEventQueue().addEvent(
			new MemoryResponseEvent (
			Clock.getCurrentTime () ,
			this ,
			event.getRequestingElement ( ) ,
			getWord (event.getAddressToReadFrom()))) ;
		}
		else if(e.getEventType ( ) == EventType.MemoryWrite) {
			MemoryWriteEvent event = ( MemoryWriteEvent ) e ; 
			((MemoryAccess) event.getRequestingElement()).EX_MA_Latch.setMA_Busy(false);
			((MemoryAccess) event.getRequestingElement()).MA_RW_Latch.setRW_enable(true);

			setWord(event.getAddressToWriteTo(), event.getValue());
			
		}
	}
}
