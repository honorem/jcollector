/*
 * The MIT License
 *
 * Copyright 2015 cambierr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.cambierr.jcollector.metric;

import com.github.cambierr.jcollector.worker.Worker;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cambierr
 */
public class Metric {

    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public final String name;
    public final ConcurrentLinkedQueue<Entry> entries;

    /**
     * Creates a metric from its name
     *
     * @param _name the metric name
     */
    public Metric(String _name) {
        name = _name;
        entries = new ConcurrentLinkedQueue<>();
        register();
    }

    /**
     * Pushs a new entry for this metric
     *
     * @param _entry the new entry
     */
    public void push(Entry _entry) {
        entries.add(_entry);
    }

    private void register() {
        Worker.getInstance().metrics.add(this);
    }

    /**
     * Schedules a recurrent metric
     *
     * @param _delta the time (ms) between two calls
     * @param _provider the metric entry provider
     * @return a ScheduledFuture to cancel the task if required
     */
    public ScheduledFuture schedule(int _delta, Provider _provider) {
        return scheduler.scheduleAtFixedRate(() -> push(_provider.get()), 0, _delta, TimeUnit.MILLISECONDS);
    }
}
