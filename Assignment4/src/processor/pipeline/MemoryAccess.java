package processor.pipeline;
import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if (EX_MA_Latch.getNop()) {
			MA_RW_Latch.setNop(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setNop(false);
		}
		else{
			if(EX_MA_Latch.isMA_enable()) {
	
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
		
				MA_RW_Latch.setInstruction(inst);
				EX_MA_Latch.setMA_enable(false);
				MA_RW_Latch.setRW_enable(true);
			}
		}
		
	}

}
