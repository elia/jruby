package org.jruby.compiler.ir.operands;

import java.util.List;

import org.jruby.RubyRegexp;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

// Represents a backref node in Ruby code
//
// NOTE: This operand is only used in the initial stages of optimization
// Further down the line, it could get converted to calls
public class Backref extends Reference {
    final public char type; 

    public Backref(char t) {
        super("$" + t);
        type = t;
    }

    @Override
    public boolean isNonAtomicValue() {
        return true;
    }

    public Object retrieve(ThreadContext context, IRubyObject self, DynamicScope currDynScope, Object[] temp) {
        IRubyObject backref = currDynScope.getBackRef(context.getRuntime());
        
        switch (type) {
        case '&':
            return RubyRegexp.last_match(backref);
        case '`':
            return RubyRegexp.match_pre(backref);
        case '\'':
            return RubyRegexp.match_post(backref);
        case '+':
            return RubyRegexp.match_last(backref);
        default:
            assert false: "backref with invalid type";
            return null;
        } 
    }
}
