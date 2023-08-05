package com.peetseater.filesorter;

import java.nio.file.Path;

class FileToMove {
    Path pathToFile;
    String displayName;
    
    public FileToMove(Path pathToFile, String displayName) {
        this.pathToFile = pathToFile;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}