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
package com.github.cambierr.jcollector.worker;

import com.github.cambierr.jcollector.metric.Metric;
import com.github.cambierr.jcollector.sender.Sender;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cambierr
 */
public class Worker {

    private static Worker instance;
    public static final Logger logger = Logger.getLogger("jcollector");

    private final Timer daemon = new Timer(true);
    public final ConcurrentLinkedQueue<Metric> metrics = new ConcurrentLinkedQueue<>();

    /**
     * Returns an existing instance of the metric pusher or creates one
     *
     * @return an existing instance of the metric pusher or creates one
     */
    public synchronized static Worker getInstance() {
        if (instance == null) {
            instance = new Worker();
        }
        return instance;
    }

    /**
     * Stops and destroy the running instance, if any
     *
     * <p>
     * This won't unallocate Metric Object from memory if you linked them</p>
     */
    public synchronized static void destroy() {
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }

    /**
     * Starts the metric pusher daemon
     *
     * @param _s The sender to be used
     * @param _delta the time (in millis) between two push
     */
    public synchronized void start(Sender _s, int _delta) {
        daemon.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    _s.send(metrics);
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Could not push data through " + _s.getClass().getSimpleName(), ex);
                }
            }
        }, 0, _delta);
    }

    /**
     * Starts the metric pusher daemon
     *
     * @param _s The sender to be used
     *
     * <p>
     * Default push-time is 5000 millis</p>
     */
    public synchronized void start(Sender _s) {
        start(_s, 5000);
    }

    /**
     * Stops the metric pusher daemon
     */
    public synchronized void stop() {
        daemon.cancel();
        daemon.purge();
    }

}
