package com.olavgg.pc;

import com.github.mgunlogson.cuckoofilter4j.CuckooFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class PasswordReader {

    public static CuckooFilter<byte[]> FILTER;

    public void read() throws IOException {
        final Path filterFilePath = Paths.get("pw.filter");
        final Path pwnedPassFilePath = Paths.get("16.txt");

        if(Files.isRegularFile(filterFilePath)){
            loadFilter(filterFilePath);
        } else {
            loadPasswords(pwnedPassFilePath, filterFilePath);
        }
    }

    private void loadPasswords(
            Path pwnedPassFilePath,
            Path filterFilePath
    ) throws IOException{

        FILTER =
                new CuckooFilter.Builder<>(
                        Funnels.byteArrayFunnel(),
                        551509767
                )
                .withFalsePositiveRate(0.08)
                .withExpectedConcurrency(64)
                .build();

        AtomicLong count = new AtomicLong();
        try (Stream<String> linesStream = Files.lines(pwnedPassFilePath)) {
            linesStream.forEach( line -> {
                count.getAndIncrement();
                if((count.get() % 1000000) == 0){
                    System.out.println(count.get());
                }
                FILTER.put(line.getBytes());
            });
        }

        ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(filterFilePath)
        );
        out.writeObject(FILTER);
        out.close();
    }

    @SuppressWarnings("unchecked")
    private void loadFilter(Path filterFilePath) throws IOException{
        int bufferSize = 1024 * 64;
        try (
                InputStream fileInputStream = Files.newInputStream(
                        filterFilePath,
                        StandardOpenOption.READ
                );
                InputStream buffer = new BufferedInputStream(
                        fileInputStream,
                        bufferSize
                );
                ObjectInput input = new ObjectInputStream(buffer)
        ){

            FILTER = (CuckooFilter<byte[]>)input.readObject();

        } catch (RuntimeException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
