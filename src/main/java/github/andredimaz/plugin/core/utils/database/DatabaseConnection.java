package github.andredimaz.plugin.core.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.andredimaz.plugin.core.managers.DatabaseManager;
import github.andredimaz.plugin.core.utils.database.query.QueryExecution;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseConnection {
    private Connection connection;
    private ExecutorService service;
    private JavaPlugin plugin;

    public DatabaseConnection(JavaPlugin plugin, Connection connection) {
        this.plugin = plugin;
        this.connection = connection;
        this.service = Executors.newSingleThreadExecutor();
        DatabaseManager.getInstance().getDatabaseConnections().add(this);
    }

    public DatabaseConnection() {
    }

    public QueryExecution newQuery() {
        return new QueryExecution(this);
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public Connection getConn() {
        return this.connection;
    }

    public ExecutorService getExecutorService() {
        return this.service;
    }

    public void close() throws SQLException {
        this.service.shutdown();
        this.connection.close();
    }
}
