package processor.pipeline;

import configuration.*;
import generic.*;
import processor.*;


public class InstructionFetch implements Element {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.getIF_Busy()) return;
			if(EX_IF_Latch.getIS_enable())
			{
				int branch_target = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(branch_target-1);
				Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
				System.out.println("IF - EX_IF_Enable: False");
				EX_IF_Latch.setIS_enable(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			
			Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
									this,containingProcessor.getMainMemory(),
									containingProcessor.getRegisterFile().getProgramCounter()));
									IF_EnableLatch.setIF_Busy(true);
									System.out.println("IF: setIF_Busy: True");
									
									// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
			System.out.println("Instruction Fetch Completed");
		}
	}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.getOF_Busy()) {
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			MemoryResponseEvent event=(MemoryResponseEvent) e;
			IF_OF_Latch.setInstruction(event.getValue());
			IF_OF_Latch.setOF_enable(true);
			System.out.println("IF: setOF_enable: True");
			IF_EnableLatch.setIF_Busy(false);
			System.out.println("IF: setIF_Busy: False");
		}
		
	}

}
