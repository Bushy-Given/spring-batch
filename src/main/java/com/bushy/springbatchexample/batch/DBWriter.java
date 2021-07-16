package com.bushy.springbatchexample.batch;

import com.bushy.springbatchexample.model.Employee;
import com.bushy.springbatchexample.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Employee> {

    private final UserRepository userRepository;

    public DBWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void write(List<? extends Employee> users) throws Exception {
        System.out.println("saving data for users...");
        users.forEach(userRepository::save);
        System.out.println("data saved for users : " + users);
    }
}
