package github.andredimaz.plugin.core.managers;

import java.sql.SQLException;
import java.util.ArrayList;

import github.andredimaz.plugin.core.Main;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseManager {
    private static DatabaseManager instance;
    private ArrayList databaseConnection = new ArrayList();

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }

    public ArrayList getDatabaseConnections() {
        return this.databaseConnection;
    }

}

