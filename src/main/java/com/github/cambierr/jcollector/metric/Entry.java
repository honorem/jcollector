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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author cambierr
 */
public class Entry {

    public final double value;
    public final long time;
    public final Collection<Tag> tags;

    /**
     * Creates a metric entry with just its value and a custom time
     *
     * @param _time the metric time
     * @param _value the metric value
     */
    public Entry(long _time, double _value) {
        this(_time, _value, new ArrayList<>());
    }

    /**
     * Creates a metric entry with value, time and a list of tags
     *
     * @param _time the metric time
     * @param _value the metric value
     * @param _tags a list of tags to be associated
     */
    public Entry(long _time, double _value, Collection<Tag> _tags) {
        value = _value;
        tags = _tags;
        time = _time;
    }

    /**
     * Creates a metric entry with value, time and a list of tags
     *
     * @param _time the metric time
     * @param _value the metric value
     * @param _tags a list of tags to be associated
     */
    public Entry(long _time, double _value, Tag[] _tags) {
        this(_time, _value, Arrays.asList(_tags));
    }

    /**
     * Creates a metric entry with just its value
     *
     * @param _value the metric value
     */
    public Entry(double _value) {
        this(System.currentTimeMillis(), _value);
    }

    /**
     * Creates a metric entry with value and a list of tags
     *
     * @param _value the metric value
     * @param _tags a list of tags to be associated
     */
    public Entry(double _value, Collection<Tag> _tags) {
        this(System.currentTimeMillis(), _value, _tags);
    }

    /**
     * Creates a metric entry with value and a list of tags
     *
     * @param _value the metric value
     * @param _tags a list of tags to be associated
     */
    public Entry(double _value, Tag[] _tags) {
        this(System.currentTimeMillis(), _value, _tags);
    }

    /**
     * Adds a tag to thie metric entry
     *
     * @param _tag the tag to be added
     *
     * @return a reference to this entry
     */
    public Entry addTag(Tag _tag) {
        tags.add(_tag);
        return this;
    }

    @Override
    public String toString() {
        return "{time: " + time + ", value: " + value + ", tags: " + tags + "}";
    }

}
