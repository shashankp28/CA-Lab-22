package processor.pipeline;
import generic.Statistics;
import processor.Processor;
import generic.Simulator;
import processor.Clock;
import configuration.*;
import generic.*;

public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	int pc_current = 0;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable()) {
			if(IF_EnableLatch.getBusy() == true) return;

			pc_current = containingProcessor.getRegisterFile().getProgramCounter();
			if(EX_IF_Latch.isBranchTaken == true) {
				pc_current = pc_current + EX_IF_Latch.offset - 1;
				EX_IF_Latch.isBranchTaken = false;
			}
			
			// System.out.println("IF is enabled with instruction: " + Integer.toBinaryString(newInstruction) + "..");
			System.out.println("pcCurrent " + Integer.toString(pc_current));
			Simulator.instCount++;
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency, 
					(Element) this, 
					(Element) containingProcessor.getMainMemory(), 
					pc_current)
			);

			IF_EnableLatch.setBusy(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.getBusy() == true) {
			System.out.println("IF-OF is busy");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			MemoryResponseEvent response_event = (MemoryResponseEvent) e ; 
			System.out.println("Memory is responding");
			if(EX_IF_Latch.isBranchTaken == false)	{
				IF_OF_Latch.setInstruction(response_event.getValue());
			}
			else IF_OF_Latch.setInstruction(0);
			IF_OF_Latch.pc_inst = this.pc_current;
			containingProcessor.getRegisterFile().setProgramCounter(this.pc_current + 1);
			System.out.println("Event value : " + response_event.getValue());
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setBusy(false);

		}
	}
}
