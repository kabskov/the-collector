package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;

public class Main {
    static void main() {
        ApplicationConfig.startServer(7070, HibernateConfig.getEntityManagerFactory());
    }
}
