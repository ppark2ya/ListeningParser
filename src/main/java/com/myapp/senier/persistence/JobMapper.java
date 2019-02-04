package com.myapp.senier.persistence;

import com.myapp.senier.model.DataModel;

public interface JobMapper {
    public DataModel getKeywords();
    public DataModel getServerInfo(String serverCode);
    public int insLogData(DataModel dm);
    public int insMorpheme(DataModel dm);
}
