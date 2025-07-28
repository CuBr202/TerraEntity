package org.confluence.terraentity.integration;

import com.google.common.base.Suppliers;
import net.neoforged.fml.ModList;

import java.util.function.Supplier;

public class ModChecker {


    public static Supplier<Boolean> isConfluenceLoaded = Suppliers.memoize(()->ModList.get().isLoaded("confluence"));

    public static Supplier<Boolean> isIrisLoaded = Suppliers.memoize(()->ModList.get().isLoaded("iris"));





}
