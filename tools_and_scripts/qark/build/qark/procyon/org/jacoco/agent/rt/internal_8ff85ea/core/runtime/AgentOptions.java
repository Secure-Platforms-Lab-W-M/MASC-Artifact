// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.util.List;
import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Collection;
import java.util.regex.Pattern;

public final class AgentOptions
{
    public static final String ADDRESS = "address";
    public static final String APPEND = "append";
    public static final String CLASSDUMPDIR = "classdumpdir";
    public static final String DEFAULT_ADDRESS;
    public static final String DEFAULT_DESTFILE = "jacoco.exec";
    public static final int DEFAULT_PORT = 6300;
    public static final String DESTFILE = "destfile";
    public static final String DUMPONEXIT = "dumponexit";
    public static final String EXCLCLASSLOADER = "exclclassloader";
    public static final String EXCLUDES = "excludes";
    public static final String INCLBOOTSTRAPCLASSES = "inclbootstrapclasses";
    public static final String INCLNOLOCATIONCLASSES = "inclnolocationclasses";
    public static final String INCLUDES = "includes";
    public static final String JMX = "jmx";
    private static final Pattern OPTION_SPLIT;
    public static final String OUTPUT = "output";
    public static final String PORT = "port";
    public static final String SESSIONID = "sessionid";
    private static final Collection<String> VALID_OPTIONS;
    private final Map<String, String> options;
    
    static {
        OPTION_SPLIT = Pattern.compile(",(?=[a-zA-Z0-9_\\-]+=)");
        DEFAULT_ADDRESS = null;
        VALID_OPTIONS = Arrays.asList("destfile", "append", "includes", "excludes", "exclclassloader", "inclbootstrapclasses", "inclnolocationclasses", "sessionid", "dumponexit", "output", "address", "port", "classdumpdir", "jmx");
    }
    
    public AgentOptions() {
        this.options = new HashMap<String, String>();
    }
    
    public AgentOptions(final String s) {
        this();
        if (s != null && s.length() > 0) {
            final String[] split = AgentOptions.OPTION_SPLIT.split(s);
            for (int length = split.length, i = 0; i < length; ++i) {
                final String s2 = split[i];
                final int index = s2.indexOf(61);
                if (index == -1) {
                    throw new IllegalArgumentException(String.format("Invalid agent option syntax \"%s\".", s));
                }
                final String substring = s2.substring(0, index);
                if (!AgentOptions.VALID_OPTIONS.contains(substring)) {
                    throw new IllegalArgumentException(String.format("Unknown agent option \"%s\".", substring));
                }
                this.setOption(substring, s2.substring(index + 1));
            }
            this.validateAll();
        }
    }
    
    public AgentOptions(final Properties properties) {
        this();
        for (final String s : AgentOptions.VALID_OPTIONS) {
            final String property = properties.getProperty(s);
            if (property != null) {
                this.setOption(s, property);
            }
        }
    }
    
    private int getOption(String s, final int n) {
        s = this.options.get(s);
        if (s == null) {
            return n;
        }
        return Integer.parseInt(s);
    }
    
    private String getOption(String s, final String s2) {
        s = this.options.get(s);
        if (s == null) {
            return s2;
        }
        return s;
    }
    
    private boolean getOption(String s, final boolean b) {
        s = this.options.get(s);
        if (s == null) {
            return b;
        }
        return Boolean.parseBoolean(s);
    }
    
    private void setOption(final String s, final int n) {
        this.setOption(s, Integer.toString(n));
    }
    
    private void setOption(final String s, final String s2) {
        this.options.put(s, s2);
    }
    
    private void setOption(final String s, final boolean b) {
        this.setOption(s, Boolean.toString(b));
    }
    
    private void validateAll() {
        this.validatePort(this.getPort());
        this.getOutput();
    }
    
    private void validatePort(final int n) {
        if (n >= 0) {
            return;
        }
        throw new IllegalArgumentException("port must be positive");
    }
    
    public String getAddress() {
        return this.getOption("address", AgentOptions.DEFAULT_ADDRESS);
    }
    
    public boolean getAppend() {
        return this.getOption("append", true);
    }
    
    public String getClassDumpDir() {
        return this.getOption("classdumpdir", null);
    }
    
    public String getDestfile() {
        return this.getOption("destfile", "jacoco.exec");
    }
    
    public boolean getDumpOnExit() {
        return this.getOption("dumponexit", true);
    }
    
    public String getExclClassloader() {
        return this.getOption("exclclassloader", "sun.reflect.DelegatingClassLoader");
    }
    
    public String getExcludes() {
        return this.getOption("excludes", "");
    }
    
    public boolean getInclBootstrapClasses() {
        return this.getOption("inclbootstrapclasses", false);
    }
    
    public boolean getInclNoLocationClasses() {
        return this.getOption("inclnolocationclasses", false);
    }
    
    public String getIncludes() {
        return this.getOption("includes", "*");
    }
    
    public boolean getJmx() {
        return this.getOption("jmx", false);
    }
    
    public OutputMode getOutput() {
        final String s = this.options.get("output");
        if (s == null) {
            return OutputMode.file;
        }
        return OutputMode.valueOf(s);
    }
    
    public int getPort() {
        return this.getOption("port", 6300);
    }
    
    public String getQuotedVMArgument(final File file) {
        return CommandLineSupport.quote(this.getVMArgument(file));
    }
    
    public String getSessionId() {
        return this.getOption("sessionid", null);
    }
    
    public String getVMArgument(final File file) {
        return String.format("-javaagent:%s=%s", file, this);
    }
    
    public String prependVMArguments(final String s, final File file) {
        final List<String> split = CommandLineSupport.split(s);
        final String format = String.format("-javaagent:%s", file);
        final Iterator<String> iterator = split.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().startsWith(format)) {
                iterator.remove();
            }
        }
        split.add(0, this.getVMArgument(file));
        return CommandLineSupport.quote(split);
    }
    
    public void setAddress(final String s) {
        this.setOption("address", s);
    }
    
    public void setAppend(final boolean b) {
        this.setOption("append", b);
    }
    
    public void setClassDumpDir(final String s) {
        this.setOption("classdumpdir", s);
    }
    
    public void setDestfile(final String s) {
        this.setOption("destfile", s);
    }
    
    public void setDumpOnExit(final boolean b) {
        this.setOption("dumponexit", b);
    }
    
    public void setExclClassloader(final String s) {
        this.setOption("exclclassloader", s);
    }
    
    public void setExcludes(final String s) {
        this.setOption("excludes", s);
    }
    
    public void setInclBootstrapClasses(final boolean b) {
        this.setOption("inclbootstrapclasses", b);
    }
    
    public void setInclNoLocationClasses(final boolean b) {
        this.setOption("inclnolocationclasses", b);
    }
    
    public void setIncludes(final String s) {
        this.setOption("includes", s);
    }
    
    public void setJmx(final boolean b) {
        this.setOption("jmx", b);
    }
    
    public void setOutput(final String s) {
        this.setOutput(OutputMode.valueOf(s));
    }
    
    public void setOutput(final OutputMode outputMode) {
        this.setOption("output", outputMode.name());
    }
    
    public void setPort(final int n) {
        this.validatePort(n);
        this.setOption("port", n);
    }
    
    public void setSessionId(final String s) {
        this.setOption("sessionid", s);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String s : AgentOptions.VALID_OPTIONS) {
            final String s2 = this.options.get(s);
            if (s2 != null) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(s);
                sb.append('=');
                sb.append(s2);
            }
        }
        return sb.toString();
    }
    
    public enum OutputMode
    {
        file, 
        none, 
        tcpclient, 
        tcpserver;
    }
}
