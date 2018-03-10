package org.processmining.openslex.metamodel;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

public class AttNamesMapSerializer implements Serializer<HashMap<String,AbstractAttDBElement>>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -525690979880509828L;
	private SLEXMMStorageMetaModel storage;
	private DBElementSerializer elementSerializer;
	
	public AttNamesMapSerializer(SLEXMMStorageMetaModel storage) {
		this.storage = storage;
		this.elementSerializer = new DBElementSerializer(storage);
	}
	
	private void serializeElement(DataOutput2 out, AbstractDBElement o) throws IOException {
		elementSerializer.serialize(out, o);
	}
	
    @Override
    public void serialize(DataOutput2 out, HashMap<String,AbstractAttDBElement> value) throws IOException {
        
    	out.writeInt(value.size());
    	for (Entry<String,AbstractAttDBElement> aten: value.entrySet()) {
    		AbstractAttDBElement at = aten.getValue();
    		serializeElement(out, at);
    	}
    }

    @Override
    public HashMap<String,AbstractAttDBElement> deserialize(DataInput2 in, int available) throws IOException {
    	HashMap<String,AbstractAttDBElement> map = new HashMap<>();
    	
    	int size = in.readInt();
    	for (int i = 0; i < size; i++) {
    		
    		AbstractAttDBElement at = (AbstractAttDBElement) elementSerializer.deserialize(in, available);
    		
    		AbstractAttDBElement at2 = storage.getFromCache(at.getClazz(), at.getId());
    		
    		if (at2 != null) {
    			at = at2;
    		}
    		
    		String atName = at.getName();
    		
    		map.put(atName, at);
    	}
    	
    	return map;
    }

}