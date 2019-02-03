package com.olavgg.pc;

import com.github.mgunlogson.cuckoofilter4j.CuckooFilter;
import com.google.common.hash.Funnels;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
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
            loadPasswords(pwnedPassFilePath);
        }
    }

    private void loadPasswords(Path pwnedPassFilePath) throws IOException{

        FILTER =
                new CuckooFilter.Builder<>(
                        Funnels.byteArrayFunnel(),
                        551509767
                )
                .withFalsePositiveRate(0.001)
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
    }

    private void loadFilter(Path filterFilePath) throws IOException{
        try {
            InputStream buffer = Files.newInputStream(
                    filterFilePath,
                    StandardOpenOption.READ
            );
            ObjectInput input = new ObjectInputStream(buffer);

            FILTER = (CuckooFilter<byte[]>)input.readObject();
        } catch (RuntimeException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
