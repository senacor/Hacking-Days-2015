package com.senacor.hackingdays.serialization.data.generate;

import com.senacor.hackingdays.serialization.data.thrift.Location;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

class LocationSupplierThrift implements Supplier<Location> {
    private final List<Location> locations;
    private final Random rnd = new Random();

    private LocationSupplierThrift(List<Location> locations) {
        this.locations = locations;
    }

    static LocationSupplierThrift newInstance() {
        List<String> lines = LineReader.readLines("cities.txt");
        List<Location> locations = lines.stream().map(line -> line.split(",")).map(tokens -> new Location(tokens[1], tokens[2], tokens[0])).collect(toList());
        return new LocationSupplierThrift(locations);
    }

    public Location get() {
        return locations.get(rnd.nextInt(locations.size()));
    }
}
