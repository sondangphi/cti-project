package org.meetmejava.event;

public class Event {
        private final EventType type;
        private final Object data;

        public Event(EventType type) {
                this.type = type;
                data = null;
        }

        public Event(EventType type, Object data) {
                this.type = type;
                this.data = data;
        }

        public EventType getType() {
                return type;
        }

        public Object getData() {
                return data;
        }

}