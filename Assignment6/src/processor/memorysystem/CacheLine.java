package processor.memorysystem;
import generic.*;
import processor.*;

public class CacheLine{
    int[] tag = new int[2]; 
    int[] data = new int[2];
    int lru;

    public CacheLine() {
        this.tag[0] = -1;
        this.tag[1] = -1;
        this.lru = 0;
    }

    public CacheLine(int updated_lru) {
        this.lru = updated_lru;
        this.tag[0] = -1;
        this.tag[1] = -1;
    }

    public int get_cache_data(int index) {
        return this.data[index];
    }

    public int get_cache_tag(int index) {
        return this.tag[index];
    }

    public int get_cache_lru() {
        return this.lru;
    }

    public int set_cache_lru(int newLru) {
        this.lru = newLru;
        return this.lru;
    }

    public void set_cache_value(int tag, int value) {
        if(tag == this.tag[0]) {
            this.data[0] = value;
            this.lru = 1;
        }
        else if(tag == this.tag[1]) {
            this.data[1] = value;
            this.lru = 0;
        }
        else {
            this.tag[this.lru] = tag;
            this.data[this.lru] = value;
            this.lru = 1- this.lru;
        }
	}

    public String toString() {
        return Integer.toString(this.lru);
    }
}