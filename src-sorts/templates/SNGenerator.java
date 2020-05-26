/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package templates;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import weaver.kadabra.agent.asm.ASMUtils;

public class SNGenerator implements Opcodes {

    public static void specialize(MethodVisitor mv, int kernelSize) {
        specializeWAccess(mv, false, kernelSize);
    }

    public static void specializeWAccess(MethodVisitor mv, int access, int kernelSize) {
        specializeWAccess(mv, (access & ACC_STATIC) != 0, kernelSize);
    }

    public static void specializeWAccess(MethodVisitor mv, boolean isStatic, int kernelSize) {
        SNGenerator sn = new SNGenerator(mv, isStatic, kernelSize);
        if (kernelSize >= 100) {
            sn.generateOriginal();
        } else {
            sn.generate();
        }
    }

    private final MethodVisitor mv;
    private final int arraySize;
    private int startVar = 0;

    private SNGenerator(MethodVisitor mv, boolean isStatic, int arraySize) {
        this.mv = mv;
        if (!isStatic) {
            startVar = 1;
        }
        this.arraySize = arraySize;
    }

    private void generate() {
        mv.visitCode();
        Label[] localVarsLabels = initLocalVariables();
        // Then generate the swaps

        generateSwapOddEven();

        addReturnStatement();
        // return statement
        // Declare local variables
        declareVars(localVarsLabels);
        mv.visitEnd();

    }

    public void declareVars(Label[] localVarsLabels) {
        Label lLast = new Label();
        mv.visitLabel(lLast);

        mv.visitLocalVariable("input", "[I", null, localVarsLabels[0], lLast, getVar(0));

        for (int i = 0; i < localVarsLabels.length - 2; i++) {
            Label start = localVarsLabels[i];
            mv.visitLocalVariable("value_" + i, "I", null, start, lLast, getVar(i + 1));
        }
        int maxLocals = 1 + this.arraySize;// +1 because var numeration starts in 0
        mv.visitMaxs(3, maxLocals);
    }

    public int getVar(int i) {
        return i + startVar;
    }

    public Label[] initLocalVariables() {
        int numLocalVars = this.arraySize;
        Label[] localVars = new Label[numLocalVars];
        // First initialize local variables
        for (int i = 0; i < this.arraySize; i++) {
            Label li = new Label();
            mv.visitLabel(li);
            localVars[i] = li;
            mv.visitVarInsn(ALOAD, getVar(0)); // load array
            visitConstantInstr(i); // load y position
            mv.visitInsn(IALOAD); // load value x position
            mv.visitVarInsn(ISTORE, getVar(i + 1));
        }
        return localVars;
    }

    private void addReturnStatement() {

        int localVarPos = 1;
        // First initialize local variables
        for (int i = 0; i < this.arraySize; i++) {
            mv.visitVarInsn(ALOAD, getVar(0)); // load array
            visitConstantInstr(i); // load y position
            mv.visitVarInsn(ILOAD, getVar(localVarPos));
            mv.visitInsn(IASTORE); // load value x position
            localVarPos++;
        }
        mv.visitInsn(RETURN);
    }

    private void generateSwapOddEven() {

        // StringBuilder even = new StringBuilder();
        // StringBuilder odd = new StringBuilder();
        List<Integer> odds = new ArrayList<>();
        List<Integer> evens = new ArrayList<>();
        for (int j = 0; j < arraySize - 2; j += 2) {
            odds.add(j);
            evens.add(j + 1);

        }
        if (arraySize % 2 == 0) {
            odds.add(arraySize - 2);
        }

        for (int i = 0; i < arraySize - 1; i += 2) {
            odds.forEach(this::swap);
            evens.forEach(this::swap);
        }
        if (arraySize % 2 != 0) {
            odds.forEach(this::swap);
        }
    }

    private void swap(int pos) {
        pos = getVar(pos + 1);

        Label lIf = new Label();
        Label lElse = new Label();
        mv.visitLabel(lIf);
        mv.visitVarInsn(ILOAD, pos); // load local_pos
        mv.visitVarInsn(ILOAD, pos + 1); // load local_pos+1

        mv.visitJumpInsn(IF_ICMPLE, lElse); // If <= go to else

        mv.visitVarInsn(ILOAD, pos); // load local_pos -> [pos
        mv.visitVarInsn(ILOAD, pos + 1); // load local_pos+1 -> [pos, pos+1
        mv.visitVarInsn(ISTORE, pos); // store in pos -> [pos
        mv.visitVarInsn(ISTORE, pos + 1); // store in pos+1 -> [

        mv.visitLabel(lElse);

    }

    private void visitConstantInstr(int i) {
        ASMUtils.visitConstantInstr(i, mv);
    }

    private void generateOriginal() {
        mv.visitCode();
        Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitLineNumber(27, label0);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitInsn(ARRAYLENGTH);
        mv.visitVarInsn(ISTORE, getVar(1));
        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitLineNumber(28, label1);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, getVar(2));
        Label label2 = new Label();
        mv.visitLabel(label2);
        Label label3 = new Label();
        mv.visitJumpInsn(GOTO, label3);
        Label label4 = new Label();
        mv.visitLabel(label4);
        mv.visitLineNumber(31, label4);
        mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER }, 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, getVar(3));
        Label label5 = new Label();
        mv.visitLabel(label5);
        Label label6 = new Label();
        mv.visitJumpInsn(GOTO, label6);
        Label label7 = new Label();
        mv.visitLabel(label7);
        mv.visitLineNumber(33, label7);
        mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { Opcodes.INTEGER }, 0, null);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        Label label8 = new Label();
        mv.visitJumpInsn(IF_ICMPLE, label8);
        Label label9 = new Label();
        mv.visitLabel(label9);
        mv.visitLineNumber(35, label9);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ISTORE, getVar(4));
        Label label10 = new Label();
        mv.visitLabel(label10);
        mv.visitLineNumber(36, label10);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        mv.visitInsn(IASTORE);
        Label label11 = new Label();
        mv.visitLabel(label11);
        mv.visitLineNumber(37, label11);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(4));
        mv.visitInsn(IASTORE);
        mv.visitLabel(label8);
        mv.visitLineNumber(31, label8);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitIincInsn(getVar(3), 2);
        mv.visitLabel(label6);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(1));
        mv.visitJumpInsn(IF_ICMPLT, label7);
        Label label12 = new Label();
        mv.visitLabel(label12);
        mv.visitLineNumber(42, label12);
        mv.visitInsn(ICONST_1);
        mv.visitVarInsn(ISTORE, getVar(3));
        Label label13 = new Label();
        mv.visitLabel(label13);
        Label label14 = new Label();
        mv.visitJumpInsn(GOTO, label14);
        Label label15 = new Label();
        mv.visitLabel(label15);
        mv.visitLineNumber(44, label15);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        Label label16 = new Label();
        mv.visitJumpInsn(IF_ICMPLE, label16);
        Label label17 = new Label();
        mv.visitLabel(label17);
        mv.visitLineNumber(46, label17);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ISTORE, getVar(4));
        Label label18 = new Label();
        mv.visitLabel(label18);
        mv.visitLineNumber(47, label18);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        mv.visitInsn(IASTORE);
        Label label19 = new Label();
        mv.visitLabel(label19);
        mv.visitLineNumber(48, label19);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(4));
        mv.visitInsn(IASTORE);
        mv.visitLabel(label16);
        mv.visitLineNumber(42, label16);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitIincInsn(getVar(3), 2);
        mv.visitLabel(label14);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(1));
        mv.visitJumpInsn(IF_ICMPLT, label15);
        Label label20 = new Label();
        mv.visitLabel(label20);
        mv.visitLineNumber(28, label20);
        mv.visitIincInsn(getVar(2), 1);
        mv.visitLabel(label3);
        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitVarInsn(ILOAD, getVar(1));
        mv.visitJumpInsn(IF_ICMPLT, label4);
        Label label21 = new Label();
        mv.visitLabel(label21);
        mv.visitLineNumber(54, label21);
        mv.visitVarInsn(ILOAD, getVar(1));
        mv.visitInsn(ICONST_2);
        mv.visitInsn(IREM);
        Label label22 = new Label();
        mv.visitJumpInsn(IFEQ, label22);
        Label label23 = new Label();
        mv.visitLabel(label23);
        mv.visitLineNumber(56, label23);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, getVar(2));
        Label label24 = new Label();
        mv.visitLabel(label24);
        Label label25 = new Label();
        mv.visitJumpInsn(GOTO, label25);
        Label label26 = new Label();
        mv.visitLabel(label26);
        mv.visitLineNumber(58, label26);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        Label label27 = new Label();
        mv.visitJumpInsn(IF_ICMPLE, label27);
        Label label28 = new Label();
        mv.visitLabel(label28);
        mv.visitLineNumber(60, label28);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(IALOAD);
        mv.visitVarInsn(ISTORE, getVar(3));
        Label label29 = new Label();
        mv.visitLabel(label29);
        mv.visitLineNumber(61, label29);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(IALOAD);
        mv.visitInsn(IASTORE);
        Label label30 = new Label();
        mv.visitLabel(label30);
        mv.visitLineNumber(62, label30);
        mv.visitVarInsn(ALOAD, getVar(0));
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(3));
        mv.visitInsn(IASTORE);
        mv.visitLabel(label27);
        mv.visitLineNumber(56, label27);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitIincInsn(getVar(2), 2);
        mv.visitLabel(label25);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, getVar(2));
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, getVar(1));
        mv.visitJumpInsn(IF_ICMPLT, label26);
        mv.visitLabel(label22);
        mv.visitLineNumber(66, label22);
        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        mv.visitInsn(RETURN);
        Label label31 = new Label();
        mv.visitLabel(label31);
        mv.visitLocalVariable("valuesArray", "[I", null, label0, label31, 0);
        mv.visitLocalVariable("length", "I", null, label1, label31, 1);
        mv.visitLocalVariable("i", "I", null, label2, label21, 2);
        mv.visitLocalVariable("j", "I", null, label5, label12, 3);
        mv.visitLocalVariable("temp", "I", null, label10, label8, 4);
        mv.visitLocalVariable("j", "I", null, label13, label20, 3);
        mv.visitLocalVariable("temp", "I", null, label18, label16, 4);
        mv.visitLocalVariable("i", "I", null, label24, label22, 2);
        mv.visitLocalVariable("temp", "I", null, label29, label27, 3);
        mv.visitMaxs(5, 6);
        mv.visitEnd();
    }
}
