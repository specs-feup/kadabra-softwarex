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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import weaver.kadabra.agent.asm.ASMUtils;

public class CountingSortGenerator implements Opcodes {

    public static void specialize(MethodVisitor mv, int size) {
        CountingSortGenerator sn = new CountingSortGenerator(mv, false, size);
        sn.generate();
    }

    public static void specializeStatic(MethodVisitor mv, int size) {
        CountingSortGenerator sn = new CountingSortGenerator(mv, true, size);
        sn.generate();
    }

    private final MethodVisitor mv;
    private final int size;
    private final int windowSize;
    private static final int SORTING_TABLE_INDEX = 6;
    private int startVar = 0;

    private CountingSortGenerator(MethodVisitor mv, boolean isStatic, int size) {
        this.mv = mv;
        if (!isStatic) {
            startVar = 1;
        }
        this.size = size;
        windowSize = size * size;

    }

    private void generate() {
        if (windowSize % 2 == 0) {

            throw new RuntimeException("Even kernelSize not yet implemented");
        }

        mv.visitCode();
        Label l0 = new Label();
        Label lsum = new Label();
        Label lmedian = new Label();
        initSortingTable(l0);
        fillTable();
        calcMedian(lsum, lmedian);
        declareVars(l0, lsum, lmedian);
        mv.visitEnd();

    }

    private void fillTable() {
        // sortingTable[input[y - 1][x - 1]]++;
        int corner = size / 2;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Label l1 = new Label();
                mv.visitLabel(l1);
                visitVar(ALOAD, 6);
                visitVar(ALOAD, 0);
                visitVar(ILOAD, 4);

                visitAddOrSub(i - corner);
                mv.visitInsn(AALOAD);
                visitVar(ILOAD, 5);
                visitAddOrSub(j - corner);
                mv.visitInsn(IALOAD);
                mv.visitInsn(DUP2);
                mv.visitInsn(IALOAD);

                mv.visitInsn(ICONST_1);
                mv.visitInsn(IADD);
                mv.visitInsn(IASTORE);
            }
        }
    }

    private void visitVar(int opcode, int var) {
        mv.visitVarInsn(opcode, getVar(var));
    }

    private void visitAddOrSub(int i) {
        if (i != 0) {
            visitConstantInstr(Math.abs(i));
            if (i > 0) {

                mv.visitInsn(IADD);
            } else if (i < 0) {
                mv.visitInsn(ISUB);
            }
        }
    }

    private void calcMedian(Label lsum, Label lmedian) {
        mv.visitLabel(lsum);
        mv.visitInsn(ICONST_0);
        visitVar(ISTORE, 7);
        mv.visitLabel(lmedian);
        mv.visitInsn(ICONST_0);
        visitVar(ISTORE, 8);

        Label l13 = new Label();
        mv.visitJumpInsn(GOTO, l13);
        Label l14 = new Label();
        mv.visitLabel(l14);
        mv.visitLineNumber(43, l14);
        mv.visitFrame(Opcodes.F_APPEND, 3, new Object[] { "[I", Opcodes.INTEGER, Opcodes.INTEGER }, 0, null);
        visitVar(ILOAD, 7);
        visitVar(ALOAD, 6);
        visitVar(ILOAD, 8);
        mv.visitInsn(IALOAD);
        mv.visitInsn(IADD);
        visitVar(ISTORE, 7);
        Label l15 = new Label();
        mv.visitLabel(l15);
        mv.visitLineNumber(44, l15);
        mv.visitIincInsn(getVar(8), 1);
        mv.visitLabel(l13);
        mv.visitLineNumber(42, l13);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        visitVar(ILOAD, 7);
        int div = windowSize / 2 + 1;
        visitConstantInstr(div);
        mv.visitJumpInsn(IF_ICMPLT, l14);
        Label l16 = new Label();
        mv.visitLabel(l16);
        mv.visitIincInsn(getVar(8), -1);
        visitVar(ILOAD, 8);
        mv.visitInsn(IRETURN);
    }

    private void initSortingTable(Label ltable) {
        // int[] sortingTable = new int[256];
        mv.visitLabel(ltable);
        mv.visitIntInsn(SIPUSH, 256);
        mv.visitIntInsn(NEWARRAY, T_INT);
        visitVar(ASTORE, SORTING_TABLE_INDEX);
    }

    public void declareVars(Label l0, Label lSum, Label lMedian) {
        Label lLast = new Label();
        mv.visitLabel(lLast);
        // In this case we don't care about "this" variable
        mv.visitLocalVariable("input", "[[I", null, l0, lLast, getVar(0));
        mv.visitLocalVariable("kernelSize", "I", null, l0, lLast, getVar(1));
        mv.visitLocalVariable("width", "I", null, l0, lLast, getVar(2));
        mv.visitLocalVariable("height", "I", null, l0, lLast, getVar(3));
        mv.visitLocalVariable("y", "I", null, l0, lLast, getVar(4));
        mv.visitLocalVariable("x", "I", null, l0, lLast, getVar(5));
        mv.visitLocalVariable("sortingTable", "[I", null, l0, lLast, getVar(SORTING_TABLE_INDEX)); // i.e. 6
        mv.visitLocalVariable("sum", "I", null, lSum, lLast, getVar(7));
        mv.visitLocalVariable("median", "I", null, lMedian, lLast, getVar(8));

        int maxLocals = 9 + startVar;// 8 (last local var index) + 1 (numeration starts in 0)
        mv.visitMaxs(3, maxLocals);
    }

    public int getVar(int i) {
        return i + startVar;
    }

    private void visitConstantInstr(int i) {
        ASMUtils.visitConstantInstr(i, mv);
    }
}
