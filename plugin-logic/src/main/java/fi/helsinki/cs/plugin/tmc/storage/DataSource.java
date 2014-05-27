package fi.helsinki.cs.plugin.tmc.storage;

import java.util.List;

public interface DataSource<T> {

    List<T> load();

    void save(List<T> elements);

}
