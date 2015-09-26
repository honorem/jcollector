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
package com.github.cambierr.jcollector.sender;

import com.github.cambierr.jcollector.metric.Entry;
import com.github.cambierr.jcollector.metric.Metric;
import com.github.cambierr.jcollector.metric.Tag;
import com.github.cambierr.jcollector.worker.Worker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Metric pusher through http api. Made to works with OVH's time series PAAS but should work with any other standard OpenTSDB impementation
 *
 * @author cambierr
 */
public class OpenTsdbHttp implements Sender {

    private final URL host;
    private final String auth;

    /**
     * Instanciates this Sender
     *
     * @param _host the server host
     *
     * @throws MalformedURLException in case host is malformed
     */
    public OpenTsdbHttp(String _host) throws MalformedURLException {
        host = new URL("https://" + _host + "/api/put");
        auth = null;
    }

    /**
     * Instanciates this Sender with basic authorization support
     *
     * @param _host the server host
     * @param _authorization the basic authorization
     *
     * @throws MalformedURLException in case host is malformed
     */
    public OpenTsdbHttp(String _host, String _authorization) throws MalformedURLException {
        host = new URL("https://" + _host + "/api/put");
        auth = _authorization;
    }

    @Override
    public void send(ConcurrentLinkedQueue<Metric> _metrics) throws IOException {

        JSONArray entries = toJson(_metrics);

        if (entries.length() == 0) {
            return;
        }

        HttpURLConnection conn = (HttpURLConnection) host.openConnection();
        if (auth != null) {
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((auth).getBytes()));
        }

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream body = conn.getOutputStream();
        body.write(entries.toString().getBytes());
        body.flush();

        if (conn.getResponseCode() >= 400) {
            BufferedReader responseBody = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
            String output;
            StringBuilder sb = new StringBuilder("Could not push data to OpenTSDB through ").append(getClass().getSimpleName()).append("\n");
            while ((output = responseBody.readLine()) != null) {
                sb.append(output).append("\n");
            }
            Worker.logger.log(Level.WARNING, sb.toString());
            throw new IOException(conn.getResponseMessage() + " (" + conn.getResponseCode() + ")");
        }
    }

    private JSONArray toJson(ConcurrentLinkedQueue<Metric> _metrics) {
        final JSONArray output = new JSONArray();

        _metrics.forEach((Metric t) -> {
            toJson(t, output);
        });

        return output;
    }

    private void toJson(final Metric _m, final JSONArray _acumulator) {
        _m.entries.forEach((Entry _t) -> {
            JSONObject tags = new JSONObject();
            for (Tag tag : _t.tags) {
                tags.put(tag.name, tag.value);
            }
            _acumulator.put(new JSONObject()
                    .put("metric", _m.name)
                    .put("timestamp", _t.time)
                    .put("value", _t.value)
                    .put("tags", tags)
            );
            _m.entries.remove(_t);
        });
    }

}
