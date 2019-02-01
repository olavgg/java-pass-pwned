package com.olavgg.pc;

import com.github.mgunlogson.cuckoofilter4j.CuckooFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class PasswordReader {


    /*public static CuckooFilter<byte[]> FILTER =
            new CuckooFilter.Builder<>(
                    Funnels.byteArrayFunnel(),
                    551509767
    )
        .withFalsePositiveRate(0.001)
        .withExpectedConcurrency(64)
        .build();*/

    public static CuckooFilter<byte[]> FILTER;

    public static void read() throws IOException {
        //String filename = "/tank/rot/16.txt";
        /*String filename = "/mnt/tank10/rot/16.txt";
        AtomicLong count = new AtomicLong();
        try (Stream<String> linesStream = Files.lines(Paths.get(filename))) {
            linesStream.forEach( line -> {
                count.getAndIncrement();
                if((count.get() % 1000000) == 0){
                    System.out.println(count.get());
                }
                FILTER.put(line.getBytes());
            });
        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("pw.filter"));
        out.writeObject(FILTER);
        out.close();*/


        try {
            String filename = "pw.filter";

            InputStream buffer = new BufferedInputStream(
                    new FileInputStream(filename)
            );
            ObjectInput input = new ObjectInputStream(buffer);

            FILTER = (CuckooFilter<byte[]>)input.readObject();
        } catch (RuntimeException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
