package task;

import java.awt.Desktop;
import java.net.URI;

import javafx.concurrent.Task;

public class OpenBrowserTask extends Task<Void> {
    private final String url;

    public OpenBrowserTask(String url) {
        this.url = url;
    }

    @Override
    protected Void call() throws Exception {
        URI uri = new URI(this.url);
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(uri);
        return null;
    }
}
