package processor.pipeline;

// IF Enable Latch Type
public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean is_busy;
	
	public IF_EnableLatchType() {
		IF_enable = true;
		is_busy = false;
	}

	public boolean checkBusy() {
		return is_busy;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public IF_EnableLatchType(boolean iF_enable, boolean is_busy) {
		this.IF_enable = iF_enable;
		this.is_busy = is_busy;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

}
