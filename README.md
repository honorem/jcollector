# jcollector
jcollector is a scollector-like tool to be used within your java apps.

It's not a super-pro tool, I just made it to be easily expandable since I use it in a lot of different projects to push metrics to some OpenTSDB servers hosted by OVH (ovh.com).

Feel free to fork/contribute/add Senders

## How to get started:

```java
public class Test {
    public static void main(String[] args) throws MalformedURLException, InterruptedException, IOException {
        
        Worker.getInstance().start(new OpenTsdbHttp("opentsdb.iot.runabove.io", "w3eaaaqaa7pff:xyz123xyz123xyz123xyz123"));
        
        Metric m = new Metric("romain.cambier.test");
        
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                m.push(new Entry((i+1)*(j+1)).addTag(new Tag("server", "054")).addTag(new Tag("dc", "SBG1")));
            }
            TimeUnit.SECONDS.sleep(5);
        }
    }
    
}
```

##Maven
```xml
<dependency>
  <groupId>com.github.cambierr</groupId>
  <artifactId>jcollector</artifactId>
  <version>...</version>
</dependency>
```
