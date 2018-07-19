package com.yash.ota.daoimpl;

import com.yash.ota.dao.BatchDAO;
import com.yash.ota.model.Batch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BatchDAOImpl implements BatchDAO {

    private static File batchFile = new File("Batch.txt");
    private List<Batch> batches;

    @Override
    public boolean insert(Batch batch) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            if (!batchFile.exists())
                batchFile.createNewFile();

            bufferedWriter = new BufferedWriter(new FileWriter(batchFile, true));
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.println(batch.getId() + "," + batch.getTitle());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
        return true;
    }

    @Override
    public boolean delete(int id) {
        batches = list();
        for (Batch batch1 : batches) {
            if (batch1.getId() == id) {
                batches.remove(batch1);
                break;
            }
        }
        writeToFile(batches);
        return true;
    }

    @Override
    public boolean update(Batch batch) {
        batches = list();
        for (Batch batch1 : batches) {
            if (batch1.getId() == batch.getId()) {
                batch1.setTitle(batch.getTitle());
                break;
            }
        }
        writeToFile(batches);
        return true;
    }

    @Override
    public List<Batch> list() {
        batches = new ArrayList<>();
        if (batchFile.exists()) {
            try {
                Scanner scanner = new Scanner(batchFile);
                while (scanner.hasNext()) {
                    Batch batch = new Batch();
                    String input[] = scanner.nextLine().split(",");
                    batch.setId(Integer.parseInt(input[0]));
                    batch.setTitle(input[1]);
                    batches.add(batch);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return batches;
    }

    private void writeToFile(List<Batch> batches) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(batchFile));
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            for (Batch batch : batches)
                printWriter.println(batch.getId() + "," + batch.getTitle());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
