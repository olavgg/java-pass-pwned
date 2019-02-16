package com.olavgg.pc;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class QualityTest {

    @Test
    public void testFilterQuality() throws IOException {
        final Path pwnedPassFilePath = Paths.get("16.txt");

        new PasswordReader().read();

        AtomicLong count = new AtomicLong();
        try (Stream<String> linesStream = Files.lines(pwnedPassFilePath)) {
            linesStream.forEach( line -> {
                count.getAndIncrement();
                if((count.get() % 1000000) == 0){
                    System.out.println("tests passed: " + count.get());
                }

                assertTrue(PasswordReader.FILTER.mightContain(line.getBytes()));
            });
        }
    }

}
