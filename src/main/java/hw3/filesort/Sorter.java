package hw3.filesort;

import java.io.*;
import java.util.*;

public class Sorter {
    public File sortFile(File dataFile) throws IOException {

        long chunkSize = estimateChunkSize(dataFile);
        List<File> sortedChunkFiles = readAndSortChunks(dataFile, chunkSize);
        File resultFile = mergeSortedChunks(sortedChunkFiles);

        return resultFile;
    }

    private long estimateChunkSize(File file) {
        long fileSize = file.length();

        //Насколько я понял 1024 это стандартный лимит количества разрешенных временных файлов
        long chunkSize = fileSize / 1024;

        long allocatedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long freeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;

        if (chunkSize < freeMemory / 2) {
            chunkSize = freeMemory / 2;
        }
        return chunkSize;
    }

    private List<File> readAndSortChunks(File file, long chunkSize) throws IOException {
        List<File> sortedChunkList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            List<Long> chunkData = new ArrayList<>();
            String line = "";
            while (line != null) {
                long currentChunkSize = 0;
                while ((line = bufferedReader.readLine()) != null && currentChunkSize < chunkSize) {
                    chunkData.add(Long.parseLong(line));
                    //Для уверенности, что размер чанка будет близок к рассчитанному добавляем длину переноса строки.
                    //Хотя остается превышение в пределах около 20 байт, что я думаю в текущей задаче не критично
                    currentChunkSize += line.length() + System.lineSeparator().length();
                }
                sortedChunkList.add(sortToTempFile(chunkData));
                chunkData.clear();
            }
        }
        return sortedChunkList;
    }


    private File sortToTempFile(List<Long> chunkData) throws IOException {
        Collections.sort(chunkData);

        File tempFile = File.createTempFile("chunkSortedFile", "");
        tempFile.deleteOnExit();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (long element : chunkData) {
                writer.write(element + System.lineSeparator());
            }
        }
        return tempFile;
    }

    private File mergeSortedChunks(List<File> sortedChunks) throws IOException {
        List<BinaryFileBuffer> fileBuffers = new ArrayList<>();
        for (File chunkFile : sortedChunks) {
            fileBuffers.add(new BinaryFileBuffer(chunkFile));
        }
        //В чате сказали, что коллекции можно использовать
        PriorityQueue<BinaryFileBuffer> fileBuffersQueue = populateQueue(fileBuffers);

        File outputFile = new File("result.txt");

        writeToOutputFile(outputFile, fileBuffersQueue);

        for (File chunkFile : sortedChunks) {
            chunkFile.delete();
        }

        return outputFile;
    }

    private PriorityQueue<BinaryFileBuffer> populateQueue(List<BinaryFileBuffer> fileBuffers) {
        PriorityQueue<BinaryFileBuffer> priorityQueue = new PriorityQueue<>(Comparator.comparingLong(BinaryFileBuffer::peek));

        for (BinaryFileBuffer fileBuffer : fileBuffers) {
            if (!fileBuffer.empty()) {
                priorityQueue.add(fileBuffer);
            }
        }
        return priorityQueue;
    }

    private void writeToOutputFile(File outputFile, PriorityQueue<BinaryFileBuffer> fileBuffersQueue) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            while (fileBuffersQueue.size() > 0) {
                BinaryFileBuffer fileBuffer = fileBuffersQueue.poll();
                Long current = fileBuffer.pop();
                writer.write(current + System.lineSeparator());
                if (fileBuffer.empty()) {
                    fileBuffer.close();
                } else {
                    fileBuffersQueue.add(fileBuffer);
                }
            }
        } finally {
            for (BinaryFileBuffer fileBuffer : fileBuffersQueue) {
                fileBuffer.close();
            }
        }
    }
}
