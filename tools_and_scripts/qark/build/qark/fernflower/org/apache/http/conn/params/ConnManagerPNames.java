package org.apache.http.conn.params;

@Deprecated
public interface ConnManagerPNames {
   String MAX_CONNECTIONS_PER_ROUTE = "http.conn-manager.max-per-route";
   String MAX_TOTAL_CONNECTIONS = "http.conn-manager.max-total";
   String TIMEOUT = "http.conn-manager.timeout";
}
