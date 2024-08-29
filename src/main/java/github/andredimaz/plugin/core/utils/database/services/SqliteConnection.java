package github.andredimaz.plugin.core.utils.database.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import github.andredimaz.plugin.core.utils.database.DatabaseConnection;
import github.andredimaz.plugin.core.utils.database.query.QueryExecution;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SqliteConnection {
    private static HashMap connections = new HashMap();
    private Connection connection;
    private String tableName;
    private String pluginName;
    private DatabaseConnection dbConn;
    private String fileName;

    public SqliteConnection(JavaPlugin plugin, String tableName, String parameters, String fileName) {
        this.pluginName = plugin.getName();
        this.tableName = tableName;
        this.fileName = fileName;
        File folder = plugin.getDataFolder().getAbsoluteFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            Class.forName("org.sqlite.JDBC");
            if (connections.containsKey(fileName) && !((Connection)connections.get(fileName)).isClosed()) {
                this.connection = (Connection)connections.get(fileName);
            } else {
                this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/" + this.pluginName + "/" + fileName + ".db");
                connections.put(fileName, this.connection);
            }

            Throwable e = null;
            Object var7 = null;

            try {
                Statement stmt = this.connection.createStatement();

                try {
                    stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + parameters + ")");
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } catch (Throwable var16) {
                if (e == null) {
                    e = var16;
                } else if (e != var16) {
                    e.addSuppressed(var16);
                }

                throw e;
            }
        } catch (Throwable var17) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName.toUpperCase() + "] §cOcorreu um erro ao tentar conectar o sqlite:");
            var17.printStackTrace();
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public SqliteConnection(JavaPlugin plugin, String fileName) {
        this.pluginName = plugin.getName();
        this.fileName = fileName;
        File folder = plugin.getDataFolder().getAbsoluteFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            if (connections.containsKey(fileName) && !((Connection)connections.get(fileName)).isClosed()) {
                this.connection = (Connection)connections.get(fileName);
            } else {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/" + this.pluginName + "/" + fileName + ".db");
                connections.put(fileName, this.connection);
            }
        } catch (Exception var5) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName.toUpperCase() + "] §cOcorreu um erro ao tentar conectar o sqlite:");
            var5.printStackTrace();
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public SqliteConnection importTable(String tableName, String parameters) {
        try {
            this.tableName = tableName;
            Statement stmt = this.connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + parameters + ");");
            stmt.close();
        } catch (Exception var4) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName.toUpperCase() + "] §cOcorreu um erro ao tentar criar a tabela " + tableName + ":");
            var4.printStackTrace();
        }

        return this;
    }

    public QueryExecution newQuery() {
        return this.dbConn.newQuery();
    }

    public String getTableName() {
        return this.tableName;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() throws SQLException {
        this.connection.close();
        this.dbConn.close();
        connections.remove(this.fileName);
    }
}
