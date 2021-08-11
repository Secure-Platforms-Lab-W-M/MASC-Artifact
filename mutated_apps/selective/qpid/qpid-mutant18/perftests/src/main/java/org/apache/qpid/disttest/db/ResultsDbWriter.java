/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.disttest.db;

import static org.apache.qpid.disttest.message.ParticipantAttribute.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.disttest.controller.ResultsForAllTests;
import org.apache.qpid.disttest.message.ParticipantResult;
import org.apache.qpid.disttest.results.ResultsWriter;
import org.apache.qpid.disttest.results.aggregation.ITestResult;

public class ResultsDbWriter implements ResultsWriter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsDbWriter.class);

    private static final String RESULTS_TABLE_NAME = "RESULTS";

    /** column name */
    static final String INSERTED_TIMESTAMP = "insertedTimestamp";
    /** column name */
    static final String RUN_ID = "runId";

    private static final String TABLE_EXISTENCE_QUERY = "SELECT 1 FROM SYS.SYSTABLES WHERE TABLENAME = ?";

    private static final String CREATE_RESULTS_TABLE = String.format(
            "CREATE TABLE %1$s (" +
            "%2$s varchar(200) not null" +   // TEST_NAME
            ", %3$s bigint not null" +       // ITERATION_NUMBER
            ", %4$s varchar(200) not null" + // PARTICIPANT_NAME
            ", %5$s double not null" +       // THROUGHPUT
            ", %6$s double" +                // AVERAGE_LATENCY
            ", %7$s varchar(200)" + // CONFIGURED_CLIENT_NAME
            ", %8$s bigint" +       // NUMBER_OF_MESSAGES_PROCESSED
            ", %9$s bigint" +       // PAYLOAD_SIZE
            ", %10$s bigint" +      // PRIORITY
            ", %11$s bigint" +      // TIME_TO_LIVE
            ", %12$s bigint" +      // ACKNOWLEDGE_MODE
            ", %13$s bigint" +      // DELIVERY_MODE
            ", %14$s bigint" +      // BATCH_SIZE
            ", %15$s bigint" +      // MAXIMUM_DURATION
            ", %16$s bigint" +      // PRODUCER_INTERVAL
            ", %17$s bigint" +      // IS_TOPIC
            ", %18$s bigint" +      // IS_DURABLE_SUBSCRIPTION
            ", %19$s bigint" +      // IS_BROWSING_SUBSCRIPTION
            ", %20$s bigint" +      // IS_SELECTOR
            ", %21$s bigint" +      // IS_NO_LOCAL
            ", %22$s bigint" +      // IS_SYNCHRONOUS_CONSUMER
            ", %23$s bigint" +      // TOTAL_NUMBER_OF_CONSUMERS
            ", %24$s bigint" +      // TOTAL_NUMBER_OF_PRODUCERS
            ", %25$s bigint" +      // TOTAL_PAYLOAD_PROCESSED
            ", %26$s bigint" +      // TIME_TAKEN
            ", %27$s varchar(2000)" +  // ERROR_MESSAGE
            ", %28$s bigint" +      // MIN_LATENCY
            ", %29$s bigint" +      // MAX_LATENCY
            ", %30$s double" +      // LATENCY_STANDARD_DEVIATION
            ", %31$s double" +      // MESSAGE_THROUGHPUT
            ", %32$s varchar(200)" +      // PROVIDER_VERSION
            ", %33$s varchar(200)" +      // PROTOCOL_VERSION
            ", %34$s varchar(200) not null" +
            ", %35$s timestamp not null" +
            ")",
            RESULTS_TABLE_NAME,
            TEST_NAME.getDisplayName(),
            ITERATION_NUMBER.getDisplayName(),
            PARTICIPANT_NAME.getDisplayName(),
            THROUGHPUT.getDisplayName(),
            AVERAGE_LATENCY.getDisplayName(),
            CONFIGURED_CLIENT_NAME.getDisplayName(),
            NUMBER_OF_MESSAGES_PROCESSED.getDisplayName(),
            PAYLOAD_SIZE.getDisplayName(),
            PRIORITY.getDisplayName(),
            TIME_TO_LIVE.getDisplayName(),
            ACKNOWLEDGE_MODE.getDisplayName(),
            DELIVERY_MODE.getDisplayName(),
            BATCH_SIZE.getDisplayName(),
            MAXIMUM_DURATION.getDisplayName(),
            PRODUCER_INTERVAL.getDisplayName(),
            IS_TOPIC.getDisplayName(),
            IS_DURABLE_SUBSCRIPTION.getDisplayName(),
            IS_BROWSING_SUBSCRIPTION.getDisplayName(),
            IS_SELECTOR.getDisplayName(),
            IS_NO_LOCAL.getDisplayName(),
            IS_SYNCHRONOUS_CONSUMER.getDisplayName(),
            TOTAL_NUMBER_OF_CONSUMERS.getDisplayName(),
            TOTAL_NUMBER_OF_PRODUCERS.getDisplayName(),
            TOTAL_PAYLOAD_PROCESSED.getDisplayName(),
            TIME_TAKEN.getDisplayName(),
            ERROR_MESSAGE.getDisplayName(),
            MIN_LATENCY.getDisplayName(),
            MAX_LATENCY.getDisplayName(),
            LATENCY_STANDARD_DEVIATION.getDisplayName(),
            MESSAGE_THROUGHPUT.getDisplayName(),
            PROVIDER_VERSION.getDisplayName(),
            PROTOCOL_VERSION.getDisplayName(),
            RUN_ID,
            INSERTED_TIMESTAMP
        );

    public static final String DRIVER_NAME = "jdbcDriverClass";
    public static final String URL = "jdbcUrl";

    private final String _url;
    private final String _runId;

    private final Clock _clock;

    /**
     * @param runId may be null, in which case a default value is chosen based on current GMT time
     * @param context must contain environment entries {@value #DRIVER_NAME} and {@value #URL}.
     */
    public ResultsDbWriter(Context context, String runId)
    {
        this(context, runId, new Clock());
    }

    /** only call directly from tests */
    ResultsDbWriter(Context context, String runId, Clock clock)
    {
        _clock = clock;
        _runId = defaultIfNullRunId(runId);

        _url = initialiseJdbc(context);
    }

    private String defaultIfNullRunId(String runId)
    {
        if(runId == null)
        {
            Date dateNow = new Date(_clock.currentTimeMillis());
            Calendar calNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
            calNow.setTime(dateNow);
            return String.format("run %1$tF %1$tT.%tL", calNow);
        }
        else
        {
            return runId;
        }
    }

    public String getRunId()
    {
        return _runId;
    }

    /**
     * Uses the context's environment to load the JDBC driver class and return the
     * JDBC URL specified therein.
     * @return the JDBC URL
     */
    private String initialiseJdbc(Context context)
    {
        Hashtable<?, ?> environment = null;
        try
        {
            environment = context.getEnvironment();

            String driverName = (String) environment.get(DRIVER_NAME);
            if(driverName == null)
            {
                throw new IllegalArgumentException("JDBC driver name " + DRIVER_NAME
                        + " missing from context environment: " + environment);
            }

            Class.forName(driverName);

            Object url = environment.get(URL);
            if(url == null)
            {
                throw new IllegalArgumentException("JDBC URL " + URL + " missing from context environment: " + environment);
            }
            return (String) url;
        }
        catch (NamingException e)
        {
            throw constructorRethrow(e, environment);
        }
        catch (ClassNotFoundException e)
        {
            throw constructorRethrow(e, environment);
        }
    }

    private RuntimeException constructorRethrow(Exception e, Hashtable<?, ?> environment)
    {
        return new RuntimeException("Couldn't initialise ResultsDbWriter from context with environment" + environment, e);
    }

    private void createResultsTableIfNecessary()
    {
        try
        {
            Connection connection = null;
            try
            {
                connection = DriverManager.getConnection(_url);
                if(!tableExists(RESULTS_TABLE_NAME, connection))
                {
                    Statement statement = connection.createStatement();
                    try
                    {
                        LOGGER.info("About to create results table using SQL: " + CREATE_RESULTS_TABLE);
                        statement.execute(CREATE_RESULTS_TABLE);
                    }
                    finally
                    {
                        statement.close();
                    }
                }
            }
            finally
            {
                if(connection != null)
                {
                    connection.close();
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Couldn't create results table", e);
        }

    }

    private boolean tableExists(final String tableName, final Connection conn) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(TABLE_EXISTENCE_QUERY);
        try
        {
            stmt.setString(1, tableName);
            ResultSet rs = stmt.executeQuery();
            try
            {
                return rs.next();
            }
            finally
            {
                rs.close();
            }
        }
        finally
        {
            stmt.close();
        }
    }

    @Override
    public void begin()
    {
        createResultsTableIfNecessary();

    }

    @Override
    public void writeResults(ResultsForAllTests results, String testName)
    {
        try
        {
            writeResultsThrowingException(results);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Couldn't write results " + results, e);
        }
        LOGGER.info(this + " wrote " + results.getTestResults().size() + " results to database");
    }

    @Override
    public void end()
    {

    }

    private void writeResultsThrowingException(ResultsForAllTests results) throws SQLException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(_url);

            for (ITestResult testResult : results.getTestResults())
            {
                for (ParticipantResult participantResult : testResult.getParticipantResults())
                {
                    writeParticipantResult(connection, participantResult);
                }
            }
        }
        finally
        {
            if(connection != null)
            {
                connection.close();
            }
        }
    }

    private void writeParticipantResult(Connection connection, ParticipantResult participantResult) throws SQLException
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("About to write to DB the following participant result: " + participantResult);
        }

        PreparedStatement statement = null;
        try
        {
            String sqlTemplate = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    RESULTS_TABLE_NAME,
                    TEST_NAME.getDisplayName(),
                    ITERATION_NUMBER.getDisplayName(),
                    PARTICIPANT_NAME.getDisplayName(),
                    THROUGHPUT.getDisplayName(),
                    AVERAGE_LATENCY.getDisplayName(),
                    CONFIGURED_CLIENT_NAME.getDisplayName(),
                    NUMBER_OF_MESSAGES_PROCESSED.getDisplayName(),
                    PAYLOAD_SIZE.getDisplayName(),
                    PRIORITY.getDisplayName(),
                    TIME_TO_LIVE.getDisplayName(),
                    ACKNOWLEDGE_MODE.getDisplayName(),
                    DELIVERY_MODE.getDisplayName(),
                    BATCH_SIZE.getDisplayName(),
                    MAXIMUM_DURATION.getDisplayName(),
                    PRODUCER_INTERVAL.getDisplayName(),
                    IS_TOPIC.getDisplayName(),
                    IS_DURABLE_SUBSCRIPTION.getDisplayName(),
                    IS_BROWSING_SUBSCRIPTION.getDisplayName(),
                    IS_SELECTOR.getDisplayName(),
                    IS_NO_LOCAL.getDisplayName(),
                    IS_SYNCHRONOUS_CONSUMER.getDisplayName(),
                    TOTAL_NUMBER_OF_CONSUMERS.getDisplayName(),
                    TOTAL_NUMBER_OF_PRODUCERS.getDisplayName(),
                    TOTAL_PAYLOAD_PROCESSED.getDisplayName(),
                    TIME_TAKEN.getDisplayName(),
                    ERROR_MESSAGE.getDisplayName(),
                    MIN_LATENCY.getDisplayName(),
                    MAX_LATENCY.getDisplayName(),
                    LATENCY_STANDARD_DEVIATION.getDisplayName(),
                    MESSAGE_THROUGHPUT.getDisplayName(),
                    PROVIDER_VERSION.getDisplayName(),
                    PROTOCOL_VERSION.getDisplayName(),
                    RUN_ID,
                    INSERTED_TIMESTAMP
                    );
            statement = connection.prepareStatement(sqlTemplate);

            int columnIndex = 1;
            statement.setString(columnIndex++, participantResult.getTestName());
            statement.setInt(columnIndex++, participantResult.getIterationNumber());
            statement.setString(columnIndex++, participantResult.getParticipantName());
            statement.setDouble(columnIndex++, participantResult.getThroughput());
            statement.setDouble(columnIndex++, participantResult.getAverageLatency());
            statement.setString(columnIndex++, participantResult.getConfiguredClientName());
            statement.setLong(columnIndex++, participantResult.getNumberOfMessagesProcessed());
            statement.setLong(columnIndex++, participantResult.getPayloadSize());
            statement.setLong(columnIndex++, participantResult.getPriority());
            statement.setLong(columnIndex++, participantResult.getTimeToLive());
            statement.setLong(columnIndex++, participantResult.getAcknowledgeMode());
            statement.setLong(columnIndex++, participantResult.getDeliveryMode());
            statement.setLong(columnIndex++, participantResult.getBatchSize());
            statement.setLong(columnIndex++, participantResult.getMaximumDuration());
            statement.setLong(columnIndex++, 0 /* TODO PRODUCER_INTERVAL*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_TOPIC*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_DURABLE_SUBSCRIPTION*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_BROWSING_SUBSCRIPTION*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_SELECTOR*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_NO_LOCAL*/);
            statement.setLong(columnIndex++, 0 /* TODO IS_SYNCHRONOUS_CONSUMER*/);
            statement.setLong(columnIndex++, participantResult.getTotalNumberOfConsumers());
            statement.setLong(columnIndex++, participantResult.getTotalNumberOfProducers());
            statement.setLong(columnIndex++, participantResult.getTotalPayloadProcessed());
            statement.setLong(columnIndex++, participantResult.getTimeTaken());
            statement.setString(columnIndex++, participantResult.getErrorMessage());
            statement.setLong(columnIndex++, participantResult.getMinLatency());
            statement.setLong(columnIndex++, participantResult.getMaxLatency());
            statement.setDouble(columnIndex++, participantResult.getLatencyStandardDeviation());
            statement.setDouble(columnIndex++, participantResult.getMessageThroughput());
            statement.setString(columnIndex++, participantResult.getProviderVersion());
            statement.setString(columnIndex++, participantResult.getProtocolVersion());

            statement.setString(columnIndex++, _runId);
            statement.setTimestamp(columnIndex++, new Timestamp(_clock.currentTimeMillis()));

            statement.execute();
            connection.commit();
        }
        catch(SQLException e)
        {
            LOGGER.error("Couldn't write " + participantResult, e);
        }
        finally
        {
            if (statement != null)
            {
                statement.close();
            }
        }
    }

    public static class Clock
    {
        public long currentTimeMillis()
        {
            return System.currentTimeMillis();
        }
    }

    @Override
    public String toString()
    {
        return "ResultsDbWriter[" +
               "runId='" + _runId + '\'' +
               ", url='" + _url + '\'' +
               ']';
    }
}
