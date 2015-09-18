package com.senacor.hackingdays.serialization.data.generate;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 18/09/15
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public interface DataGenerator {
    void doEach(int size, Consumer consumer);
}
