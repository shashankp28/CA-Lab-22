package processor.memorysystem;

import java.io.*; 
import java.util.*; 
import generic.*;
import processor.*;
import configuration.Configuration;

public class Cache implements Element {
	public int latency ;
	Processor containingProcessor;
	int csize, miss_addr, read, write_data, temp;
    boolean isPresent = true;
    CacheLine[] cache_line;
    int[] index;
	
	public Cache(Processor containingProcessor, int latency, int cacheSize) {
		this.containingProcessor = containingProcessor;
        this.latency = latency;
        this.csize = cacheSize;

        this.temp = (int)(Math.log(this.csize/8)/Math.log(2));
        this.cache_line = new CacheLine[csize/8];

		for(int i = 0; i < csize/8; i++) 
			this.cache_line[i] = new CacheLine();
    }

    public boolean checkPresence() {
        return this.isPresent;
    }

    public int[] getIndexes() {
        return this.index;
    }

    public CacheLine[] getCaches() {
        return this.cache_line;
    }

    public Processor getProcessor() {
        return this.containingProcessor;
    }

    public void setProcessor(Processor processor) {
        this.containingProcessor = processor;
    }

    public String toString() {
        return Integer.toString(this.latency) + " : latency";
    }
    
    public void handleCacheMiss(int addr) {
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
        int temp_ind;
       
        for(int i = 0; i < 32-a.length(); i++)
            a = "0" + a;
        
        for(int i = 0; i < temp; i++) 
            ind = ind + "1";
        
        if(temp == 0)
            temp_ind = 0;
        else 
            temp_ind = address & Integer.parseInt(ind, 2);
        

        System.out.println("in the Cache " + address);
        int add_tag = Integer.parseInt(a.substring(0, a.length()-temp),2);

        if(add_tag == cache_line[temp_ind].tag[0]){
            cache_line[temp_ind].lru = 1;
            isPresent = true;
            return cache_line[temp_ind].data[0];
        }
        else if(add_tag == cache_line[temp_ind].tag[1]){
            cache_line[temp_ind].lru = 0;
            isPresent = true;
            return cache_line[temp_ind].data[1];
        }
        else {
            isPresent = false;
            return -1;
        }

    }

    public void write_cache(int address, int value){
        String a = Integer.toBinaryString(address);
        String ind = "";
        int temp_ind;

        for(int i = 0; i < 32-a.length(); i++)
            a = "0" + a;
        
        for(int i = 0; i < temp; i++ )
            ind = ind + "1";
        
        if(temp == 0)
            temp_ind = 0;
        else 
            temp_ind = address & Integer.parseInt(ind, 2);
        

        int tag = Integer.parseInt(a.substring(0, a.length()-temp),2);
        cache_line[temp_ind].setValue(tag, value);

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
                handleCacheMiss(ee.getAddressToReadFrom());
            }
        }

       else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent ee = (MemoryResponseEvent) e;
            write_cache(this.miss_addr, ee.getValue());
            
        }        
        else if(e.getEventType() == Event.EventType.MemoryWrite){
            System.out.println("handle event cache memory write");
            MemoryWriteEvent ee = (MemoryWriteEvent) e;
            write_cache(ee.getAddressToWriteTo(), ee.getValue());

            containingProcessor.getMainMemory().setWord(ee.getAddressToWriteTo(), ee.getValue());

            Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime()+Configuration.mainMemoryLatency, 
					containingProcessor.getMainMemory(), 
					ee.getRequestingElement())
			); 
        }}}