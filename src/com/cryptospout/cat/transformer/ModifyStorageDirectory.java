/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptospout.cat.transformer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.Opcodes;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 *
 * @author Cryptospout
 * CAT uses protected domain code source for finding the data
 * directly. Due to running a javaagent prior to running CAT this will throw a
 * security exception. This transformer will remove the protected domain
 * calls.
 */
public class ModifyStorageDirectory extends ObservableTransformer {

    protected boolean debug = false;

    @Override
    public boolean identify(ClassNode n) {

        return n.name.equals("CAT/A_Main/A_Support/PATHS");

    }

    @Override
    public ClassNode transform(ClassNode n) throws TransformerException {
        for (Object m : n.methods) {
            MethodNode mn = (MethodNode) m;
            if (mn.name.equals("getJarContainingFolder")) {
                modifyMethod(mn);
                return n;
            }
        }
        throw new TransformerException("Can't transform Class");
    }

    @Override
    public void finish() {

        try {
            FileOutputStream out = new FileOutputStream("PATHS.class");
            out.write(classWriter.toByteArray());
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModifyStorageDirectory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModifyStorageDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
        debug = false;

        notifyObservers();
    }

    private void modifyMethod(MethodNode methodNode) {
        methodNode.instructions.clear();
        methodNode.exceptions.clear();
        methodNode.tryCatchBlocks.clear();
        InsnList il = new InsnList();

        il.add(new LdcInsnNode("user.home"));
        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "java/lang/System", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;", false));
        il.add(new InsnNode(Opcodes.ARETURN));
        methodNode.instructions.add(il);

    }

}
