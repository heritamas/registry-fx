package regfx.model;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class MainModel {

    private final static String URLS = "URLs";
    private Preferences userUrlsNode = Preferences.userNodeForPackage(MainModel.class).node(URLS);
    private Logger log = Logger.getLogger("regfx");

    private ObservableMap<String, Map<String, String>> observablePreferences = FXCollections.observableMap(new HashMap<>());

    public MainModel() {
        addPreferencesListener((MapChangeListener<? super String, ? super Map<String, String>>) chg -> {
            if (chg.wasAdded()) {
                setNode(chg.getKey(), chg.getValueAdded());
            } else if (chg.wasRemoved()) {
                removeNode(chg.getKey());
            }

            try {
                savePreferences();
            } catch (BackingStoreException e) {
                log.throwing(this.getClass().getPackageName(), "MainModel", e);
            }
        });
    }

    public void addPreferencesListener(MapChangeListener<? super String, ? super Map<String, String>> listener) {
        observablePreferences.addListener(listener);
    }

    private void readNode(String nodeName)  {
        try {
            Preferences nodeForUrl  = userUrlsNode.node(nodeName);
            Map<String, String> props = Arrays.stream(nodeForUrl.keys())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            key -> nodeForUrl.get(key, "")
            ));
            addPreferences(nodeName, props);
        } catch (BackingStoreException e) {
            log.throwing(this.getClass().getPackageName(), "readNode", e);
        }
    }

    private void setNode(String nodeName, Map<String, String> props)  {
        Preferences nodeForUrl = userUrlsNode.node(nodeName);
        props.forEach(nodeForUrl::put);
    }

    private void removeNode(String nodeName)  {
        try {
            userUrlsNode.node(nodeName).removeNode();
        } catch (BackingStoreException e) {
            log.throwing(this.getClass().getPackageName(), "MainModel", e);
        }

    }

    private void savePreferences() throws BackingStoreException {
        userUrlsNode.flush();
    }

    public void readPreferences() throws BackingStoreException {
        Arrays.stream(userUrlsNode.childrenNames()).forEach(this::readNode);
    }

    public void addPreferences(String key, Map<String, String> props) {
        observablePreferences.put(key, props);
    }

    public void removePreferences(String key) {
        observablePreferences.remove(key);
    }
}
