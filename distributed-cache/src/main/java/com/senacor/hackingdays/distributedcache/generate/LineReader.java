package com.senacor.hackingdays.distributedcache.generate;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

class LineReader {


    static List<String> readLines(String fileName) {
        try {
            System.out.println("readLines: " + fileName);
            return Resources.readLines(Thread.currentThread().getContextClassLoader().getResource(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
           throw new UncheckedIOException(e);
        }
    }
}
