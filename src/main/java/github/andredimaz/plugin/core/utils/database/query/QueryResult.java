package github.andredimaz.plugin.core.utils.database.query;

import com.google.common.primitives.Primitives;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryResult {
    private HashMap values = new HashMap();
    private int current = -1;

    public QueryResult(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            while(rs.next()) {
                for(int i = 1; i <= columns; ++i) {
                    if (!this.values.containsKey(md.getColumnName(i))) {
                        this.values.put(md.getColumnName(i), new ArrayList());
                    }

                    ((ArrayList)this.values.get(md.getColumnName(i))).add(rs.getObject(i));
                }
            }
        } catch (SQLException var5) {
        }

    }

    public Object getObject(String columnLabel) {
        return ((ArrayList)this.values.get(columnLabel)).get(this.current);
    }

    public Object getObject(String columnLabel, Class classOfT) {
        return Primitives.wrap(classOfT).cast(((ArrayList)this.values.get(columnLabel)).get(this.current));
    }

    public String getString(String columnLabel) {
        return (String)this.getObject(columnLabel);
    }

    public Integer getInt(String columnLabel) {
        return (Integer)this.getObject(columnLabel);
    }

    public Double getDouble(String columnLabel) {
        return (Double)this.getObject(columnLabel);
    }

    public Long getLong(String columnLabel) {
        return (Long)this.getObject(columnLabel);
    }

    public Boolean getBoolean(String columnLabel) {
        return (Boolean)this.getObject(columnLabel);
    }

    public boolean next() {
        if (this.values.size() != 0 && this.current + 1 <= ((ArrayList)this.values.get(this.getColumns().get(0))).size() - 1) {
            ++this.current;
            return true;
        } else {
            return false;
        }
    }

    public int count() {
        return this.values.size() == 0 ? 0 : ((ArrayList)this.values.get(this.getColumns().get(0))).size();
    }

    public ArrayList getColumns() {
        return new ArrayList(this.values.keySet());
    }
}

