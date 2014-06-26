package fi.helsinki.cs.tmc.core.io.zip.unzippingdecider;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import fi.helsinki.cs.tmc.core.io.FileIO;

/**
 * Represents the contents of a {@code .tmcproject.yml} file.
 */
public class TmcProjectFile {

    private static final Logger log = Logger.getLogger(TmcProjectFile.class.getName());

    private List<String> extraStudentFiles;

    public TmcProjectFile(FileIO file) {
        this.extraStudentFiles = Collections.emptyList();
        load(file);
    }

    public List<String> getExtraStudentFiles() {
        return this.extraStudentFiles;
    }

    public void setExtraStudentFiles(List<String> extraStudentFiles) {
        this.extraStudentFiles = Collections.unmodifiableList(extraStudentFiles);
    }

    private void load(FileIO file) {
        if (!file.fileExists()) {
            return;
        }
        try {
            Reader reader = file.getReader();
            try {
                Object root = new Yaml().load(reader);
                List<String> extraStudentFiles = parse(root);
                if (extraStudentFiles != null) {
                    setExtraStudentFiles(extraStudentFiles);
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to read {0}: {1}", new Object[] {file.getPath(), e.getMessage()});
        }
    }

    private List<String> parse(Object root) {
        if (!(root instanceof Map)) {
            return null;
        }
        Map<?, ?> rootMap = (Map<?, ?>) root;
        Object files = rootMap.get("extra_student_files");
        if (files instanceof List) {
            List<String> extraStudentFiles = new ArrayList<String>();
            for (Object value : (List<?>) files) {
                if (value instanceof String) {
                    extraStudentFiles.add((String) value);
                }
            }
            return extraStudentFiles;
        }
        return null;
    }
}
