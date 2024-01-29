package ole.core;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    private final Ole ole = new Ole();

    public static void main(String[] args) {

        new Main().run(new String[]{"-g=C:\\Users\\spiderman\\Desktop\\ole-demo"});
    }

    private void run(String[] args) {
        OleOption oleOption = new Main().parseArguments(args);

        if (oleOption.isHelpNeeded()) {
            printUsage(oleOption);

            return;
        }

        if (oleOption.isGen()) {
            System.err.println("help oleOption.getLocalPath()= " + oleOption.getLocalPath());
            ole.generate(oleOption.getLocalPath());
            return;
        }


    }

    private void printUsage(Object options) {
        CommandLine cli = new CommandLine(options);
        cli.setUsageHelpLongOptionsMaxWidth(28);
        cli.usage(System.err);
    }

    private OleOption parseArguments(String[] args) {
        return CommandLine.populateCommand(new OleOption(), args);
    }
}
