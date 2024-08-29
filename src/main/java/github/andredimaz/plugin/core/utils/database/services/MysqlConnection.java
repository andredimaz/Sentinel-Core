package github.andredimaz.plugin.core.utils.database.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import github.andredimaz.plugin.core.utils.database.DatabaseConnection;
import github.andredimaz.plugin.core.utils.database.query.QueryExecution;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MysqlConnection {
    private static HashMap connections = new HashMap();
    private Connection connection;
    private Properties properties;
    private String tableName;
    private String pluginName;
    private final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private final String MAX_POOL = "250";
    private DatabaseConnection dbConn;
    private String databaseName;

    public MysqlConnection() {
    }

    public MysqlConnection(JavaPlugin plugin, String username, String password, String databaseName) {
        this.pluginName = plugin.getName();
        this.databaseName = databaseName;

        try {
            String dbUrl = "jdbc:mysql://localhost:3306/" + databaseName;
            Class.forName("com.mysql.jdbc.Driver");
            if (connections.containsKey(databaseName)) {
                this.connection = (Connection)connections.get(databaseName);
            } else {
                this.connection = DriverManager.getConnection(dbUrl, this.getProperties(username, password));
                connections.put(databaseName, this.connection);
            }
        } catch (Exception var6) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName + "] §cOcorreu um erro ao tentar conectar ao mysql:");
            var6.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cDesligando plugin...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public MysqlConnection(JavaPlugin plugin, String address, String username, String password, String databaseName) {
        this.pluginName = plugin.getName();

        try {
            String dbUrl = "jdbc:mysql://" + address + ":3306/" + databaseName;
            Class.forName("com.mysql.jdbc.Driver");
            if (connections.containsKey(databaseName) && !((Connection)connections.get(databaseName)).isClosed()) {
                this.connection = (Connection)connections.get(databaseName);
            } else {
                this.connection = DriverManager.getConnection(dbUrl, this.getProperties(username, password));
                connections.put(databaseName, this.connection);
            }
        } catch (Exception var7) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName + "] §cOcorreu um erro ao tentar conectar ao mysql:");
            var7.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cDesligando plugin...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public MysqlConnection(JavaPlugin plugin, String parameters, String username, String password, String databaseName, String tableName) {
        this.pluginName = plugin.getName();
        this.tableName = tableName;

        try {
            String dbUrl = "jdbc:mysql://localhost:3306/" + databaseName;
            Class.forName("com.mysql.jdbc.Driver");
            if (connections.containsKey(databaseName) && !((Connection)connections.get(databaseName)).isClosed()) {
                this.connection = (Connection)connections.get(databaseName);
            } else {
                this.connection = DriverManager.getConnection(dbUrl, this.getProperties(username, password));
                connections.put(databaseName, this.connection);
            }

            Throwable var8 = null;
            Object var9 = null;

            try {
                Statement stmt = this.connection.createStatement();

                try {
                    stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + parameters + ");");
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } catch (Throwable var18) {
                if (var8 == null) {
                    var8 = var18;
                } else if (var8 != var18) {
                    var8.addSuppressed(var18);
                }

                throw var8;
            }
        } catch (Throwable var19) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName + "] §cOcorreu um erro ao tentar conectar ao mysql:");
            var19.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cDesligando plugin...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public MysqlConnection(JavaPlugin plugin, String parameters, String address, String username, String password, String databaseName, String tableName) {
        this.pluginName = plugin.getName();
        this.tableName = tableName;

        try {
            String dbUrl = "jdbc:mysql://" + address + ":3306/" + databaseName;
            Class.forName("com.mysql.jdbc.Driver");
            if (connections.containsKey(databaseName) && !((Connection)connections.get(databaseName)).isClosed()) {
                this.connection = (Connection)connections.get(databaseName);
            } else {
                this.connection = DriverManager.getConnection(dbUrl, this.getProperties(username, password));
                connections.put(databaseName, this.connection);
            }

            Throwable var9 = null;
            Object var10 = null;

            try {
                Statement stmt = this.connection.createStatement();

                try {
                    stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + parameters + ");");
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } catch (Throwable var19) {
                if (var9 == null) {
                    var9 = var19;
                } else if (var9 != var19) {
                    var9.addSuppressed(var19);
                }

                throw var9;
            }
        } catch (Throwable var20) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName + "] §cOcorreu um erro ao tentar conectar ao mysql:");
            var20.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cDesligando plugin...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        this.dbConn = new DatabaseConnection(plugin, this.connection);
    }

    public MysqlConnection importTable(String tableName, String parameters) {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                Bukkit.broadcastMessage("Problema ao criar a tabela " + tableName + " (Conexão nula)");
                return null;
            }

            this.tableName = tableName;
            Statement stmt = this.connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + parameters + ");");
            stmt.close();
        } catch (Exception var4) {
            Bukkit.getConsoleSender().sendMessage("§7[" + this.pluginName + "] §cOcorreu um erro ao tentar criar a tabela " + tableName + ":");
            var4.printStackTrace();
        }

        return this;
    }

    public QueryExecution newQuery() {
        return this.dbConn.newQuery();
    }

    private Properties getProperties(String username, String password) {
        if (this.properties == null) {
            this.properties = new Properties();
            this.properties.setProperty("user", username);
            this.properties.setProperty("password", password);
            this.properties.setProperty("MaxPooledStatements", "250");
            this.properties.setProperty("connectTimeout", "0");
            this.properties.setProperty("socketTimeout", "0");
            this.properties.setProperty("autoReconnect", "true");
        }

        return this.properties;
    }

    public void importConn(MysqlConnection mysql) {
        this.connection = mysql.connection;
        this.properties = mysql.properties;
    }

    public String getTableName() {
        return this.tableName;
    }

    public MysqlConnection setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() throws SQLException {
        this.connection.close();
        this.dbConn.close();
        connections.remove(this.databaseName);
    }
}
