package org.meetmejava;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class Connection {
        private final ManagerConnection managerConnection;

        public Connection(String address, String uname, String pwd)
                        throws Exception {
                ManagerConnectionFactory factory = new ManagerConnectionFactory(
                                address, uname, pwd);

                this.managerConnection = factory.createManagerConnection();

        }

        public void connect() throws Exception {
                managerConnection.login();
        }

        public void disconnect() {
                managerConnection.logoff();
        }

        public ManagerConnection getManagerConnection() {
                return managerConnection;
        }

        public void addManagerEventListener(ManagerEventListener listener) {
                managerConnection.addEventListener(listener);
        }

        public void removeManagerEventListener(ManagerEventListener listener) {
                managerConnection.removeEventListener(listener);
        }

        public String sendAction(ManagerAction action) throws Exception {
                ManagerResponse response = managerConnection.sendAction(action,2000);
                return response.getResponse();
        }

}