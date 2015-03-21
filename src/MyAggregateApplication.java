import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyAggregateApplication {

    public static class AggregationMapper extends Mapper<LongWritable, Text, Text, Text> {

        private final static Integer firstIndex = 1;
        private final static Integer secondIndex = 4;

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


            String[] fields = value.toString().split(",");

            if (fields[0].startsWith("\"")) {
                System.out.println(value.toString());
                return;
            }

            Text outputKey = new Text(fields[firstIndex]);
            Text outputValue = new Text(fields[secondIndex]);

            context.write(outputKey, outputValue);
        }
    }

    public static class AggregationReducer extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> value, Context context) throws IOException, InterruptedException {
            Aggregator aggregator = new Aggregator(value);

            Text outputValue = new Text(String.valueOf(aggregator.getUniqueOccurrences()) + "\t"
                                                     + aggregator.getMinOccurrences() + "\t"
                                                     + aggregator.getMedianOccurrences() + "\t"
                                                     + aggregator.getMaxOccurrences() + "\t"
                                                     + aggregator.getMeanOccurrences() + "\t"
                                                     + aggregator.getDeviationOccurrences() + "\t");

            context.write(key, outputValue);
        }
    }

    public static void main(String [] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "aggregation");
        job.setJarByClass(MyAggregateApplication.class);
        job.setMapperClass(AggregationMapper.class);
        job.setReducerClass(AggregationReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
    