package processor.pipeline;

import generic.Instruction.*;
import generic.*;
import processor.*;
import configuration.*;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performMA()
	{
		if (EX_MA_Latch.isMA_enable())
		{
			if(EX_MA_Latch.getMA_Busy()) {
				return;
			}			
			
			if (EX_MA_Latch.getNop())
			{
				MA_RW_Latch.setNop(true);
				System.out.println("MA - MA_RW_Nop: True");
				MA_RW_Latch.setInstruction(null);
				System.out.println("MA - MA_RW_Inst: Null");
				EX_MA_Latch.setNop(false);
				System.out.println("MA - EX_MA_Nop: False");
				EX_MA_Latch.setMA_enable(false);
				System.out.println("MA: setMA_Busy: False");
				MA_RW_Latch.setRW_enable(true);
				System.out.println("MA: setRW_Enable: True");
				return;
			} 
	
			Instruction inst = EX_MA_Latch.getInstruction();
			OperationType ot = inst.getOperationType();
			int alu_result = EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(alu_result);

			if (ot == OperationType.load) {

				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,containingProcessor.getMainMemory(),
								alu_result));
				EX_MA_Latch.setMA_Busy(true);
				System.out.println("MA: setMA_Busy: True");

				// int lr = containingProcessor.getMainMemory().getWord(alu_result);
				// MA_RW_Latch.setLoad_result(lr);
			}
			else if (ot == OperationType.store) {

				int str = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand1().getValue());
				Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								alu_result, str));

				EX_MA_Latch.setMA_Busy(true);
				System.out.println("MA: setMA_Busy: True");

				// containingProcessor.getMainMemory().setWord(alu_result, str);
			}
			if (inst.getOperationType().ordinal() == 29) 
			{
				IF_EnableLatch.setIF_enable(false);
				System.out.println("MA - IF_Enable: False (end)");
			} 
	
			MA_RW_Latch.setInstruction(inst);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
			System.out.println("Memory Access Completed");
		}
	}

	@Override
	public void handleEvent(Event e) {
		MemoryResponseEvent event=(MemoryResponseEvent) e;
		MA_RW_Latch.setLoad_result(event.getValue());
		EX_MA_Latch.setMA_Busy(false);
		EX_MA_Latch.setMA_enable(false);
		System.out.println("MA: setMA_Busy: False");
		MA_RW_Latch.setRW_enable(true);
		System.out.println("MA: setRW_Enable: True");
		
		
	}

}
