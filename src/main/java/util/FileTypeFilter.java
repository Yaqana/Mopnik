package util;

// Source of this code: http://www.codejava.net/java-se/swing/file-picker-component-in-swing

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileTypeFilter extends FileFilter {

    private String extension;
    private String description;

    public FileTypeFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(extension);
    }

    public String getDescription() {
        return description + String.format(" (*%s)", extension);
    }
}
