package org.meetmejava;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.meetmejava.Conference;

public class State {

        private final Connection connection;
        private final AsteriskServer asteriskServer;
        private final String asteriskURL;
        private final String asteriskAdmin;
        private final String asteriskPassword;
        private final String asteriskExtURL;
        private final String tempRecDir;
        private final Map<String, Map<String, String>> dialOutLocks = new HashMap<String, Map<String, String>>();

        public String getTempRecDir() {
                return tempRecDir;
        }

        private LiveEventHandler liveEventHandler;
        private final Map<String, Conference> startedConferences = new HashMap<String, Conference>();

        public State(Map<String, String> settings) throws Exception {
                asteriskURL = settings.get("URL");
                asteriskAdmin = settings.get("ADMIN");
                asteriskPassword = settings.get("PASSWORD");
                asteriskExtURL = settings.get("EXTENSION_URL");
                tempRecDir = settings.get("TEMP_REC_DIR");
                connection = new Connection(asteriskURL, asteriskAdmin,
                                asteriskPassword);
                connection.connect();
                asteriskServer = new DefaultAsteriskServer(connection
                                .getManagerConnection());

        }

        public void init() {
                liveEventHandler = new LiveEventHandler(this);
                liveEventHandler.init();
        }

        public void destroy() {
                liveEventHandler.destroy();
                connection.disconnect();
        }

        public Connection getConnection() {
                return connection;
        }

        public AsteriskServer getAsteriskServer() {
                return asteriskServer;
        }

        public String getAsteriskURL() {
                return asteriskURL;
        }

        public String getAsteriskAdmin() {
                return asteriskAdmin;
        }

        public String getAsteriskPassword() {
                return asteriskPassword;
        }

        public Map<String, Conference> getStartedConferences() {
                return startedConferences;
        }

        public String getAsteriskExtURL() {
                return asteriskExtURL;
        }

        public Map<String, Map<String, String>> getDialOutLocks() {
                return dialOutLocks;
        }

}

