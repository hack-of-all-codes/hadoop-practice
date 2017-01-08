package com.hadooppractice.average;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class AverageReducer extends Reducer<Text, AverageTuple, Text, AverageTuple> {

    @Override
    protected void reduce(Text key, Iterable<AverageTuple> values, Reducer<Text, AverageTuple, Text, AverageTuple>.Context context) throws IOException, InterruptedException {

        AverageTuple computedAverage = StreamSupport.stream(values.spliterator(), false).reduce(new AverageTuple(), (a,b) -> {
            Double temp = a.getAverageLength()*a.getCount() + b.getAverageLength()*b.getCount();
            Long currentCount = a.getCount() + b.getCount();
            temp = temp / currentCount;
            AverageTuple c = new AverageTuple(temp, currentCount);

            System.out.println(a + " + " + b + " = " + c);

            return c;
        });

        context.write(key, computedAverage);
    }
}
