package yankov.args;

import yankov.args.annotations.*;
import yankov.args.annotations.ProgramOption;

public class AppArgs {
    @ProgramOption(shortName = "i", longName = "input", defaultValue = "input.txt")
    private String input;

    @ProgramOption(shortName = "o", longName = "output", defaultValue = "output.txt")
    private String output;

    @ProgramFlag(shortName = "f", longName = "flag-one")
    private boolean flag1;

    @ProgramFlag(shortName = "", longName = "flag-two")
    private boolean flag2;

    @ProgramArgument(order = 0, defaultValue = "default-arg-1")
    private String arg1;

    @ProgramArgument(order = 1, defaultValue = "default-arg-2")
    private String arg2;

    private double someOtherField;

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public boolean isFlag1() {
        return flag1;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }
}
