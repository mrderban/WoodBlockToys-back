package com.zenika.academy.woodblocktoys.Brick;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BrickFactoryTest {

    @Test
    public void knapSack() {
        //input
        Brick brick1 = Brick.builder()
                .price(0.5)
                .id(1L)
                .build();
        Brick brick2 = Brick.builder()
                .price(0.5)
                .id(2L)
                .build();

        Brick brick3 = Brick.builder()
                .price(1)
                .id(3L)
                .build();


        List<Brick> inputBrickList = new ArrayList<>();
        inputBrickList.add(brick1);
        inputBrickList.add(brick2);
        inputBrickList.add(brick3);


        Solution inputSolution = Solution.builder()
                .value(0)
                .capacity(1)
                .brickList(new ArrayList<>())
                .build();

        //output expected
        List<Brick> brickList1 = new ArrayList<>();
        brickList1.add(brick1);
        brickList1.add(brick2);


        Solution expectedSolution = Solution.builder()
                .value(1)
                .capacity(0)
                .brickList(brickList1)
                .build();

        Solution resultSolution = BrickFactory.knapSack(inputBrickList, inputSolution, inputBrickList.size() - 1);
        Set<Brick> expectedBrickSet = new HashSet<Brick>(expectedSolution.getBrickList());
        Set<Brick> resultBrickSet = new HashSet<Brick>(resultSolution.getBrickList());


        assertThat(expectedBrickSet).isEqualTo(resultBrickSet);
        assertThat(expectedSolution.getCapacity()).isEqualTo(expectedSolution.getCapacity());
        assertThat(expectedSolution.getValue()).isEqualTo(expectedSolution.getValue());
    }

    @Test
    public void knapSackTest2() {
        //input
        Brick brick1 = Brick.builder()
                .price(0.5)
                .id(1L)
                .build();
        Brick brick2 = Brick.builder()
                .price(0.5)
                .id(2L)
                .build();

        Brick brick3 = Brick.builder()
                .price(1)
                .id(3L)
                .build();
        Brick brick4 = Brick.builder()
                .price(1.3)
                .id(4L)
                .build();

        List<Brick> inputBrickList = new ArrayList<>();
        inputBrickList.add(brick1);
        inputBrickList.add(brick2);
        inputBrickList.add(brick3);
        inputBrickList.add(brick4);

        Solution inputSolution = Solution.builder()
                .value(0)
                .capacity(2)
                .brickList(new ArrayList<>())
                .build();

        //output expected
        List<Brick> brickList1 = new ArrayList<>();
        brickList1.add(brick1);
        brickList1.add(brick2);
        brickList1.add(brick3);


        Solution expectedSolution = Solution.builder()
                .value(2)
                .capacity(0)
                .brickList(brickList1)
                .build();

        Solution resultSolution = BrickFactory.knapSack(inputBrickList, inputSolution, inputBrickList.size() - 1);
        Set<Brick> expectedBrickSet = new HashSet<Brick>(expectedSolution.getBrickList());
        Set<Brick> resultBrickSet = new HashSet<Brick>(resultSolution.getBrickList());


        assertThat(expectedBrickSet).isEqualTo(resultBrickSet);
        assertThat(expectedSolution.getCapacity()).isEqualTo(expectedSolution.getCapacity());
        assertThat(expectedSolution.getValue()).isEqualTo(expectedSolution.getValue());
    }
}