package com.apsis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/counter")
public class Controller {
    public static List<Counter> counterList = new ArrayList<Counter>();

    @Autowired
    private CounterService counterService;

    @RequestMapping(method = RequestMethod.POST, value = "/createCounter")
    public void createCounter(@RequestBody Counter counter) {
        boolean counterFound = false;
        for (Counter tmpCounter : counterList) {
            if (tmpCounter.getCounterName().equals(counter.getCounterName())) {
                counterFound = true;
                break;
            }
        }
        if (!counterFound) {
            counterList.add(counter);
        } else {
            throw new CounterNotFoundException(counter.getCounterName());
        }
    }


    @RequestMapping("/getCurrentValue/{counterName}")
    public int getCurrentValueByName(@PathVariable(value = "counterName", required = true) String counterName) {
        int counterValue = -1;
        boolean counterFound = false;

        for (Counter tmpCounter : counterList) {
            if (tmpCounter.getCounterName().equals(counterName)) {
                counterValue = tmpCounter.getValue();
                counterFound = true;
                break;
            }
        }

        if (!counterFound) {
            throw new CounterNotFoundException(counterName);
        }

        return counterValue;

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/incrementCounter/{counterName}")
    public void incrementCounterById(@PathVariable(value = "counterName", required = true) String counterName) {

        boolean counterFound = false;

        for (Counter tmpCounter : counterList) {
            if (tmpCounter.getCounterName().equals(counterName)) {
                tmpCounter.setValue(tmpCounter.getValue() + 1);
                counterFound = true;
                break;
            }
        }

        if (!counterFound) {
            throw new CounterNotFoundException(counterName);
        }

    }

    @RequestMapping("/getCounterList")
    public List<Counter> getCountersList() {
        return counterList;
    }
}


class Counter {
    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private String counterName;
    private int value;
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class CounterNotFoundException extends RuntimeException {
    public CounterNotFoundException(String counterName) {
        super("Could not find counter with name:  '" + counterName + "'.");
    }
}