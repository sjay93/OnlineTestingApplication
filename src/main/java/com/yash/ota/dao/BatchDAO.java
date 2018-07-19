package com.yash.ota.dao;

import com.yash.ota.model.Batch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface BatchDAO {

    boolean insert(Batch batch) throws IOException;

    boolean delete(int id);

    boolean update(Batch batch);

    List<Batch> list();

}
