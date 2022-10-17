package processor.pipeline;
import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;
import generic.*;
import generic.Event.EventType;


public class MemoryAccess implements Element {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
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

		if(EX_MA_Latch.isMA_enable() && EX_MA_Latch.getBusy() == false) {
		if (EX_MA_Latch.getNop()) {
			MA_RW_Latch.setNop(true);
			System.out.println("MA - MA_RW_Nop: True");
			MA_RW_Latch.setInstruction(null);
			System.out.println("MA - MA_RW_Inst: Null");
			EX_MA_Latch.setNop(false);
			System.out.println("MA - EX_MA_Nop: False");
		} else if (EX_MA_Latch.isMA_enable())
		{
	
			Instruction inst = EX_MA_Latch.getInstruction();
			OperationType ot = inst.getOperationType();
			int alu_result = EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(alu_result);

			if (ot == OperationType.load) {

				int lr = containingProcessor.getMainMemory().getWord(alu_result);
				MA_RW_Latch.setLoad_result(lr);
			}
			else if (ot == OperationType.store) {

				int str = containingProcessor.getRegisterFile().getValue(inst.getSourceOperand1().getValue());
				containingProcessor.getMainMemory().setWord(alu_result, str);
			}
			if (inst.getOperationType().ordinal() == 29) 
			{
				IF_EnableLatch.setIF_enable(false);
				System.out.println("MA - IF_Enable: False (end)");
			} 
	
			MA_RW_Latch.setInstruction(inst);
			MA_RW_Latch.setRW_enable(true);
			System.out.println("MA - RW_Enable: True");
		}
	}

}

@Override
	public void handleEvent(Event e) {
		if(e.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) e ; 
			MA_RW_Latch.alu_result = event.getValue();
			MA_RW_Latch.pc_inst = EX_MA_Latch.pc_inst;
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.isBusy = false;
		}
		else {
			EX_MA_Latch.isBusy = false;
		}

	}
}
