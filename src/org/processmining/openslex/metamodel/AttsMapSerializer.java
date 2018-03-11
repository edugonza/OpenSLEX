package org.processmining.openslex.metamodel;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

public class AttsMapSerializer implements Serializer<HashMap<AbstractAttDBElement,AbstractDBElementWithValue>>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4752424952988775720L;
	private SLEXMMStorageMetaModel storage;
	private DBElementSerializer elementSerializer;
	
	public AttsMapSerializer(SLEXMMStorageMetaModel storage) {
		this.storage = storage;
		this.elementSerializer = new DBElementSerializer(storage);
	}
	
	private void serializeElement(DataOutput2 out, AbstractDBElement o) throws IOException {
		elementSerializer.serialize(out, o);
	}
	
    @Override
    public void serialize(DataOutput2 out, HashMap<AbstractAttDBElement,AbstractDBElementWithValue> value) throws IOException {
        
    	out.writeInt(value.size());
    	for (Entry<AbstractAttDBElement,AbstractDBElementWithValue> aten: value.entrySet()) {
    		AbstractAttDBElement at = aten.getKey();
    		AbstractDBElement atv = aten.getValue();
    		serializeElement(out, at);
    		serializeElement(out, atv);
    	}
    }

    @Override
    public HashMap<AbstractAttDBElement,AbstractDBElementWithValue> deserialize(DataInput2 in, int available) throws IOException {
    	HashMap<AbstractAttDBElement,AbstractDBElementWithValue> map = new HashMap<>();
    	
    	int size = in.readInt();
    	for (int i = 0; i < size; i++) {
    		AbstractAttDBElement at = (AbstractAttDBElement) elementSerializer.deserialize(in, available);
    		
    		AbstractAttDBElement at2 = storage.getFromCache(at.getClazz(), at.getId());
    		
    		if (at2 != null) {
    			at = at2;
    		}
    		
    		AbstractDBElementWithValue atv = (AbstractDBElementWithValue) elementSerializer.deserialize(in, available);
    		
    		AbstractDBElementWithValue atv2 = storage.getFromCache(atv.getClazz(), atv.getId());
    		
    		if (atv2 != null) {
    			atv = atv2;
    		}
    		    		
    		map.put(at, atv);
    	}
    	
    	return map;
    }

}