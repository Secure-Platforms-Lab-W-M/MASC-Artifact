// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

class Handler
{
    String desc;
    Label end;
    Label handler;
    Handler next;
    Label start;
    int type;
    
    static Handler remove(final Handler handler, final Label label, final Label label2) {
        if (handler == null) {
            return null;
        }
        handler.next = remove(handler.next, label, label2);
        final int position = handler.start.position;
        final int position2 = handler.end.position;
        final int position3 = label.position;
        int position4;
        if (label2 == null) {
            position4 = Integer.MAX_VALUE;
        }
        else {
            position4 = label2.position;
        }
        if (position3 < position2 && position4 > position) {
            if (position3 <= position) {
                if (position4 >= position2) {
                    return handler.next;
                }
                handler.start = label2;
                return handler;
            }
            else {
                if (position4 >= position2) {
                    handler.end = label;
                    return handler;
                }
                final Handler next = new Handler();
                next.start = label2;
                next.end = handler.end;
                next.handler = handler.handler;
                next.desc = handler.desc;
                next.type = handler.type;
                next.next = handler.next;
                handler.end = label;
                handler.next = next;
            }
        }
        return handler;
    }
}
