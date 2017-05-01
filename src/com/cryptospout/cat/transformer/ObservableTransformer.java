/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptospout.cat.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Observable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 *
 * @author Cryptospout 
 * 
 * this will be fleshed out when we have transformers that require
 * other transformations to occur first
 */
public abstract class ObservableTransformer extends Observable implements ClassFileTransformer {

    protected ClassWriter classWriter;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try{
        final ClassReader classReader = new ClassReader(classfileBuffer);

        ClassNode n = new ClassNode(Opcodes.ASM5);

        classReader.accept(n, ClassReader.EXPAND_FRAMES);
        if (identify(n)) {
            try {
                ClassNode cn = transform(n);

                classWriter = new ClassWriter(classReader,
                        ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                cn.accept(classWriter);
                finish();
                return classWriter.toByteArray();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        return classfileBuffer;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public abstract boolean identify(ClassNode cn);

    public abstract ClassNode transform(ClassNode cn) throws TransformerException;

    public abstract void finish();

}
