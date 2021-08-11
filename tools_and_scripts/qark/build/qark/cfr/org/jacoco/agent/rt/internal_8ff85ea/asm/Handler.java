/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

import org.jacoco.agent.rt.internal_8ff85ea.asm.Label;

class Handler {
    String desc;
    Label end;
    Label handler;
    Handler next;
    Label start;
    int type;

    Handler() {
    }

    static Handler remove(Handler handler, Label label, Label label2) {
        if (handler == null) {
            return null;
        }
        handler.next = Handler.remove(handler.next, label, label2);
        int n = handler.start.position;
        int n2 = handler.end.position;
        int n3 = label.position;
        int n4 = label2 == null ? Integer.MAX_VALUE : label2.position;
        if (n3 < n2 && n4 > n) {
            if (n3 <= n) {
                if (n4 >= n2) {
                    return handler.next;
                }
                handler.start = label2;
                return handler;
            }
            if (n4 >= n2) {
                handler.end = label;
                return handler;
            }
            Handler handler2 = new Handler();
            handler2.start = label2;
            handler2.end = handler.end;
            handler2.handler = handler.handler;
            handler2.desc = handler.desc;
            handler2.type = handler.type;
            handler2.next = handler.next;
            handler.end = label;
            handler.next = handler2;
        }
        return handler;
    }
}

