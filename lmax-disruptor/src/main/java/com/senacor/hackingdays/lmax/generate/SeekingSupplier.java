package com.senacor.hackingdays.lmax.generate;


import com.senacor.hackingdays.lmax.generate.model.Gender;
import com.senacor.hackingdays.lmax.generate.model.Range;
import com.senacor.hackingdays.lmax.generate.model.Seeking;

import java.util.Random;
import java.util.function.Function;

interface SeekingSupplier extends Function<Gender, Seeking> {

}
