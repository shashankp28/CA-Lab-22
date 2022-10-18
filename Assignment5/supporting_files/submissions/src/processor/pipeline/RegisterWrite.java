package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import generic.Statistics;
import generic.Instruction.OperationType;
import generic.Operand;
import processor.Processor;
import generic.Statistics;

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
		if (MA_RW_Latch.getNop()) {
			MA_RW_Latch.setNop(false);
			MA_RW_Latch.setRW_enable(false);
			System.out.println("RW - MA_RW_Nop: False");
//			IF_EnableLatch.setIF_enable(true);
		} else if (MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			Instruction inst = MA_RW_Latch.getInstruction();
			String op_name = inst.getOperationType().name();
			int operation_number = OperationType.valueOf(op_name).ordinal();

			if(operation_number<=21)
			{
				int result = MA_RW_Latch.getALU_result();
				containingProcessor.getRegisterFile().setValue(inst.getDestinationOperand().getValue(), result);
				Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
			}

			if(operation_number==22)
			{
				int result = MA_RW_Latch.getLoad_result();
				containingProcessor.getRegisterFile().setValue(inst.getDestinationOperand().getValue(), result);
				Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions()+1);
			}
			
			if(operation_number==29)
			{
				Simulator.setSimulationComplete(true);
				System.out.println("RW - Simulation COmplete: True");
			}

			// if (operation_number != 29) {
			// 	IF_EnableLatch.setIF_enable(true);
			// 	System.out.println("RW - IF_Enable: True");

			// }
			System.out.println("Register Write Completed");
			Statistics.setThroughput(Statistics.getThroughput()+1);
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			MA_RW_Latch.setRW_enable(false);
		}
	}

}
