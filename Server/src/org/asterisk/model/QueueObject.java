package org.asterisk.model;

import java.io.Serializable;

public class QueueObject implements Serializable {

    private String queueId;
    private String queueName;

    public QueueObject() {
    }

    public QueueObject(String id, String name) {
        this.queueId = id;
        this.queueName = name;
    }

    public void setQueueId(String id) {
        queueId = id;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueName(String name) {
        queueName = name;
    }

    public String getQueueName() {
        return queueName;
    }
}
