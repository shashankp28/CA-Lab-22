package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			Instruction inst = MA_RW_Latch.getInstruction();
			String op_name = inst.OperationType.name();
			int operation_number = OperationType.valueOf(op_name).ordinal();

			if(operation_number<=21)
			{
				int result = MA_RW_Latch.getALU_result();
				containingProcessor.getRegisterFile().setValue(inst.getDestinationOperand().getValue(), result);
			}

			if(operation_number==22)
			{
				int result = MA_RW_Latch.getLoad_result();
				containingProcessor.getRegisterFile().setValue(inst.getDestinationOperand().getValue(), result);
			}
			
			if(operation_number==29)
			{
				Simulator.setupSimulationComplete(true);
			}

			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
