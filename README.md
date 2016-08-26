# jcollector
![Build Status](https://travis-ci.org/cambierr/jcollector.svg?branch=master)

jcollector is a scollector-like tool to be used within your java apps.

It's not a super-pro tool, I just made it to be easily expandable since I use it in a lot of different projects to push metrics to some OpenTSDB servers like the OVH's timeseries PAAS.

Feel free to fork/contribute/add Senders

## How to get started:

```java
public class Test {
    public static void main(String[] args) throws MalformedURLException, InterruptedException, IOException {
        
        Worker.getInstance().start(new OpenTsdbHttp(
            "https://opentsdb.iot.runabove.io", 
            OpenTsdbHttp.Authorization.basic("token-id", "token-secret")
        ));
        
        Metric m = new Metric("romain.cambier.test");
        
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                m.push(new Entry(Math.random()).addTag(new Tag("server", "054")).addTag(new Tag("dc", "SBG1")));
            }
            TimeUnit.SECONDS.sleep(3);
        }
    }
    
}
```

##Maven
```xml
<dependency>
  <groupId>com.github.cambierr</groupId>
  <artifactId>jcollector</artifactId>
  <version>1.2</version>
</dependency>
```
