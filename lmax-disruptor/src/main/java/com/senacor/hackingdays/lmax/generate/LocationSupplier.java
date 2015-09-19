package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Location;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

class LocationSupplier implements Supplier<Location> {
    private final List<Location> locations;
    private final Random rnd = new Random();

    private LocationSupplier(List<Location> locations) {
        this.locations = locations;
    }

    static LocationSupplier newInstance() {
        List<String> lines = LineReader.readLines("cities.txt");
        List<Location> locations = lines.stream().map(line -> line.split(",")).map(tokens -> new Location(tokens[1], tokens[2], tokens[0])).collect(toList());
        return new LocationSupplier(locations);
    }

    public Location get() {
        return locations.get(rnd.nextInt(locations.size()));
    }
}
