package ole.core;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    private final Ole ole = new Ole();

    public static void main(String[] args) {

        new Main().run(args);
    }

    private void run(String[] args) {
        OleOption oleOption = new Main().parseArguments(args);

        if (oleOption.isRunServer()) {
            System.err.println("欸嘿嘿");
        }

        if (oleOption.isHelpNeeded()) {
            printUsage(oleOption);

            return;
        }

        if (oleOption.isCarve()) {
            String carvePath = System.getProperty("user.dir");
//            System.err.println("carvePath = "+carvePath);
            ole.generate(carvePath);
            return;
        }

        if (oleOption.isGen()) {
            ole.generate(oleOption.getLocalPath());
            return;
        }

        if (oleOption.isRunServer()&& oleOption.isHasPath()) {
            ole.runServer(oleOption.getPath());
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
