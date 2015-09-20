package com.senacor.hackingdays.distributedcache.generate;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.senacor.hackingdays.distributedcache.generate.model2.Location2;

class Location2Supplier implements Supplier<Location2> {
    private final List<Location2> locations;
    private final Random rnd = new Random();

    private Location2Supplier(List<Location2> locations) {
        this.locations = locations;
    }

    static Location2Supplier newInstance() {
        List<String> lines = LineReader.readLines("cities.txt");
        List<Location2> locations = lines.stream().map(line -> line.split(",")).map(tokens -> new Location2(tokens[1], tokens[2], tokens[0])).collect(toList());
        return new Location2Supplier(locations);
    }

    public Location2 get() {
        return locations.get(rnd.nextInt(locations.size()));
    }
}
