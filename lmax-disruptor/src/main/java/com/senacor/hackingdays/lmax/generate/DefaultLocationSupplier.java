package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Location;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

class DefaultLocationSupplier implements LocationSupplier {
    private final Random rnd = new Random();
    private final List<Location> locations;

    DefaultLocationSupplier() {
        this.locations = lineSplit();
    }


    private List<Location> lineSplit() {
        List<String> lines = LineReader.readLines("cities.txt");
        return lines.stream().map(line -> line.split(",")).map(tokens -> new Location(tokens[1], tokens[2], tokens[0])).collect(toList());
    }

    @Override
    public Location get() {
        return locations.get(rnd.nextInt(locations.size()));
    }
}
