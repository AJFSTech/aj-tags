package tech.ajfs.ajtags.persistence;

import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Supplier;

public class StatementProcessor {

  private final Map<String, Supplier<String>> replacements;

  public StatementProcessor() {
    this.replacements = Maps.newHashMap();
  }

  public StatementProcessor addReplacement(String key, Supplier<String> supplier) {
    this.replacements.put(key, supplier);
    return this;
  }

  public PreparedStatement prepareStatement(Connection connection, String statement)
      throws SQLException {
    for (String replacement : this.replacements.keySet()) {
      if (statement.contains(replacement)) {
        statement = statement.replace(replacement, this.replacements.get(replacement).get());
      }
    }

    return connection.prepareStatement(statement);
  }

}
