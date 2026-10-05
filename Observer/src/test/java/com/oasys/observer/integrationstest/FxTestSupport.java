package com.oasys.observer.integrationstest;

import javafx.application.Platform;

final class FxTestSupport {

    private static boolean initialized;

    private FxTestSupport() {
    }

    static synchronized void initializeToolkit() {
        if (initialized) {
            return;
        }

        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // The toolkit may already have been started by another test class.
        }

        initialized = true;
    }
}
