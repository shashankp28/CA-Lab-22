package processor.pipeline;
import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.*;	
import processor.memorysystem.*;	

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	Cache my_cache;
	int pc_curr;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch, Cache cach)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.my_cache = cach;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable()) {
			if(IF_EnableLatch.is_busy == true) return;

			pc_curr = containingProcessor.getRegisterFile().getProgramCounter();
			if(EX_IF_Latch.branch_taken == true) {
				pc_curr = pc_curr + EX_IF_Latch.offset - 1;
				EX_IF_Latch.branch_taken = false;
			}
			Simulator.ins_count++;
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime()+this.my_cache.latency, 
					this, 
					this.my_cache, 
					pc_curr)
			);
			IF_EnableLatch.is_busy = true;
	}}

	@Override
	public void handleEvent(Event e) {
		if(IF_OF_Latch.is_busy == true) {
			System.out.println("IF-OF is busy");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			MemoryResponseEvent event = (MemoryResponseEvent) e ; 
			System.out.println("Memory is responding");
			if(EX_IF_Latch.branch_taken == false)	{
				IF_OF_Latch.setInstruction(event.getValue());
			}
			else IF_OF_Latch.setInstruction(0);
			IF_OF_Latch.pc_instruction = this.pc_curr;
			containingProcessor.getRegisterFile().setProgramCounter(this.pc_curr + 1);
		//	System.out.println("event value " + event.getValue());
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.is_busy = false;
	}}}
