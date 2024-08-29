package github.andredimaz.plugin.core.utils.database.query;

import github.andredimaz.plugin.core.utils.database.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class QueryExecution {
    private DatabaseConnection databaseConn;
    private ArrayList entries = new ArrayList();
    private QueryExecution.ExecutationType executationType;
    private String query = "";
    private QueryResult out;

    public QueryExecution(DatabaseConnection databaseConn) {
        this.databaseConn = databaseConn;
    }

    public QueryExecution select(String fields, String tableName) {
        this.executationType = QueryExecution.ExecutationType.SELECT;
        this.query = "SELECT " + fields + " FROM " + tableName + " ";
        return this;
    }

    public QueryExecution insert(String tableName) {
        this.executationType = QueryExecution.ExecutationType.INSERT;
        this.query = "INSERT INTO " + tableName + " VALUES ";
        return this;
    }

    public QueryExecution update(String tableName) {
        this.executationType = QueryExecution.ExecutationType.UPDATE;
        this.query = "UPDATE " + tableName + " ";
        return this;
    }

    public QueryExecution delete(String tableName) {
        this.executationType = QueryExecution.ExecutationType.DELETE;
        this.query = "DELETE FROM " + tableName + " ";
        return this;
    }

    public QueryExecution set(String... components) {
        this.query = this.query + "SET " + String.join(", ", components) + " ";
        return this;
    }

    public QueryExecution where(String... components) {
        this.query = this.query + "WHERE " + String.join(" AND ", components) + " ";
        return this;
    }

    public QueryExecution groupBy(String... components) {
        this.query = this.query + "GROUP BY " + String.join(", ", components) + " ";
        return this;
    }

    public QueryExecution values(Object... entries) {
        this.entries = new ArrayList(this.entries);

        for(Object o : entries) {
            this.entries.add(o);
        }

        return this;
    }

    public QueryResult execute() {
        Runnable runnable = () -> {
            if (this.executationType == QueryExecution.ExecutationType.NONE) {
                this.out = null;
            } else if ((this.executationType == QueryExecution.ExecutationType.INSERT || this.executationType == QueryExecution.ExecutationType.UPDATE) && this.entries.size() == 0) {
                this.out = null;
            } else {
                if (this.executationType == QueryExecution.ExecutationType.INSERT) {
                    String additional = "";

                    for(int i = 0; i < this.entries.size() - 1; ++i) {
                        additional = additional + "?,";
                    }

                    additional = additional + "?";
                    this.query = this.query + "(" + additional + ")";
                }

                try {
                    Throwable var17 = null;
                    Object var18 = null;

                    try {
                        PreparedStatement stmt = this.databaseConn.getConn().prepareStatement(this.query);

                        try {
                            int pos = 0;

                            for(Object o : this.entries) {
                                ++pos;
                                if (o != null) {
                                    if (o instanceof String) {
                                        stmt.setString(pos, (String)o);
                                    } else if (o instanceof Integer) {
                                        stmt.setInt(pos, (Integer)o);
                                    } else if (o instanceof Long) {
                                        stmt.setLong(pos, (Long)o);
                                    } else if (o instanceof Double) {
                                        stmt.setDouble(pos, (Double)o);
                                    } else if (o instanceof Boolean) {
                                        stmt.setBoolean(pos, (Boolean)o);
                                    } else if (o instanceof Date) {
                                        stmt.setDate(pos, (Date)o);
                                    }
                                }
                            }

                            if (this.executationType != QueryExecution.ExecutationType.SELECT) {
                                stmt.executeUpdate();
                                return;
                            }

                            this.out = new QueryResult(stmt.executeQuery());
                        } finally {
                            if (stmt != null) {
                                stmt.close();
                            }

                        }

                    } catch (Throwable var14) {
                        if (var17 == null) {
                            var17 = var14;
                        } else if (var17 != var14) {
                            var17.addSuppressed(var14);
                        }

                        throw var17;
                    }
                } catch (Throwable var15) {
                    var15.printStackTrace();
                }
            }
        };
        if (this.executationType == QueryExecution.ExecutationType.SELECT) {
            runnable.run();
        } else {
            this.databaseConn.getExecutorService().submit(runnable);
        }

        return this.out;
    }

    public static enum ExecutationType {
        SELECT,
        UPDATE,
        DELETE,
        INSERT,
        NONE;
    }
}

