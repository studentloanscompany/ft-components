package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.StopWatch;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableException;
import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

@Slf4j
public class PropertyRecorder {

    private static PropertyRecorder instance;
    private String targetFile;
    private boolean newEntry = false;
    private StopWatch sw = new StopWatch();
    private StopWatch startDelay = new StopWatch();
    private Map<String, String> props = new TreeMap<String, String>();

    static {
        try {
            initialise();
        } catch (ContentEditableException e) {
            log.error("Exception in PropertyRecorder...", e);
        }
    }

    private static void initialise() throws ContentEditableException {
        String dir = ContentEditableUtils.getPropertiesDirectory();
        if (dir == null) {
            dir = System.getProperty("propertyCaptureDir");
        }
        instance = (dir != null) ? new PropertyRecorder(dir + "/capture.properties") : new PropertyRecorder();
    }

    public static PropertyRecorder getInstance() {
        return instance;
    }

    private PropertyRecorder() {
    }

    private PropertyRecorder(String targetFile) throws ContentEditableException {
        this.targetFile = targetFile;
        sw.start();
        startDelay.start();
        load();
    }

    private void load() throws ContentEditableException {
        if (!(new File(targetFile)).exists()) {
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(FileSystems.getDefault().getPath(targetFile), Charset.defaultCharset())) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    int i = line.indexOf('=');
                    String key = line.substring(0, i);
                    String val = line.substring(i + 1);
                    props.put(key, val);
                }
            }
        } catch (IOException e) {
            throw new ContentEditableException(e);
        }
    }

    public void store(String key, String val) throws ContentEditableException {

        if (targetFile == null || val.matches("\\?\\?\\?(.*)\\?\\?\\?") || startDelay.getTime() < 3000) {
            return;
        }

        String v = props.get(key);
        if (v == null || !v.equals(val)) {
            props.put(key, val);
            newEntry = true;
        }
        save();
    }

    public void save() throws ContentEditableException {
        if (!newEntry && sw.getTime() < 3000) {
            return;
        }

        try (BufferedWriter bw = Files.newBufferedWriter(FileSystems.getDefault().getPath(targetFile), Charset.defaultCharset(), StandardOpenOption.CREATE)) {
            for (String key : props.keySet()) {
                bw.write(key + "=" + props.get(key));
                bw.newLine();
            }
            newEntry = false;
            sw.reset();
        } catch (IOException e) {
            throw new ContentEditableException(e);
        }
    }
}