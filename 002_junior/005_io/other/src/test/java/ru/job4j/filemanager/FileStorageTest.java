package ru.job4j.filemanager;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.util.methods.InputOutputUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FileStorageTest {

    private Path root;

    private Path temp;

    private Path source;
    private Path file1;

    public FileStorageTest() throws IOException {
        this.createStructure();
    }

    private void createStructure() throws IOException {
        this.root = Files.createTempDirectory("filemanager");
        this.temp = this.root.resolve("temp");
        this.source = Files.createDirectory(this.root.resolve("source"));
        this.file1 = Files.createFile(this.source.resolve("file1.txt"));
        try (var out = new PrintWriter(Files.newBufferedWriter(this.file1))) {
            out.println("line 1");
            out.println("line 2");
            out.print("line 3");
        }
    }

    @Before
    public void deleteIfExistsTempAll() throws IOException {
        InputOutputUtils.deleteIfExistsRecursively(this.temp);
        Files.createDirectory(this.temp);
    }

    @Test
    public void whenCurrentThenCurrentDirPath() throws IOException {
        var storageRoot = this.temp;
        Files.createFile(storageRoot.resolve("innerDir"));
        var storage = new FileStorage(storageRoot);
        assertThat(storage.current(), is(Path.of("")));
        storage.cd(Path.of("innerDir"));
        assertThat(storage.current(), is(Path.of("innerDir")));
        storage.parent();
        assertThat(storage.current(), is(Path.of("")));
    }

    @Test
    public void whenContentsThenDirectoryContents() throws IOException {
        var storageRoot = this.temp;
        Files.createDirectory(storageRoot.resolve("dir1"));
        Files.createDirectory(storageRoot.resolve("dir2"));
        Files.createFile(storageRoot.resolve("file1"));
        Files.createFile(storageRoot.resolve("file2"));
        Files.createFile(storageRoot.resolve("file3"));
        var storage = new FileStorage(storageRoot);
        var contents = storage.contents();
        assertThat(contents.getDirs(), is(List.of(Path.of("dir1"), Path.of("dir2"))));
        assertThat(contents.getOthers(), is(List.of(Path.of("file1"), Path.of("file2"), Path.of("file3"))));
    }

    @Test
    public void whenCdThenCurrentDirChanges() throws IOException {
        Path storageRoot = this.temp;
        Path dest = Path.of("dest");
        Files.createDirectory(this.temp.resolve(dest));
        var storage = new FileStorage(storageRoot);
        assertThat(storage.current(), is(Path.of("")));
        storage.cd(dest);
        assertThat(storage.current(), is(Path.of("dest")));
    }

    @Test
    public void whenCdToNotExistingDirectoryThenExceptionAndNotChanging() {
        Path storageRoot = this.temp;
        Path dest = Path.of("dest");
        var storage = new FileStorage(storageRoot);
        assertThat(storage.current(), is(Path.of("")));
        var wasException = new boolean[]{false};
        try {
            storage.cd(dest);
        } catch (FileNotFoundException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
        assertThat(storage.current(), is(Path.of("")));
    }

    @Test
    public void whenParentThenCurrentToParent() throws IOException {
        Path storageRoot = this.temp;
        Path cdPath = Path.of("dir1/dir2");
        Files.createDirectories(storageRoot.resolve("dir1/dir2"));
        var storage = new FileStorage(storageRoot);
        storage.cd(cdPath);
        assertThat(storage.current(), is(Path.of("dir1/dir2")));
        storage.parent();
        assertThat(storage.current(), is(Path.of("dir1")));
        storage.parent();
        assertThat(storage.current(), is(Path.of("")));
    }

    @Test
    public void whenParentFromRootThenExceptionAndCurrentStaysRoot() {
        var storage = new FileStorage(this.root);
        assertThat(storage.current(), is(Path.of("")));
        var wasException = new boolean[]{false};
        try {
            storage.parent();
        } catch (IllegalStateException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
        assertThat(storage.current(), is(Path.of("")));
    }

    @Test
    public void whenMkdirThenDirectoryCreated() throws IOException {
        Path storageRoot = this.temp;
        Path relative = Path.of("newDir");
        Path absolute = storageRoot.resolve(relative);
        assertTrue(Files.notExists(absolute));
        var storage = new FileStorage(storageRoot);
        storage.mkdir(relative);
        assertTrue(Files.exists(absolute));
    }

    @Test
    public void whenMkdirAlreadyExistsThenException() throws IOException {
        Path storageRoot = this.temp;
        Path relative = Path.of("newDir");
        Path absolute = storageRoot.resolve(relative);
        Files.createDirectory(absolute);
        var storage = new FileStorage(storageRoot);
        var wasException = new boolean[]{false};
        try {
            storage.mkdir(relative);
        } catch (FileAlreadyExistsException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenGetContentThenContents() throws IOException {
        var source = this.file1;
        var storageRoot = source.getParent();
        var name = source.getFileName();
        var storage = new FileStorage(storageRoot);
        byte[] result;
        try (var in = storage.getFileContents(name);
             var out = new ByteArrayOutputStream()
        ) {
            InputOutputUtils.writeAllBytes(in, out);
            result = out.toByteArray();
        }
        byte[] expected = Files.readAllBytes(source);
        assertThat(result, is(expected));
    }

    @Test
    public void whenGetContentFileNotExistThenException() throws IOException {
        var storageRoot = this.temp;
        var source = this.temp.resolve("file1");
        var storage = new FileStorage(storageRoot);
        var wasException = new boolean[]{false};
        try (var in = storage.getFileContents(source)) {
            in.toString(); // no matter
        } catch (NoSuchFileException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenPutContentsThenFileCreatedAndWritten() throws IOException {
        var dest = this.temp.resolve("dest.txt");
        var source = this.file1;
        var storageDest = dest.getFileName();
        var storageRoot = dest.getParent();
        var storage = new FileStorage(storageRoot);
        try (var contents = Files.newInputStream(source)) {
            storage.putFileContents(storageDest, contents);
        }
        byte[] result = Files.readAllBytes(dest);
        byte[] expected = Files.readAllBytes(source);
        assertThat(result, is(expected));
    }

    @Test
    public void whenPutContentsAndFileAlreadyExistsThenException() throws IOException {
        var storageRoot = this.temp;
        var dest = Files.createFile(this.temp.resolve("dest.txt"));
        var source = this.file1;
        var storage = new FileStorage(storageRoot);
        var wasException = new boolean[]{false};
        try (var contents = Files.newInputStream(source)) {
            storage.putFileContents(dest, contents);
        } catch (FileAlreadyExistsException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }
}