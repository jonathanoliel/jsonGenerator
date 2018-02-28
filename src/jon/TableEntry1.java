package jon;

import javafx.beans.property.SimpleStringProperty;

public class TableEntry1 {
	    public final SimpleStringProperty keyName;
	    public final SimpleStringProperty valueName;

	    public TableEntry1(String fkey, String fvalue) {
	        this.keyName = new SimpleStringProperty(fkey);
	        this.valueName = new SimpleStringProperty(fvalue);
	    }

	    public String getKeyName() {
	        return keyName.get();
	    }
	    public void setKeyName(String fName) {
	    	keyName.set(fName);
	    }
	   
	    public String getValueName() {
	        return valueName.get();
	    }
	    public void setValueName(String fName) {
	    	valueName.set(fName);
	    }
	   
}
