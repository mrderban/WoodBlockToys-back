package com.zenika.academy.woodblocktoys.Brick;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Solution {
    public double capacity;
    public int value;
    public List<Brick> brickList;
}
