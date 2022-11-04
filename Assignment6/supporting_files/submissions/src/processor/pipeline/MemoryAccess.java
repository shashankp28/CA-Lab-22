// Import required packages

package processor.pipeline;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.*;
import processor.memorysystem.*;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	Cache my_cache;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, Cache cach)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.my_cache = cach;
	}
	
	public void performMA()
	{
		// if(EX_MA_Latch.MA_enable == false) MA_RW_Latch.RW_enable = false;
		if(EX_MA_Latch.isMA_enable() && EX_MA_Latch.is_busy == false) {
			if(EX_MA_Latch.is_nop == true) {
				MA_RW_Latch.is_nop = true;
				MA_RW_Latch.rd = 75000;
			}
			else {
				MA_RW_Latch.is_nop = false;
				int alu_result = EX_MA_Latch.alu_result;
				int rs1 = EX_MA_Latch.rs1;
				int rs2 = EX_MA_Latch.rs2;
				int rd = EX_MA_Latch.rd;
				int imm = EX_MA_Latch.imm;
				String opcode = EX_MA_Latch.opcode;

				MA_RW_Latch.pc_instruction = EX_MA_Latch.pc_instruction;
				MA_RW_Latch.alu_result = alu_result;
				MA_RW_Latch.rs1 = rs1;
				MA_RW_Latch.rs2 = rs2;
				MA_RW_Latch.rd = rd;
				MA_RW_Latch.imm = imm;
				MA_RW_Latch.opcode = opcode;
				if(opcode.equals("10110")) // load
				{
					MA_RW_Latch.isLoad = true;
					EX_MA_Latch.is_busy = true;
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime(), 
							this, 
							this.my_cache, alu_result)
					);
					EX_MA_Latch.setMA_enable(false);
					return;
				}
				if(opcode.equals("10111")) {  // store
					EX_MA_Latch.is_busy = true;
					Simulator.storeresp = Clock.getCurrentTime();
					Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
							Clock.getCurrentTime()+this.my_cache.latency, 
							this, 
							this.my_cache, 
							alu_result, 
							rs1)
					);
					EX_MA_Latch.setMA_enable(false);
					return;
				}
			}
			EX_MA_Latch.setMA_enable(false);
			if(EX_MA_Latch.opcode.equals("11101") == true ) {
				EX_MA_Latch.setMA_enable(false);
			}
			MA_RW_Latch.setRW_enable(true);
		}
	}

	@Override
	public void handleEvent(Event e) {
		if(e.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent mrevent = (MemoryResponseEvent) e ; 
			MA_RW_Latch.alu_result = mrevent.getValue();
			MA_RW_Latch.pc_instruction = EX_MA_Latch.pc_instruction;
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.is_busy = false;
		}
		else {
			EX_MA_Latch.is_busy = false;
	}}}
