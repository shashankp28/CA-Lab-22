package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class InstructionFetch {
	
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
			IF_OF_Latch.setOF_enable(true);
			System.out.println("IF - setOF_Enable: True");
			if(EX_IF_Latch.getIS_enable())
			{
				int branch_target = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(branch_target-1);
				Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
				System.out.println("IF - EX_IF_Enable: False");
				EX_IF_Latch.setIS_enable(false);
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
		}
	}

}
