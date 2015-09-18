package com.senacor.hackingdays.serialization.data.generate;

import com.senacor.hackingdays.serialization.data.proto.ProfileProtos;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

class LocationProtoSupplier implements Supplier<ProfileProtos.Location> {
    private final List<ProfileProtos.Location> locations;
    private final Random rnd = new Random();

    private LocationProtoSupplier(List<ProfileProtos.Location> locations) {
        this.locations = locations;
    }

    static LocationProtoSupplier newInstance() {
        List<String> lines = LineReader.readLines("cities.txt");
        List<ProfileProtos.Location> locations = lines.stream().map(line -> line.split(",")).map(tokens -> ProfileProtos.Location.newBuilder()
                .setState(tokens[1])
                .setCity(tokens[2])
                .setZip(tokens[3])
                .build())
                .collect(toList());
        return new LocationProtoSupplier(locations);
    }

    public ProfileProtos.Location get() {
        return locations.get(rnd.nextInt(locations.size()));
    }
}
