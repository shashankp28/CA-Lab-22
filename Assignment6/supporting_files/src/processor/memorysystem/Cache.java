package processor.memorysystem;
import java.io.*; 
import java.util.*; 
import generic.*;
import processor.*;
import configuration.Configuration;

public class Cache implements Element{
	public int latency ;
	Processor containingProcessor;
	int cache_size, miss_addr, read, write_data, temp;
    boolean isPresent = true;
    CacheLine[] cacheline;
    int[] index;

	public Cache(Processor containingProcessor, int latency, int cacheSize) {
		this.containingProcessor = containingProcessor;
        this.latency = latency;
        this.cache_size = cacheSize;

        this.temp = (int)(Math.log(this.cache_size/8)/Math.log(2));
        this.cacheline = new CacheLine[cache_size/8];

		for(int i = 0; i < cache_size/8; i++) 
			this.cacheline[i] = new CacheLine();
    }

    public boolean check_is_present() {
        return this.isPresent;
    }

    public int[] get_indices() {
        return this.index;
    }

    public CacheLine[] get_caches() {
        return this.cacheline;
    }

    public Processor get_processor() {
        return this.containingProcessor;
    }

    public void set_processor(Processor processor) {
        this.containingProcessor = processor;
    }

    public String toString() {
        return Integer.toString(this.latency) + " : latency";
    }
    
    public void cache_miss_handler(int addr) {
		Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
						Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        addr));
                        
	}

    
    public int read_cache(int address){
        String a = Integer.toBinaryString(address);
        String ind = "";
        int index_temp;
       
        for(int i = 0; i < 32-a.length(); i++)
            a = "0" + a;
        
        for(int i = 0; i < temp; i++) 
            ind = ind + "1";
        
        if(temp == 0)
            index_temp = 0;
        else 
            index_temp = address & Integer.parseInt(ind, 2);
        

        System.out.println("Inside Cache " + address);
        int add_tag = Integer.parseInt(a.substring(0, a.length()-temp),2);

        if(add_tag == cacheline[index_temp].tag[0]){
            cacheline[index_temp].lru = 1;
            isPresent = true;
            return cacheline[index_temp].data[0];
        }
        else if(add_tag == cacheline[index_temp].tag[1]){
            cacheline[index_temp].lru = 0;
            isPresent = true;
            return cacheline[index_temp].data[1];
        }
        else {
            isPresent = false;
            return -1;
        }

    }

    public void cache_write(int address, int value){
        String a = Integer.toBinaryString(address);
        String ind = "";
        int index_temp;

        for(int i = 0; i < 32-a.length(); i++)
            a = "0" + a;
        
        for(int i = 0; i < temp; i++ )
            ind = ind + "1";
        
        if(temp == 0)
            index_temp = 0;
        else 
            index_temp = address & Integer.parseInt(ind, 2);
        

        int tag = Integer.parseInt(a.substring(0, a.length()-temp),2);
        cacheline[index_temp].set_cache_value(tag, value);

    }


    @Override
	public void handleEvent(Event e) {

        if(e.getEventType() == Event.EventType.MemoryRead){
            System.out.println("handle event cache memory read");
            MemoryReadEvent ee = (MemoryReadEvent) e;
            int data = read_cache(ee.getAddressToReadFrom());
            if(isPresent == true){
                Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                        Clock.getCurrentTime() + this.latency, 
                        this, 
                        ee.getRequestingElement(), 
                        data)
                );
            }
            else{
                System.out.println("Missed");
                this.miss_addr = ee.getAddressToReadFrom();

                ee.setEventTime(Clock.getCurrentTime() + Configuration.mainMemoryLatency+1);
                Simulator.getEventQueue().addEvent(ee);
                cache_miss_handler(ee.getAddressToReadFrom());
            }
        }

       else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent ee = (MemoryResponseEvent) e;
            cache_write(this.miss_addr, ee.getValue());
            
        }
        
        else if(e.getEventType() == Event.EventType.MemoryWrite){
            System.out.println("handle event cache memory write");
            MemoryWriteEvent ee = (MemoryWriteEvent) e;
            cache_write(ee.getAddressToWriteTo(), ee.getValue());

            containingProcessor.getMainMemory().setWord(ee.getAddressToWriteTo(), ee.getValue());

            Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime()+Configuration.mainMemoryLatency, 
					containingProcessor.getMainMemory(), 
					ee.getRequestingElement())
			);
            
		}
		
	}

}