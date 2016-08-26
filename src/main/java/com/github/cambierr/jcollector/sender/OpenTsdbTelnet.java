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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Metric pusher through telnet. Made to works with OVH's time series PAAS but should work with any other standard OpenTSDB impementation
 *
 * @author cambierr
 */
public class OpenTsdbTelnet implements Sender {

    private final DataOutputStream link;

    /**
     * Instanciates this Sender
     *
     * @param _host the server host
     * @param _port the server port
     *
     * @throws java.io.IOException in case authentication goes wrong
     */
    public OpenTsdbTelnet(String _host, int _port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) factory.createSocket(_host, _port);
        link = new DataOutputStream(sslsocket.getOutputStream());
    }

    /**
     * Instanciates this Sender with basic authentication support
     *
     * @param _host the server host
     * @param _port the server port
     * @param _auth the authentication creds
     *
     * @throws java.io.IOException in case authentication goes wrong
     */
    public OpenTsdbTelnet(String _host, int _port, String _auth) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) factory.createSocket(_host, _port);
        link = new DataOutputStream(sslsocket.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
        link.writeBytes("auth " + _auth + "\n");
        String response = input.readLine();
        if (response == null || !response.equals("ok")) {
            throw new IOException("authentication failed");
        }
    }

    @Override
    public void send(ConcurrentLinkedQueue<Metric> _metrics) throws IOException {
        String entries = toTelnet(_metrics);
        if (entries.length() == 0) {
            return;
        }
        link.writeUTF(entries);
    }

    private String toTelnet(ConcurrentLinkedQueue<Metric> _metrics) {
        final StringBuilder output = new StringBuilder();

        _metrics.forEach((Metric t) -> {
            toTelnet(t, output);
        });

        return output.toString();
    }

    private void toTelnet(final Metric _m, final StringBuilder _acumulator) {
        _m.entries.forEach((Entry _t) -> {
            _acumulator.append("put ")
                    .append(_m.name)
                    .append(" ")
                    .append(_t.time)
                    .append(" ")
                    .append(_t.value);

            _t.tags.stream().forEach((tag) -> {
                _acumulator.append(" ").append(tag.name).append("=").append(tag.value);
            });
            _acumulator.append("\n");
            _m.entries.remove(_t);
        });
    }
}
