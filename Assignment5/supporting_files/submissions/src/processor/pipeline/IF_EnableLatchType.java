package processor.pipeline;

public class IF_EnableLatchType {

	
	boolean IF_enable, IsBusy;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setIF_Busy(boolean set_busy){
		IsBusy = set_busy;
	}

	public boolean getIF_Busy(){
		return IsBusy;
	}

}
