package com.yash.ota.service;

import com.yash.ota.exception.DuplicateBatchException;
import com.yash.ota.exception.EmptyBatchDetailsException;
import com.yash.ota.exception.UpdateBatchException;
import com.yash.ota.model.Batch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface BatchService {
    boolean addBatch(Batch batch) throws EmptyBatchDetailsException, DuplicateBatchException, IOException;

    boolean updateBatch(Batch batch) throws UpdateBatchException;

    boolean delete(int id) throws EmptyBatchDetailsException, FileNotFoundException;

    List<Batch> listBatch() throws FileNotFoundException;

}
