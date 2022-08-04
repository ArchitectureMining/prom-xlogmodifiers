package org.processmining.plugins.xlogmodifier;

import java.text.ParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.plugin.Progress;
import org.processmining.standalone.statechart.FakeProgress;

public class XLogUtils {
	public static void sort(XLog log, List<String> keys) {
		sort(log, keys, new FakeProgress());
	}
	public static void sort(XLog log, List<String> keys, Progress progress) {
		Comparator<XEvent> compare = new Comparator<XEvent>() {
			public int compare(XEvent o1, XEvent o2) {
				XAttributeMap a1 = o1.getAttributes(), a2 = o2.getAttributes();
				for (String k : keys) {
					int c = a1.get(k).compareTo(a2.get(k));
					if (c != 0)
						return c;
				}
				return 0;
			}
		};
		sort(log, compare, progress);
	}
	public static void sort(XLog log, Comparator<XEvent> compare) {
		sort(log, compare, new FakeProgress());
	}
	public static void sort(XLog log, Comparator<XEvent> compare, Progress progress) {
		progress.setMinimum(0);
		progress.setMaximum(log.size());
		progress.setCaption("Sorting Events");
		for (XTrace trace : log) {
			trace.sort(compare);
			progress.inc();
		}
	}

	public static void retainAll(XLog log, Collection<String> log_attrs, Collection<String> trace_attrs, Collection<String> event_attrs) {
		retainAll(log, log_attrs, trace_attrs, event_attrs, new FakeProgress());
	}
	public static void retainAll(XLog log, Collection<String> log_attrs, Collection<String> trace_attrs, Collection<String> event_attrs, Progress progress) {
		progress.setValue(0);
		progress.setMaximum(log.size());
		progress.setCaption("Retaining Attributes");

		if (log_attrs != null)
			XAttributeMapUtils.retainAll(log.getAttributes(), log_attrs);
		for (XTrace trace : log) {
			if (trace_attrs != null)
				XAttributeMapUtils.retainAll(trace.getAttributes(), trace_attrs);
			if (event_attrs != null)
				for (XEvent event : trace) {
					XAttributeMapUtils.retainAll(event.getAttributes(), event_attrs);
				}
			progress.inc();
		}
	}

	public static void changeKeys(XLog log, Map<String, String> log_attrs, Map<String, String> trace_attrs, Map<String, String> event_attrs) {
		changeKeys(log, log_attrs, trace_attrs, event_attrs, new FakeProgress());
	}
	public static void changeKeys(XLog log, Map<String, String> log_attrs, Map<String, String> trace_attrs, Map<String, String> event_attrs, Progress progress) {
		progress.setValue(0);
		progress.setMaximum(log.size());
		progress.setCaption("Renaming Attributes");

		if (log_attrs != null)
			XAttributeMapUtils.changeKeys(log.getAttributes(), log_attrs);

		for (XTrace trace : log) {
			if ((trace_attrs != null && !trace_attrs.isEmpty()));
			XAttributeMapUtils.changeKeys(trace.getAttributes(), trace_attrs);

			if (event_attrs != null && !event_attrs.isEmpty())
				for (XEvent event : trace) {
					XAttributeMapUtils.changeKeys(event.getAttributes(), event_attrs);
				}

			progress.inc();
		}
	}

	public static void changeTypes(XLog log, Map<String, Class<? extends XAttribute>> log_attrs, Map<String, Class<? extends XAttribute>> trace_attrs, Map<String, Class<? extends XAttribute>> event_attrs) throws ParseException {
		changeTypes(log, log_attrs, trace_attrs, event_attrs, new FakeProgress());
	}
	public static void changeTypes(XLog log, Map<String, Class<? extends XAttribute>> log_attrs, Map<String, Class<? extends XAttribute>> trace_attrs, Map<String, Class<? extends XAttribute>> event_attrs, Progress progress) throws ParseException {
		progress.setValue(0);
		progress.setMaximum(log.size());
		progress.setCaption("Changing Attribute types");

		if (log_attrs != null)
			XAttributeMapUtils.changeTypes(log.getAttributes(), log_attrs);

		for (XTrace trace : log) {
			if ((trace_attrs != null && !trace_attrs.isEmpty()));
			XAttributeMapUtils.changeTypes(trace.getAttributes(), trace_attrs);

			if (event_attrs != null && !event_attrs.isEmpty())
				for (XEvent event : trace) {
					XAttributeMapUtils.changeTypes(event.getAttributes(), event_attrs);
				}

			progress.inc();
		}
	}
}
