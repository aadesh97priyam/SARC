package com.philips.sarc;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.File;
import org.junit.Test;

public class UtilsTest {

    // Test cases for getFileContents
    @Test
    public void nullFile() {
        String result = Utils.getFileContents(null);
        assertEquals(null, result);
    }

    @Test
    public void emptyFileContents() {
        File emptyFile = new File("emptyTextFile.txt");
        try {
            if (!emptyFile.exists()) {
                emptyFile.createNewFile();
            }
            String result = Utils.getFileContents(emptyFile);
            assertEquals("", result);
            emptyFile.delete();
        } catch (IOException ioe) {
            throw new RuntimeException("[ERROR] Couldn't execute test case\n[ERROR] Cause: " + ioe.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void fileIsADirectory() {
        File aDirectory = new File("resources");
        Utils.getFileContents(aDirectory);
    }

    @Test
    public void fileDoesntExist() {
        File whatFile = new File("doesntexist");
        assertEquals(null, Utils.getFileContents(whatFile));
    }

    @Test
    public void readABinaryFile() throws IOException {
        File file = new File("AutoBuildTest.exe");
        file.createNewFile();
        assertEquals("", Utils.getFileContents(file));
        file.delete();
    }

    // Test cases for writeFileContents
    @Test(expected = RuntimeException.class)
    public void nullFileButProperContents() {
        Utils.writeFileContents(null, "contents");
    }

    @Test
    public void properFileButNullContents() {
        File properFile = new File("hello.txt");
        Utils.writeFileContents(properFile, null);
        String actual = Utils.getFileContents(properFile);
        properFile.delete();
        assertEquals("", actual);
    }

    @Test(expected = RuntimeException.class)
    public void bothParamsBeNull() {
        Utils.writeFileContents(null, null);
    }

    @Test
    public void properArgs() {
        File file = new File("hello.txt");
        String contents = "Some contents\nHallelujah!1234@$$5";
        Utils.writeFileContents(file, contents);
        String actual = Utils.getFileContents(file);
        file.delete();
        assertEquals(contents+"\n", actual);
    }

    @Test(expected = RuntimeException.class)
    public void directoryAndproperContents() {
        File file = new File("resources");
        String contents = "Some contents\nHallelujah!1234@$$5";
        Utils.writeFileContents(file, contents);
    }

    @Test
    public void fileandEmptyContent() {
        File file = new File("hello.txt");
        Utils.writeFileContents(file, "");
        String actual = Utils.getFileContents(file);
        file.delete();
        assertEquals("", actual);
    }

    // Test cases for getStreamContents
    @Test
    public void nullStream() {
        String content = Utils.getStreamContents(null);
        assertEquals(null, content);
    }

    @Test
    public void emptyStream() {
        String content = Utils.getStreamContents(new ByteArrayInputStream(" ".getBytes()));
        assertEquals(" ", content);
    }

    @Test
    public void properStream() {
        String content = "some content..kvkwnv";
        String actual = Utils.getStreamContents(new ByteArrayInputStream(content.getBytes()));
        assertEquals(content, actual);
    }

    // Test cases for deleteDirectoryRecursion
    @Test
    public void nullPath() throws IOException {
        Utils.deleteDirectoryRecursion(null);
    }

    @Test
    public void nonExistantPath() throws IOException {
        Utils.deleteDirectoryRecursion(new File("ksjbdkd").toPath());
    }

    @Test
    public void pathPointsToFile() throws IOException {
        File file = new File("temp.txt");
        file.createNewFile();
        Utils.deleteDirectoryRecursion(file.toPath());
        if (file.exists()) {
            file.delete();
            throw new IOException("File was to be deleted!");
        }
    }

    @Test
    public void pathPointsToEmptyDirectory() throws IOException {
        File emptyDir = new File("emptyDir");
        emptyDir.mkdir();
        Utils.deleteDirectoryRecursion(emptyDir.toPath());
        if (emptyDir.exists()) {
            emptyDir.delete();
            throw new IOException("Directory not deleted!");
        }
    }

    @Test
    public void pathPointsToDirectoryWithFiles() throws IOException {
        File dir = new File("emptyDir");
        dir.mkdir();
        File tempfile = new File(dir, "temp.txt");
        tempfile.createNewFile();
        Utils.deleteDirectoryRecursion(dir.toPath());
        if (dir.exists()) {
            throw new IOException("Directory not deleted!");
        }
    }
}