/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptospout.cat;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.Observable;
import java.util.Observer;
import com.cryptospout.cat.transformer.ObservableTransformer;
import com.cryptospout.cat.transformer.ModifyStorageDirectory;

/**
 *
 * @author Cryptospout
 */
public class CatExtended implements Observer {

    public static void premain(String s, Instrumentation instrumentation) {
        CatExtended CatExtended = new CatExtended(instrumentation);

    }
    private final Instrumentation instrumentation;

    private CatExtended(Instrumentation instrumentation) {

        ObservableTransformer[] transformers = new ObservableTransformer[]{new ModifyStorageDirectory()};
        for (ObservableTransformer ot : transformers) {
            ot.addObserver(this);
            instrumentation.addTransformer((ClassFileTransformer) ot);
        }
        this.instrumentation = instrumentation;
    }

    @Override
    public void update(Observable o, Object arg) {
        instrumentation.removeTransformer((ClassFileTransformer) o);
    }
}
