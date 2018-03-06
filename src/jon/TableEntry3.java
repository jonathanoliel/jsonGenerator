package jon;

import javafx.beans.property.SimpleBooleanProperty;

public class TableEntry3 extends TableEntry2{
	    public final SimpleBooleanProperty active;

	    public TableEntry3(String fkey, String fvalue, int fidValue, Boolean factive) {
	    	super(fkey,fvalue,fidValue);
	        this.active = new SimpleBooleanProperty(factive);
	    }
	    public Boolean getActive() {
	        return active.get();
	    }
	    public void setActive(Boolean fvalue) {
	    	active.set(fvalue);
	    }
	   
	   
	   
	   
}