package us.zonix.anticheat.runnable;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.Log;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Queue;

public class ExportLogs implements Runnable
{
    private Queue<Log> logs;
    private LordMeme plugin;
    private int count;
    
    public ExportLogs(LordMeme plugin) {
        this.plugin = plugin;
        this.count = 0;
        this.logs = plugin.getLogManager().getLogQueue();
    }

    @Override
    public void run() {

        if (this.logs.isEmpty()) {
            return;
        }

        this.count = 0;

        Iterator<Log> logIterator = this.logs.iterator();

        while (logIterator.hasNext()) {
            Log log = logIterator.next();

            Document object = new Document();
            object.put("uuid", log.getUUID().toString());
            object.put("time", new Timestamp(log.getTimestamp()).toString());
            object.put("message", log.getLog());

            MongoCollection<Document> mongoCollection = LordMeme.getInstance().getMongoDatabase().getCollection(log.getUUID().toString());
            mongoCollection.insertOne(object);

            logIterator.remove();
        }

        plugin.getLogger().info(this.count + " logs have been exported.");
    }
}
