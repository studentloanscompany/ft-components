package uk.co.slc.sfd.cp.ft.jsf.contenteditable.saving;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class PropertiesFileUpdater {

    private PropertiesFileUpdater() {

    }

    public static void update(String id, String value, String file) throws IOException {
        List<String> input = readExistingProperties(file);
        List<String> output = buildNewProperties(id, value, input);
        writeNewProperties(file, output);
    }

    private static List<String> readExistingProperties(String file) throws IOException {
        return Files.readLines(new File(file), Charset.defaultCharset());
    }

    private static List<String> buildNewProperties(String id, String value, List<String> input) {
        List<String> output = new ArrayList<>();
        boolean valueHasBeenWritten = false;
        for (String line : input) {
            if (line.startsWith(id + "=")) {
                output.add(id + "=" + value);
                valueHasBeenWritten = true;
            } else {
                output.add(line);
            }
        }

        if (!valueHasBeenWritten) {
            output.add(id + "=" + value);
        }

        return output;
    }

    private static void writeNewProperties(String file, List<String> output) throws IOException {
        Files.write(Joiner.on("\r").join(output), new File(file), Charset.defaultCharset());
    }
}
