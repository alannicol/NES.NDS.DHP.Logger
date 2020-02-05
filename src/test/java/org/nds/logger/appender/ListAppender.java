package org.nds.logger.appender;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;

import java.util.ArrayList;
import java.util.List;

@Plugin(name = "ListAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ListAppender extends AbstractAppender {

    private List<LogEvent> eventList = new ArrayList<>();

    protected ListAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @PluginFactory
    public static ListAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Filter") Filter filter) {
        return new ListAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        eventList.add(event);
    }

    public void clear() {
        eventList.clear();
    }

    public List<LogEvent> getEvents() {
        return eventList;
    }

    public int size() {
        return eventList.size();
    }

    public LogEvent getEvent(int index) {

        if(index < eventList.size()) {
            return eventList.get(index);
        }

        return null;
    }
}
