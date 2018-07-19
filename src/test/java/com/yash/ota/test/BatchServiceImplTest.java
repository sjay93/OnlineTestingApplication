package com.yash.ota.test;

import com.yash.ota.dao.BatchDAO;
import com.yash.ota.exception.DuplicateBatchException;
import com.yash.ota.exception.EmptyBatchDetailsException;
import com.yash.ota.exception.UpdateBatchException;
import com.yash.ota.model.Batch;
import com.yash.ota.service.BatchService;
import com.yash.ota.serviceimpl.BatchServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchServiceImplTest {
    private Batch batch;
    private BatchService batchService;
    private BatchDAO batchDAO;
    private List<Batch> batchList;

    @Before
    public void userSetup() {
        batchDAO = Mockito.mock(BatchDAO.class);
        batchService = new BatchServiceImpl(batchDAO);
        batch = new Batch();
        batch.setId(1);
        batch.setTitle("GTP2018");
        batchList = new ArrayList<>();
        batchList.add(batch);
    }

    @Test(expected = EmptyBatchDetailsException.class)
    public void testAddBatch_GivenAnyParticularFieldAsNullOrEmpty_ShouldThrowEmptyParameterException() throws EmptyBatchDetailsException, DuplicateBatchException, IOException {
        batch.setTitle("");
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Assert.assertFalse(batchService.addBatch(batch));
    }

    @Test(expected = DuplicateBatchException.class)
    public void testAddBatch_GivenDuplicateBatch_ShouldThrowDuplicateUserException() throws DuplicateBatchException, EmptyBatchDetailsException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Mockito.when(batchDAO.list()).thenReturn(batchList);
        Assert.assertFalse(batchService.addBatch(batch));
    }

    @Test
    public void testAddBatch_GivenAllDetails_ShouldAddBatch() throws DuplicateBatchException, EmptyBatchDetailsException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Assert.assertTrue(batchService.addBatch(batch));
    }

    @Test(expected = UpdateBatchException.class)
    public void testUpdateBatch_GivenIncorrectBatch_ShouldThrowUpdateBatchException() throws UpdateBatchException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Mockito.when(batchDAO.list()).thenReturn(batchList);
        Batch batch1 = new Batch();
        batch1.setId(2);
        batch1.setTitle(batch.getTitle());
        Assert.assertFalse(batchService.updateBatch(batch1));
    }

    @Test
    public void testUpdateBatch_GivenCorrectBatchDetails_ShouldUpdateBatchDetails() throws UpdateBatchException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Mockito.when(batchDAO.list()).thenReturn(batchList);
        Batch batch1 = new Batch();
        batch1.setId(batch.getId());
        batch1.setTitle("GTP_2018");
        Assert.assertTrue(batchService.updateBatch(batch1));
    }

    @Test(expected = EmptyBatchDetailsException.class)
    public void testDeleteBatch_GivenNonExistentBatchDetails_ShouldThrowRemoveException() throws EmptyBatchDetailsException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Mockito.when(batchDAO.list()).thenReturn(batchList);
        Assert.assertFalse(batchService.delete(2));
    }

    @Test
    public void testDeleteBatch_GivenCorrectBatchDetails_ShouldDeleteBatch() throws EmptyBatchDetailsException, IOException {
        Mockito.when(batchDAO.insert(batch)).thenReturn(true);
        Mockito.when(batchDAO.list()).thenReturn(batchList);
        Assert.assertTrue(batchService.delete(1));
        Assert.assertEquals(batchList.size(), 0);
    }
}
