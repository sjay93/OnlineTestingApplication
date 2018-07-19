package com.yash.ota.serviceimpl;

import com.yash.ota.dao.BatchDAO;
import com.yash.ota.exception.DuplicateBatchException;
import com.yash.ota.exception.EmptyBatchDetailsException;
import com.yash.ota.exception.UpdateBatchException;
import com.yash.ota.model.Batch;
import com.yash.ota.service.BatchService;

import java.io.IOException;
import java.util.List;

public class BatchServiceImpl implements BatchService {

    private BatchDAO batchDAO;

    public BatchServiceImpl(BatchDAO batchDAO) {
        this.batchDAO = batchDAO;
    }

    @Override
    public boolean addBatch(Batch batch) throws EmptyBatchDetailsException, DuplicateBatchException, IOException {
        if (batch.getTitle() == null || batch.getTitle().isEmpty())
            throw new EmptyBatchDetailsException("Title is not entered");

        if (!checkForAlreadyRegisteredBatch(batch))
            return false;

        batchDAO.insert(batch);
        return true;
    }

    @Override
    public boolean updateBatch(Batch batch) throws UpdateBatchException {
        if (checkForBatchExistence(batch.getId()))
            throw new UpdateBatchException("Batch to be updated doesn't exists");

        return batchDAO.update(batch);
    }

    @Override
    public boolean delete(int id) throws EmptyBatchDetailsException {
        if (checkForBatchExistence(id))
            throw new EmptyBatchDetailsException("Batch to be deleted doesn't exists");

        return batchDAO.delete(id);
    }

    @Override
    public List<Batch> listBatch() {
        return batchDAO.list();
    }

    private boolean checkForAlreadyRegisteredBatch(Batch batch) throws DuplicateBatchException {
        for (Batch batch1 : listBatch()) {
            if (batch1.getTitle().equals(batch.getTitle()))
                throw new DuplicateBatchException("Batch Title Already Exists");
        }
        return true;
    }

    boolean checkForBatchExistence(int id) {
        boolean found = false;
        for (Batch batch : listBatch())
            if (batch.getId() == id) {
                found = true;
                break;
            }
        return !found;
    }
}
