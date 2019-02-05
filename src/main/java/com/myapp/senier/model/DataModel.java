package com.myapp.senier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class DataModel extends HashMap<String, Object> {
    private final static long serialVersionUID = 536871008;

    public DataModel() {
        super();
    }

    public DataModel(Map<? extends String, ?> map) {
        super(map);
    }

    public void putAll(Object value) throws Exception {
        // List<Map<>> 형태의 데이터를 일괄 삽입
        if(value instanceof List) {
            List<?> list = (List<?>)value;
            
            list.forEach(o -> {
                if(o instanceof Map) {
                    Map<String, Object> map = (HashMap<String, Object>)o;

                    map.forEach((k, v) -> {
                        super.put(k, v);    
                    });
                }
            });
        } else {
            super.putAll((HashMap<String, Object>)value);
        }
    }

    public void putJson(JSONObject object) {
        Iterator<String> iter = object.keySet().iterator();

        while(iter.hasNext()) {
            String key = iter.next();
            Object value = object.get(key);
            super.put(key, value);
        }
    }

    public void putJsonString(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        super.putAll(mapper.readValue(jsonString, new TypeReference<DataModel>() {}));
    }

    public HashMap<? extends String, ?> clone() {
        return (HashMap<String, Object>) super.clone();
    }

    public void putStrNull(String key, String value) {
        if(value == null) {
            super.put(key, "");
        } else {
            super.put(key, value);
        }
    }

    public void putInteger(String key, Object value) {
        if(value instanceof String) {
            super.put(key, Integer.parseInt(value.toString()));
        } else {
            super.put(key, (Integer)value);
        }
    }

    public void putLong(String key, Object value) {
        if(value instanceof String) {
            super.put(key, Long.parseLong(value.toString()));
        } else {
            super.put(key, (Long)value);
        }
    }

    public void putDouble(String key, Object value) {
        if(value instanceof String) {
            super.put(key, Double.parseDouble(value.toString()));
        } else {
            super.put(key, (Double)value);
        }
    }

    public void putBoolean(String key, Object value) {
        if(value instanceof String) {
            super.put(key, Boolean.parseBoolean(value.toString()));
        } else {
            super.put(key, (Boolean)value);
        }
    }

    public Object get(String key) {
        Object o = super.get(key);

        if(o instanceof List) {
            return (ArrayList<?>)o;
        } else if(o instanceof Map) {
            return (HashMap<?, ?>)o;
        } else {
            return o;
        }
    }

    public String getStrNull(String key) {
        Object o = super.get(key);
        
        if(o == null) {
            return "";
        }
        return o.toString();
    }

    public Integer getInteger(String key) {
        Object o = super.get(key);
        
        if(o == null) {
            return 0;
        }
        return (Integer)o;
    }
    
    public Double getDouble(String key) {
        Object o = super.get(key);
        
        if(o == null) {
            return 0.0d;
        }
        return (Double)o;
    }

    public Long getLong(String key) {
        Object o = super.get(key);
        
        if(o == null) {
            return 0l;
        }
        return (Long)o;
    }

    public Boolean getBoolean(String key) {
        Object o = super.get(key);
        
        if(o == null) {
            return false;
        }
        return (Boolean)o;
    }

    public ArrayList<?> getArrayList(String key) {
        Object o = super.get(key);

        if(o != null && o instanceof List) {
            return (ArrayList<?>)o;
        } 
        
        return new ArrayList<>();
    }
}