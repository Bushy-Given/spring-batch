package com.bushy.springbatchexample.config;

import com.bushy.springbatchexample.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class SpringBatchConfig {

    @Value("${input}") String path;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Employee> itemReader,
                   ItemProcessor<Employee, Employee> itemProcessor,
                   ItemWriter<Employee> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Employee, Employee>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        Job job = jobBuilderFactory.get("ETL-load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        return job;
    }

    @Bean
    public FlatFileItemReader<Employee> fileItemReader(){
        System.out.println("the path to the file is : " + path);
        FlatFileItemReader<Employee> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new ClassPathResource("users.csv"));
        fileItemReader.setName("CSV-Reader");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setLineMapper(lineMapper());

        return fileItemReader;
    }

    @Bean
    public LineMapper<Employee> lineMapper(){

        DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);
        defaultLineMapper.setLineTokenizer(tradeRecordTokenizer());
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    private LineTokenizer tradeRecordTokenizer() {
        FixedLengthTokenizer rc = new FixedLengthTokenizer();
        String[] names = new String[]{
                "id",
                "name",
                "dept",
                "salary"
        };
        rc.setNames(names);
        Range[] ranges = new Range[]{
                new Range(1,12),
                new Range(13,15),
                new Range(16,20),
                new Range(21,29)
        };
        rc.setColumns(ranges);
        return rc;
    }


}
