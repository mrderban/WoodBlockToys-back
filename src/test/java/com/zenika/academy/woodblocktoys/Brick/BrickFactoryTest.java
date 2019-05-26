package com.zenika.academy.woodblocktoys.Brick;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BrickFactoryTest {

    @Test
    public void knapSack() {

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

        List<Brick> brickList = new ArrayList<>();
        brickList.add(brick1);
        brickList.add(brick2);
        brickList.add(brick3);
        brickList.add(brick4);

        Solution solutionIn = Solution.builder()
                .value(0)
                .capacity(2)
                .brickList(brickList)
                .build();

        List<Brick> brickList1 = new ArrayList<>();
        brickList1.add(brick1);
        brickList1.add(brick2);
        brickList1.add(brick3);


        Solution solutionOut = Solution.builder()
                .value(3)
                .capacity(0)
                .brickList(brickList1)
                .build();

        assertThat(BrickFactory.knapSack(solutionIn, 3)).isEqualTo(solutionOut);
    }
}