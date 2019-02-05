package com.myapp.senier.persistence;

import java.util.List;

import com.myapp.senier.model.DataModel;

public interface JobMapper {
    public List<DataModel> getKeywords(String serviceCd);
    public DataModel getServerInfo(String serviceCd);
    public int insMorpheme(DataModel dm);
    public int insLogHistory(DataModel dm);
}
