package yankov.args;

import org.junit.Assert;
import org.junit.Test;

public class ProgramArgumentsParserTest {
    @Test
    public void parse_NoArguments_DefaultArguments() {
        String[] args = new String[]{};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("input.txt", appArgs.getInput());
        Assert.assertEquals("output.txt", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("default-arg-1", appArgs.getArg1());
        Assert.assertEquals("default-arg-2", appArgs.getArg2());
    }

    @Test
    public void parse_NoOptions_DefaultValuesUsed() {
        String[] args = new String[]{"arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("input.txt", appArgs.getInput());
        Assert.assertEquals("output.txt", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }

    @Test
    public void parse_ShortOptionsNoValues_DefaultValuesUsed() {
        String[] args = new String[]{"-i", "-o", "arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("input.txt", appArgs.getInput());
        Assert.assertEquals("output.txt", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }

    @Test
    public void parse_ShortOptionsValues_ProvidedValuesUsed() {
        String[] args = new String[]{"-iProvidedInput", "-oProvidedOutput", "arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("ProvidedInput", appArgs.getInput());
        Assert.assertEquals("ProvidedOutput", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }

    @Test
    public void parse_LongOptionsNoValues_DefaultValuesUsed() {
        String[] args = new String[]{"--input", "--output", "arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("input.txt", appArgs.getInput());
        Assert.assertEquals("output.txt", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }

    @Test
    public void parse_LongOptionsValues_ProvidedValuesUsed() {
        String[] args = new String[]{"--input=ProvidedInput", "--output=ProvidedOutput", "arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("ProvidedInput", appArgs.getInput());
        Assert.assertEquals("ProvidedOutput", appArgs.getOutput());
        Assert.assertFalse(appArgs.isFlag1());
        Assert.assertFalse(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }

    @Test
    public void parse_Flags_FlagsUp() {
        String[] args = new String[]{"-f", "--flag-two", "arg1", "arg2"};

        AppArgs appArgs = new AppArgs();
        Assert.assertTrue(ProgramArgumentsParser.parse(args, appArgs));

        Assert.assertEquals("input.txt", appArgs.getInput());
        Assert.assertEquals("output.txt", appArgs.getOutput());
        Assert.assertTrue(appArgs.isFlag1());
        Assert.assertTrue(appArgs.isFlag2());
        Assert.assertEquals("arg1", appArgs.getArg1());
        Assert.assertEquals("arg2", appArgs.getArg2());
    }
}
