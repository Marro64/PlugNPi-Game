package db;

import java.sql.*;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.*;
import org.postgresql.jdbc.PgArray;
import org.postgresql.util.PGobject;

/* Small 'ORM' wrapper class to process queries more easily */
public class ORM {
    public static final java.sql.Connection conn = Connection.instance.getConnection();
    private static final Gson gson = new Gson();
    private static final String[] EXECUTE_UPDATE_QUERIES = new String[]{"INSERT", "UPDATE", "DELETE"};

    /* Private helper method for deserializing Objects to JSON */
    private static JsonElement valueToJson(Object value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        } else {
            Class<?> valueClass = value.getClass();

            if (valueClass == PgArray.class) {
                try {
                    return gson.toJsonTree(((PgArray) value).getArray());
                } catch (SQLException e) {
                    return new JsonArray();
                }
            } else if (valueClass == PGobject.class) {
                return gson.fromJson(((PGobject) value).getValue(), JsonElement.class);
            } else {
                return gson.toJsonTree(value);
            }
        }
    }

    private static JsonArray resultSetToJson(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            JsonObject jsonObject = new JsonObject();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(columnName);

                jsonObject.add(columnName, valueToJson(columnValue));
            }

            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    private static boolean isExecuteQuery(String query) {
        return Arrays.stream(EXECUTE_UPDATE_QUERIES).anyMatch(query::contains);
    }

    /**
     * @param query String raw query
     * @param params Vararg list of query parameters
     * @return JsonArray or null if error
     */
    public static JsonArray executeQuery(String query, Object... params) {
        if (conn == null) return null;

        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 1; i < params.length + 1; i++) {
                Object param = params[i - 1];

                /* TODO: Implement more proper way */
                if (param != null && param.getClass().isArray()) {
                    ps.setArray(i, ps.getConnection().createArrayOf("text", (Object[]) param));
                } else {
                    ps.setObject(i, param);
                }
            }

            if (isExecuteQuery(query)) {
                ps.executeUpdate();
                return resultSetToJson(ps.getGeneratedKeys());
            } else {
                return resultSetToJson(ps.executeQuery());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
