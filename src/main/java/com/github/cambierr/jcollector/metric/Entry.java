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

    public Entry(long _time, double _value) {
        value = _value;
        tags = new ArrayList<>();
        time = _time;
    }

    public Entry(long _time, double _value, Collection<Tag> _tags) {
        value = _value;
        tags = _tags;
        time = _time;
    }

    public Entry(long _time, double _value, Tag[] _tags) {
        value = _value;
        tags = Arrays.asList(_tags);
        time = _time;
    }

    public Entry(double _value) {
        value = _value;
        tags = new ArrayList<>();
        time = System.currentTimeMillis();
    }

    public Entry(double _value, Collection<Tag> _tags) {
        value = _value;
        tags = _tags;
        time = System.currentTimeMillis();
    }

    public Entry(double _value, Tag[] _tags) {
        value = _value;
        tags = Arrays.asList(_tags);
        time = System.currentTimeMillis();
    }
    
    public Entry addTag(Tag _tag){
        tags.add(_tag);
        return this;
    }

}
