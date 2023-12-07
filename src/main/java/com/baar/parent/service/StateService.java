package com.baar.parent.service;

import com.baar.parent.model.State;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class StateService {



    public List<State> states () {

        List<State> states = Arrays.asList(
                State.builder().code("TX").name("TEXAS").population("Bean").build(),
                State.builder().code("CA").name("CALIFONIA").population("Cow").build(),
                State.builder().code("AR").name("ARIZONA").population("Rice").build(),
                State.builder().code("NY").name("NEW YORK").population("Mongo").build()
        );
        return states;

    }


}
