package com.senacor.hackingdays.lmax.generate;

import com.senacor.hackingdays.lmax.generate.model.Gender;

import java.util.function.Function;

public interface NameSupplier extends Function<Gender, String> {
}
